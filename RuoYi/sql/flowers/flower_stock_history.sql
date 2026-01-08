create table flower_stock_history
(
    history_id    bigint auto_increment comment '主键ID'
        primary key,
    flower_id     bigint                                   not null comment '花卉ID',
    flower_name   varchar(100)                             null comment '花卉名称',
    user_id       bigint                                   null comment '买家ID',
    user_name     varchar(100)                             null comment '买家姓名',
    user_phone    varchar(20)                              null comment '买家电话',
    business_type char                                     not null comment '业务类型：0=进货, 1=售货, 2=订单取消',
    quantity      bigint         default 0                 null comment '变动数量(进货/退货为正, 售货为负)',
    cost_price    decimal(10, 2) default 0.00              null comment '成本价快照',
    unit_price    decimal(10, 2)                           null comment '单价快照',
    total_price   decimal(10, 2)                           null comment '总金额快照',
    profit        decimal(10, 2) default 0.00              null comment '利润快照',
    order_id      varchar(64)                              null comment '关联业务订单号',
    create_by     varchar(64)                              null comment '操作人(管理员)',
    create_time   datetime       default CURRENT_TIMESTAMP null comment '发生时间',
    remark        varchar(500)                             null comment '备注'
)
    comment '花卉库存变动及交易流水表';

create index idx_business_type
    on flower_stock_history (business_type);

create index idx_create_time
    on flower_stock_history (create_time);

create index idx_flower_id
    on flower_stock_history (flower_id);

create index idx_user_id
    on flower_stock_history (user_id);

create index idx_user_phone
    on flower_stock_history (user_phone);

