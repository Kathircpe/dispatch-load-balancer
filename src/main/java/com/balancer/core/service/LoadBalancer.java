package com.balancer.core.service;

import com.balancer.core.model.Order;
import com.balancer.core.model.Vehicle;
import com.balancer.core.service.Nodes.OrderNode;
import com.balancer.core.service.Nodes.VehicleWithOrders;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * This class holds the core load balancing implementation
 */
public class LoadBalancer implements Balancer {
    private static final int PARALLEL_STREAM_THRESHOLD=10000;
    private final PriorityQueue<OrderNode> orders;
    private final List<VehicleWithOrders> dispatch;

    public LoadBalancer(){
        this.orders=new PriorityQueue<>();
        this.dispatch=new ArrayList<>();
    }
    @Override
    public void addNewOrders(List<Order> newOrders){
        addNewOrders(newOrders,newOrders.size()>=PARALLEL_STREAM_THRESHOLD);
        tryToDispatch();
    }
    @Override
    public void addNewVehicles(List<Vehicle> newVehicles){
        addNewVehicles(newVehicles,newVehicles.size()>=PARALLEL_STREAM_THRESHOLD);
        tryToDispatch();
    }

    private void tryToDispatch() {
        if(!orders.isEmpty()){
            Thread dispatchThread=new Thread(this::dispatch);
            dispatchThread.start();
        }
    }

    // parallel streams for parallel operations
    private void addNewOrders(List<Order> newOrders, boolean isAboveParallelStreamThreshold){
        if(isAboveParallelStreamThreshold){
            newOrders.parallelStream().forEach(newOrder-> orders.offer(new OrderNode(newOrder)));
        }else{
            newOrders.stream().forEach(newOrder-> orders.offer(new OrderNode(newOrder)));
        }
    }

    // parallel streams for parallel operations
    private void addNewVehicles(List<Vehicle> newVehicles, boolean isAboveParallelStreamThreshold){
       if(isAboveParallelStreamThreshold){
           newVehicles.parallelStream().forEach(vehicle -> {
               VehicleWithOrders v=new VehicleWithOrders(vehicle.vehicleId, vehicle.capacity);
               v.currentLatitude= vehicle.currentLatitude;
               v.currentLongitude= vehicle.currentLongitude;
               v.address= vehicle.address;
               dispatch.add(v);
           });
       }else{
           newVehicles.stream().forEach(vehicle -> {
               VehicleWithOrders v=new VehicleWithOrders(vehicle.vehicleId, vehicle.capacity);
               v.currentLatitude= vehicle.currentLatitude;
               v.currentLongitude= vehicle.currentLongitude;
               v.address= vehicle.address;
               dispatch.add(v);
           });
       }
    }

    //find the optimal solution to dispatch
    @Override
    public void dispatch(){
        List<OrderNode> orphanedOrders = new ArrayList<>();
        //traverse all the orders
        while(!orders.isEmpty()){
            OrderNode currentOrder=orders.poll();
            int nearestVehicleIndex=-1;
            int minDistance=Integer.MAX_VALUE;
            //traverse all the vehicle to find the nearest one
            for(int i=0;i<dispatch.size();i++){
                VehicleWithOrders v=dispatch.get(i);
                if(v.capacity-v.totalLoad>= currentOrder.packageWeight){
                    int distance=(int)Math.round(Haversine.findDistance(currentOrder.latitude
                                                                                , currentOrder.longitude
                                                                                , v.currentLatitude
                                                                                , v.currentLongitude));
                    nearestVehicleIndex=nearestVehicleIndex==-1||distance<minDistance?i:nearestVehicleIndex;
                    minDistance=Math.min(minDistance,distance);
                }
            }
            if(nearestVehicleIndex!=-1){
                addToDispatch(currentOrder,nearestVehicleIndex);
            }else{
                orphanedOrders.add(currentOrder);
            }

        }
        //concurrent transfer
        if(!orphanedOrders.isEmpty()){
            concurrentTransfer(orphanedOrders);
        }
    }

    //dispatch the order
    private void addToDispatch(OrderNode currentOrder, int nearestVehicle) {
        VehicleWithOrders v = dispatch.get(nearestVehicle);
        v.assignedOrders.add(currentOrder);
        //now the current pos is the delivery of the last package and the weight to the load
        v.currentLatitude= currentOrder.latitude;
        v.currentLongitude= currentOrder.longitude;
        v.totalLoad+= currentOrder.packageWeight;
        v.address= currentOrder.address;
        //parse the prev travel distance and add the current one
        int kms=Integer.parseInt(v.totalDistance.substring(0,v.totalDistance.length()-3));
        v.totalDistance=kms+" km";
    }

    //Restoring the undispatched order using concurrency(new user thread)
    private void concurrentTransfer(List<OrderNode> orphanedOrders) {
        Thread orderTransferThread=new Thread(()->{
            transferOrphanedOrders(orphanedOrders
                    , orphanedOrders.size()>=PARALLEL_STREAM_THRESHOLD);
        });
        orderTransferThread.start();
    }

    //Restoring the undispatched order implementation
    private void transferOrphanedOrders(List<OrderNode> orphanedOrders,boolean isAboveParallelStreamThreshold){
            //using parallel streams for only data sets above threshold for faster handling
           if(isAboveParallelStreamThreshold){
               orphanedOrders.parallelStream().forEach(orders::offer);
           }else{
               orphanedOrders.stream().forEach(orders::offer);
           }
    }

    @Override
    public List<VehicleWithOrders> getDispatch(){
        return this.dispatch;
    }

    private static class Haversine {
        private static final double EARTH_RADIUS_KM=6371.0;
        public static double findDistance(double lat1,double lon1,double lat2,double lon2){
            double dLat=Math.toRadians(lat2-lat1);
            double dLon=Math.toRadians(lon2-lon1);
            lat1=Math.toRadians(lat2);
            lat2=Math.toRadians(lat2);
            double a=Math.pow(Math.sin(dLat/2),2)+Math.pow(Math.sin(dLon/2),2)*Math.cos(lat1)*Math.cos(lat2);
            double b=Math.asin(Math.sqrt(a));
            return EARTH_RADIUS_KM*b;
        }
    }
}
