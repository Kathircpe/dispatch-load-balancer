# Load Balancer - Order Dispatch System 

![Java](https://img.shields.io/badge/Java-17+-blue.svg)

**Production-grade order dispatch system** using geospatial calculations, multi-threading, and priority queue optimization.

##  Core Features

| Feature | Description |
|---------|-------------|
| Geospatial Routing | Haversine distance calculations |
| Multi-threaded Dispatch | Concurrent processing + background threads |
| Capacity Optimization | Greedy nearest-vehicle assignment |
| Priority Queue | O(log n) order processing |
| Thread-safe | Synchronized collections |
| Orphan Recovery | Auto re-queueing |

## Architecture

```mermaid
graph TD
    A[Orders] --> B[LoadBalancer]
    C[Vehicles] --> B
    B --> D[PriorityQueue]
    B --> E[DispatchThread]
    D --> E
    E --> F[VehicleWithOrders]
    F --> G[PlanResponse]
