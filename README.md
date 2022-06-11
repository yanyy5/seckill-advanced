# seckill
Optimized on the basis of the original seckill project

## Cloud deployment (offline)
- jar package
- Performance stress testing found problems
   - The number of concurrent threads on the server side is not increasing
     - View the spring-configuration-metadata.json file and modify the parameters
   - Customized embedded tomcat development
     - keepAliveTimeOut, maxKeepAliveRequests
     
## Distributed scaling
Horizontal expansion plan (4 servers):
nginx reverse proxy (server A) to achieve load balancing (two additional servers B, C), mysql server (server D)
- Some points:
   - Modify database connection on BC
   - Remote open port connection on D
   - Because it is an on-time payment package, restarting after shutdown requires re-running the jar package on BC, as well as mariadb, redis, rocketmq on D

### Reasons for Nginx High Performance
- epoll multiplexing
   - java bio model, blocking process
   - linux select model, changes trigger polling search, the upper limit is 1024
   - epoll model, change trigger callback to read directly, no upper limit (theoretically)
- master worker process model
   - master-worker: parent-child process
   - The master process will not hang, once the worker is killed, the master will take over all sockets and reload a new worker, and hand these sockets to the new worker
   - Worker is a single thread inside, with the epoll multiplexing model, there is no blocking operation
- Coroutine mechanism
   - Thread-dependent memory model, low switching overhead
   - In case of blocking, the execution right is returned, and the code is synchronized
   - No need to lock

## Multilevel cache
- The product details page is connected to the cache
- Local data hotspot cache (Guava+redis)

## Cache inventory + async
- Join rocketmq

## Guarantee eventual consistency
- use transactional messages
- Record inventory flow status in mysql
- Handle out of stock status
