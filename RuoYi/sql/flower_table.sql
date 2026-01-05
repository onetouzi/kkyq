-- 花卉管理系统数据库脚本

-- 1. 花卉信息表
CREATE TABLE IF NOT EXISTS `flower` (
  `flower_id` bigint NOT NULL AUTO_INCREMENT COMMENT '花卉ID',
  `flower_name` varchar(50) DEFAULT NULL COMMENT '花卉名称',
  `flower_code` varchar(50) DEFAULT NULL COMMENT '花卉编码',
  `flower_type` char(1) DEFAULT NULL COMMENT '花卉类型：0=观花类,1=观叶类,2=观果类,3=多肉类',
  `color` varchar(20) DEFAULT NULL COMMENT '颜色',
  `flowering_period` varchar(50) DEFAULT NULL COMMENT '花期',
  `price` int DEFAULT NULL COMMENT '价格',
  `num` int DEFAULT NULL COMMENT '库存量',
  `warn_value` int DEFAULT NULL COMMENT '预警值',
  `unit` varchar(10) DEFAULT NULL COMMENT '单位',
  `image` varchar(500) DEFAULT NULL COMMENT '图片路径',
  `status` char(1) DEFAULT '0' COMMENT '状态：0=正常,1=停用',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`flower_id`),
  UNIQUE KEY `uk_flower_code` (`flower_code`),
  KEY `idx_flower_type` (`flower_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='花卉信息表';

-- 2. 花卉订单表
CREATE TABLE IF NOT EXISTS `flower_order` (
  `order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `flower_id` bigint NOT NULL COMMENT '花卉ID',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '购买数量',
  `total_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '总金额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `status` varchar(20) NOT NULL DEFAULT 'PAID' COMMENT '订单状态：PAID=已支付,COMPLETED=已完成,CANCELLED=已取消',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '状态更新时间',
  PRIMARY KEY (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_flower_id` (`flower_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `flower_order_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`),
  CONSTRAINT `flower_order_ibfk_2` FOREIGN KEY (`flower_id`) REFERENCES `flower` (`flower_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='花卉订单表';

-- 3. 花卉类型字典数据
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`) 
VALUES (100, '花卉类型', 'flower_type', '0', 'admin', NOW(), '花卉类型字典')
ON DUPLICATE KEY UPDATE `dict_name`='花卉类型', `dict_type`='flower_type';

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`) VALUES
(1, 1, '观花类', '0', 'flower_type', '', 'primary', 'N', '0', 'admin', NOW(), '观花类花卉'),
(2, 2, '观叶类', '1', 'flower_type', '', 'success', 'N', '0', 'admin', NOW(), '观叶类花卉'),
(3, 3, '观果类', '2', 'flower_type', '', 'warning', 'N', '0', 'admin', NOW(), '观果类花卉'),
(4, 4, '多肉类', '3', 'flower_type', '', 'info', 'N', '0', 'admin', NOW(), '多肉类植物')
ON DUPLICATE KEY UPDATE `dict_label`=VALUES(`dict_label`), `dict_value`=VALUES(`dict_value`);

-- 4. 订单状态字典数据
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `remark`) 
VALUES (101, '订单状态', 'order_status', '0', 'admin', NOW(), '订单状态字典')
ON DUPLICATE KEY UPDATE `dict_name`='订单状态', `dict_type`='order_status';

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `remark`) VALUES
(101, 1, '已支付', 'PAID', 'order_status', '', 'primary', 'N', '0', 'admin', NOW(), '已支付订单'),
(102, 2, '已完成', 'COMPLETED', 'order_status', '', 'success', 'N', '0', 'admin', NOW(), '已完成订单'),
(103, 3, '已取消', 'CANCELLED', 'order_status', '', 'danger', 'N', '0', 'admin', NOW(), '已取消订单')
ON DUPLICATE KEY UPDATE `dict_label`=VALUES(`dict_label`), `dict_value`=VALUES(`dict_value`);

