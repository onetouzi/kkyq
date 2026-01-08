create table flower_order
(
    order_id    varchar(20)                   not null comment '订单ID',
    user_id     bigint                        not null comment '用户ID',
    user_name   varchar(64)    default ''     null comment '买家姓名',
    flower_id   bigint                        not null comment '花卉ID',
    flower_name varchar(128)   default ''     null comment '花卉名称',
    quantity    int            default 1      not null comment '购买数量',
    total_price decimal(10, 2) default 0.00   not null comment '总金额',
    status      varchar(20)    default 'PAID' null comment '状态：PAID=已支付,COMPLETED=已完成,CANCELLED=已取消',
    create_by   varchar(64)    default ''     null comment '创建者',
    create_time datetime                      null comment '创建时间',
    update_by   varchar(64)    default ''     null comment '更新者',
    update_time datetime                      null comment '更新时间',
    remark      varchar(500)   default ''     null comment '备注'
)
    comment '花卉订单表';

