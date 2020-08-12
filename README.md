# 秒杀项目V2.0

## 1、前言：

**之前的秒杀V1.0过于粗糙。实习数月以来，学习到开发中的新"姿势"。
回顾之前代码，深感自己老项目不足，现进行项目重构与功能完善。**

## 2、简介：

**秒杀项目V1**不足之处：

当时too_young，**代码的结构层次混乱，规范性不足，异常处理不完善，功能不全**
此第二版本中，对以上问题进行完善。

主要使用的技术: SpringBoot、SpringMVC、Mybatis-Plus、MySQL、Redis、Zookeeper、ElasticJob等

**所做完善或增加的新功能说明：**

- 【完善】对项目结构进行重构，逻辑分层更清楚
- 【完善】基于SpringBoot、Mybatis-plus简化开发
- 【add】增加代码规范性，完善异常处理
- 【add】增加用户登录拦截，用户登录，用户注册功能, done_90%(2020-07-25)
- 【add】基于Es-job开发定时任务进行缓存预热防止缓存雪崩, done_100%(2020-07-25)
- 【add】redis进行过滤限流防刷, 加入IP黑名单功能，定时解封，done_100%(2020-07-29)
- 【add】基于Redis#zSet实现的延时队列，模拟下单后30分钟内未付款的，取消其订单, done_80%, 延时队列已经实现，尚未模拟使用（2020-08-01）
- 【add】引入消息队列削峰, 将请求处理放到消息队列中依次处理来削峰，但是也降低了用户体验。（TODO）
- 【add】后端整体限流，使用计数器或者令牌桶（TODO）

**系统模块分层，使结构清晰，易于扩展：**
- seckill-admin 主要是登录校验（TODO）
- seckill-app 主入口，controller及其处理逻辑
- seckill-common 通用工具，比如Redis，线程池，延时队列
- seckill-core 对数据进行的处理，主要使用的是mybatis-plus

## 3、如何设计一个秒杀系统
主要分析秒杀系统的特点及代码优化思路：
### 系统特点
- 高性能：秒杀涉及大量的并发读和并发写，因此支持高并发访问这点非常关键
- 一致性：秒杀商品减库存的实现方式同样关键，有限数量的商品在同一时刻被很多倍的请求同时来减库存，在大并发更新的过程中都要保证数据的准确性。
- 高可用：秒杀时会在一瞬间涌入大量的流量，为了避免系统宕机，保证高可用，需要做好流量限制
- 其他逻辑：不可重复秒杀、防止超卖、防作弊等

### 优化思路
**后端优化：**
- 限流：屏蔽掉无用的流量，允许少部分流量走后端。假设现在库存为 10，有 1000 个购买请求，最终只有 10 个可以成功，99% 的请求都是无效请求
- 削峰：秒杀请求在时间上高度集中于某一个时间点，瞬时流量容易压垮系统，因此需要对流量进行削峰处理，缓冲瞬时流量，尽量让服务器对资源进行平缓处理
- 异步：将同步请求转换为异步请求，来提高并发量，本质也是削峰处理
- 利用缓存：创建订单时，每次都需要先查询判断库存，只有少部分成功的请求才会创建订单，因此可以将商品信息放在缓存中，减少数据库查询
- 负载均衡：利用 Nginx 等使用多个服务器并发处理请求，减少单个服务器压力

**前端优化：**
- 限流：前端答题或验证码，来分散用户的请求
- 禁止重复提交：限定每个用户发起一次秒杀后，需等待才可以发起另一次请求，从而减少用户的重复请求
- 本地标记：用户成功秒杀到商品后，将提交按钮置灰，禁止用户再次提交请求
- 动静分离：将前端静态数据直接缓存到离用户最近的地方，比如用户浏览器、CDN 或者服务端的缓存中

**防作弊优化：**
- 隐藏秒杀接口：如果秒杀地址直接暴露，在秒杀开始前可能会被恶意用户来刷接口，因此需要在没到秒杀开始时间不能获取秒杀接口，只有秒杀开始了，才返回秒杀地址 url 和验证 MD5，用户拿到这两个数据才可以进行秒杀
- 同一个账号多次发出请求：在前端优化的禁止重复提交可以进行优化；也可以使用 Redis 标志位，每个用户的所有请求都尝试在 Redis 中插入一个 userId_secondsKill 标志位，成功插入的才可以执行后续的秒杀逻辑，其他被过滤掉，执行完秒杀逻辑后，删除标志位
- 多个账号一次性发出多个请求：一般这种请求都来自同一个 IP 地址，可以检测 IP 的请求频率，如果过于频繁则弹出一个验证码
- 多个账号不同 IP 发起不同请求：这种一般都是僵尸账号，检测账号的活跃度或者等级等信息，来进行限制。比如微博抽奖，用 iphone 的年轻女性用户中奖几率更大。通过用户画像限制僵尸号无法参与秒杀或秒杀不能成功

