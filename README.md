# seckill
在原先seckill秒杀项目的基础上进行优化

### 云端部署(已下线)
- 打jar包
- 性能压测发现问题
  - server端并发线程数上不去
    - 查看spring-configuration-metadata.json文件，修改参数
  - 定制化内嵌tomcat开发
    - keepAliveTimeOut, maxKeepAliveRequests

### 分布式扩展
水平扩展方案(4台服务器)：
nginx反向代理(服务器A)实现负载均衡(另外开2台服务器B,C)，mysql服务器(服务器D)
- 一些要点：
  - 在BC上修改数据库连接
  - 在D上远程开放端口连接
  - 因为是按时付费套餐，关闭以后重启需要重新运行BC上的jar包，还有D上的mariadb,redis,rocketmq

#### Nginx 高性能的原因
- epoll多路复用
  - java bio模型，阻塞进程式
  - linux select模型，变更触发轮询查找，上限数量1024
  - epoll模型，变更触发回调直接读取，无上限（理论上）
- master worker进程模型
  - master-worker：父子进程
  - master进程不会挂，一旦worker被kill，master会接管所有socket并reload出一个新的worker，把这些socket交给新的worker
  - worker内部是单线程，配合epoll多路复用模型，没有阻塞操作
- 协程机制
  - 依附于线程的内存模型，切换开销小
  - 遇阻塞即归还执行权，代码同步
  - 无需加锁

### 多级缓存
- 商品详情页接入缓存
- 本地数据热点缓存(Guava+redis)

### 缓存库存+异步化
- 加入rocketmq

### 保证最终一致性
- 用事务型消息
- mysql里记录库存流水状态
- 处理库存售罄的状态



== 2021.12.1 update ==
把原先项目搬过来，开干～
