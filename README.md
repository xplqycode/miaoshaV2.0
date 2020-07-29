# 秒杀项目重构

## 1、前言：

**实习数月以来，回顾之前代码，深感自己老项目代码不足，人总是不断学习进步，基于实习期间所学，现进行项目重构。**

## 2、简介：

**秒杀项目V1**介绍及其不足之处：

当时too_young，**代码的结构层次混乱，规范性不足，异常处理不完善，功能不全**
此第二版本中，对以上问题进行完善

主要使用的技术: SpringBoot、SpringMVC、Mybatis-Plus、MySQL、Redis、Zookeeper、ElasticJob等

**所做完善或增加的新功能：**

- 对项目结构进行重构，逻辑分层更清楚
- 基于SpringBoot、Mybatis-plus简化开发
- 增加代码规范性，完善异常处理
- 增加用户登录拦截，用户登录，用户注册功能, ,done(2020-07-25)
- 基于Es-job开发定时任务进行缓存预热防止缓存雪崩,done(2020-07-25)
- redis进行ip过滤限流, 加入ip黑名单功能，定时解封， done(2020-07-29)
- 引入消息队列削峰（todo）

**系统模块分层，使结构清晰，易于扩展：**
- seckill-admin
- seckill-app 主入口，对controller的处理逻辑
- seckill-common 通用工具，比如redis
- seckill-core 对数据进行的处理，主要使用的是mybatis-plus

## 3、数据库表的设计

**秒杀系统的数据库的设计**
以下为主要数据库表的设计，包括建立了一些合适的索引

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

		
		
		