## 4、部分优化具体分析
### 乐观锁
悲观锁虽然可以解决超卖问题，但是加锁的时间可能会很长，会长时间的限制其他用户的访问，导致很多请求等待锁，卡死在这里，如果这种请求很多就会耗尽连接，系统出现异常。乐观锁默认不加锁，更失败就直接返回抢购失败，可以承受较高并发
每个线程在校验库存的时候会拿到当前商品的乐观锁版本号，然后在扣库存时，如果版本号不对，就会扣减失败，抛出异常结束，这样每个版本号就只能有一个线程操作成功，其他相同版本号的线程秒杀失败，就不会存在卖超问题了

### Redis限流防刷
项目采用了Redis限流，也就是计数器限流，因为其实现比较简单，统计每个ip每分钟访问次数，超过指定次数，加黑名单（可以记录数据库，也可以Redis中设置一个标识位），请求时直接查看是否为黑名单，然后是黑名单直接返回不做处理。

### 后端流量限流
与以上防刷限流针对特定ip不同的是，后端流量限流为整体限流。比如10件商品的秒杀，放1000个请求来秒杀下单是没有必要的。
一些常见的限流算法：
- 计数器算法：通过一个计数器 counter 来统计一段时间内请求的数量，并且在指定的时间之后重置计数器。该方法实现简单，但是有临界问题。例如，假设我们限流规则为每秒钟不超过 100 次接口请求，第一个 1s 时间窗口内，100 次接口请求都集中在最后的 10ms 内，在第二个 1s 的时间窗口内，100 次接口请求都集中在最开始的 10ms 内，虽然两个时间窗口内流量都符合限流要求，但是在这两个时间窗口临界的 20ms 内会集中有 200 次接口请求，如果不做限流，集中在这 20ms 内的 200 次请求就有可能压垮系统。 
- 滑动窗口法：滑动窗口算法是计数器算法的一种改进，将原来的一个时间窗口划分成多个时间窗口，并且不断向右滑动该窗口。流量经过滑动时间窗口算法整形之后，可以保证任意时间窗口内，都不会超过最大允许的限流值，从流量曲线上来看会更加平滑，可以部分解决上面提到的临界突发流量问题。
- 令牌桶法：（nginx应该也是使用此限流算法）令牌桶算法的流程： 
  - 接口限制 t 秒内最大访问次数为 n，则每隔 t/n 秒会放一个 token 到桶中
  - 桶内最多存放 b 个 token，如果 token 到达时令牌桶已经满了，那么这个 token 就会被丢弃
  - 接口请求会先从令牌桶中取 token，拿到 token 则处理接口请求，拿不到 token 则进行限流处理
  - 因为令牌桶存放了很多令牌，那么大量的突发请求会被执行，但是它不会出现临界问题，在令牌用完之后，令牌是以一个恒定的速率添加到令牌桶中的，因此不能再次发送大量突发请求 
- 漏桶算法：桶的大小是固定，桶是有洞的。桶满后，水进不来；桶内的水会以固定速率漏水
在应对秒杀，大促等高性能压力的场景时，为了保证系统的平稳运行，必须针对超过预期的流量，通过预先设定的限流规则选择性的对某些请求进行限流

### 缓存和数据一致性
常见的策略是：先更新数据库，然后删除缓存。使用懒加载避免重复更新效率低下，且产生脏数据可能性小。
但是在秒杀中，每次都删除缓存，因此导致多次缓存都不能命中，能命中缓存的次数很少，因此这种方案并不可取。会影响并发量。
所以采用先更新数据库再更新缓存，扣库存使用的是乐观锁，操作成功才更新缓存。

### Redis缓存预热
使用定时任务，每十五分钟扫描十五分钟内即将开始的商品信息放入缓存。
可以使用Spring-Boot的注解实现@Scheduled(cron = "0/5 * * * * ?")，实际是基于quartz，或者基于分布式定时任务框架Es-Job来实现

### 异步
减库存放在redis中，预减库存。然后通过消息队列削峰异步扣减数据库库存，下单。对于防止重复秒杀，可以在缓存中设置【商品+用户标识】来标记购买过，每次先查一下，已经购买过直接返回重复购买。
降低了用户体验，但是保证系统高并发下可用性。
### 负载均衡
单台服务器的处理性能是有瓶颈的，当并发量十分大时，无论怎么优化都满足不了需求，这时候就需要增加一台服务器分担原有服务器的访问压力，通过负载均衡服务器 Nginx 可以将来自用户的访问请求发到应用服务器集群中的任何一台机器

