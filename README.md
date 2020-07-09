# 秒杀项目重构

## 1、前言：

**实习数月以来，回顾之前代码，深感自己老项目代码不足，人总是不断学习进步，基于实习期间所学，现进行项目重构。**

## 2、简介：

**秒杀项目V1**介绍及其不足之处：

介绍：基于Spring, Mybtis，MVC, MySQL，Redis开发的模拟高并发秒杀商城

当时too young

代码的规范性，异常处理等不够完善

缺少登录模块

**秒杀项目V2**所做完善：

- 基于SpringBoot、Mybatis-plus简化开发
- 对项目结构进行重构，逻辑分层更清楚
- 增加代码规范性
- 完善异常处理
- 增加用户登录拦截，用户登录，用户注册功能
- 引入消息队列削峰（待开发，尚未完成）

# 数据库表的设计

**秒杀系统的数据库的设计**
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
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
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
~~~

**注意点**
- NOT NULL的数据插入时不得为空，包括create_time

## 一些坑：
- springboot配置解析jsp视图，尚未学习模板技术，待完善
   http://www.bjpowernode.com/tutorial_springboot/826.html

		
		
		