# 秒杀项目V2.0

## 1、前言：

**之前的秒杀V1.0过于粗糙。实习数月以来，学习到开发中的新"姿势"。
回顾之前代码，深感自己老项目不足，现进行项目重构与功能完善。**

## 2、简介：

**秒杀项目V1**介绍及其不足之处：

当时too_young，**代码的结构层次混乱，规范性不足，异常处理不完善，功能不全**
此第二版本中，对以上问题进行完善。

主要使用的技术: SpringBoot、SpringMVC、Mybatis-Plus、MySQL、Redis、Zookeeper、ElasticJob等

**所做完善或增加的新功能说明：**

- 【完善】对项目结构进行重构，逻辑分层更清楚
- 【完善】基于SpringBoot、Mybatis-plus简化开发
- 【add】增加代码规范性，完善异常处理
- 【add】增加用户登录拦截，用户登录，用户注册功能, done_90%(2020-07-25)
- 【add】基于Es-job开发定时任务进行缓存预热防止缓存雪崩, done_100%(2020-07-25)
- 【add】redis进行ip过滤限流, 加入ip黑名单功能，定时解封，done_100%(2020-07-29)
- 【add】基于Redis#zSet实现的延时队列，模拟下单后30分钟内未付款的，取消其订单, done_80%, 延时队列已经实现，尚未模拟使用（2020-08-01）
- 【add】引入消息队列削峰（TODO）

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

## 4、数据库表的设计

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

**注意点**
- NOT NULL的数据插入时不得为空，包括create_time

## 一些坑：
- springboot配置解析jsp视图，尚未学习模板技术，待完善
   http://www.bjpowernode.com/tutorial_springboot/826.html

		
		
		