Nginx 示例配置如下：

在项目的配置文件 application.properties 中分别设置两个应用的端口号如 8888 和 9999 。

server.port=8888
server.port=9999
然后进入nginx/conf文件目录将nginx.conf配置文件中的http部分修改为如下代码：
~~~nginx
http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    upstream server_miaosha{
        server 127.0.0.1:8888 weight=1;
        server 127.0.0.1:9999 weight=1;
    }

    server {
        listen  80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            #root html;
            #index index.html index.htm;
            set $xheader $remote_addr;
            if ( $http_x_forwarded_for != '' ){
                set $xheader $http_x_forwarded_for;
            }
            proxy_set_header X-Real-IP $xheader;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header Host $http_host;
            proxy_redirect off;
            proxy_http_version 1.1;
            proxy_set_header Connection "";
            proxy_pass http://server_miaosha;
        }

        #error_page  404     /404.html;
~~~
权重weight可以根据个人需求进行设置，本文均设置为 1 ，表示访问 IP + 80 端口时两个应用按 1:1 进行轮询。

## 5、数据库表的设计

**秒杀系统的数据库的设计**
以下为主要数据库表的设计，包括建立了一些**合适的索引**

- 秒杀商品表
~~~sql
CREATE TABLE `seckill_product` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `name` VARCHAR(120) NOT NULL COMMENT '商品名称',
  `number` INT(11) NOT NULL COMMENT '库存数量',
  `start_time` DATETIME NOT NULL COMMENT '秒杀开启时间',
  `end_time` DATETIME NOT NULL COMMENT '秒杀结束时间',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `version` INT(11) NOT NULL COMMENT '版本号',
  PRIMARY KEY (`id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`)
) ENGINE=INNODB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';
~~~

- 秒杀系统订单表
~~~sql
CREATE TABLE `seckill_order` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(32) NOT NULL COMMENT '订单id',
  `passport` BIGINT(20) NOT NULL COMMENT '用户账号',
  `product_id` BIGINT(20) NOT NULL COMMENT '秒杀的商品id',
  `status` TINYINT(4) NOT NULL COMMENT '状态标示：-1=无效，0=成功',
  `create_time` DATETIME NOT NULL,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_passport` (`passport`),
  UNIQUE KEY `idx_productid_passport` (`product_id`, `passport`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='秒杀系统订单表';
~~~

- 用户信息表
~~~sql
CREATE TABLE `seckill_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `passport` varchar(128) NOT NULL COMMENT '通行证',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `phone_number` bigint(20) NOT NULL COMMENT '手机号码',
  `create_time` DATETIME NOT NULL,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_id_userid` (`passport`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='用户信息表';
~~~

- ip黑名单
~~~sql
CREATE TABLE `seckill_black_list` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `ip` varchar(128) NOT NULL COMMENT 'ip地址',
  `date_num` bigint(20) NOT NULL COMMENT '封禁日期',
  `create_time` DATETIME NOT NULL,
  `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_ip` (`ip`),
  KEY `idx_date_num` (`date_num`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='ip黑名单表';
~~~

- 一些sql脚本
~~~sql
INSERT  INTO 
`seckill_product`(`id`,`name`,`number`,`start_time`,`end_time`,`create_time`,`version`,`create_time`) 
VALUES 
(1000,'1000元秒杀iphone8',100,'2018-05-10 15:31:53','2018-05-10 15:31:53','2018-05-10 15:31:53',0,now()),
(1001,'500元秒杀ipad2',100,'2018-05-10 15:31:53','2018-05-10 15:31:53','2018-05-10 15:31:53',0,now()),
(1002,'300元秒杀小米4',100,'2018-05-10 15:31:53','2018-05-10 15:31:53','2018-05-10 15:31:53',0,now()),
(1003,'200元秒杀红米note',100,'2018-05-10 15:31:53','2018-05-10 15:31:53','2018-05-10 15:31:53',0,now());

TRUNCATE TABLE

DELECT * FROM 
~~~

##6、 参考文章
- [常见限流算法](https://gongfukangee.github.io/2019/04/04/Limit/)
- [crossoverjie：SSM(十八)秒杀架构实践](<https://crossoverjie.top/2018/05/07/ssm/SSM18-seconds-kill/>)
- [秒杀系统优化方案（下）吐血整理](<https://www.cnblogs.com/xiangkejin/p/9351501.html>)
 


		