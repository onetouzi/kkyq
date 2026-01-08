create table buyers
(
    user_id      bigint auto_increment comment '买家ID（用户ID）'
        primary key,
    user_name    varchar(50)  not null comment '买家姓名',
    user_phone   varchar(20)  not null comment '买家手机号',
    user_email   varchar(100) null comment '买家邮箱',
    user_gender  char(5)      null comment '性别：0=女,1=男,2=未知',
    user_address varchar(500) null comment '常用收货地址',
    remark       varchar(500) null comment '备注',
    constraint uk_user_email
        unique (user_email) comment '邮箱唯一（若填写）',
    constraint uk_user_phone
        unique (user_phone) comment '手机号唯一'
)
    comment '买家表';

