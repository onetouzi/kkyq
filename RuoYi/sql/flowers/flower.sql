create table flower
(
    flower_id      bigint auto_increment comment '花卉ID'
        primary key,
    flower_name    varchar(50)      null comment '花卉名称',
    flower_code    varchar(50)      null comment '花卉编码',
    flower_type    char(50)         null comment '花卉类型：0=观花类,1=观叶类,2=观果类,3=多肉类',
    price          int              null comment '价格',
    num            int              null comment '库存量',
    unit           varchar(10)      null comment '单位',
    image          varchar(500)     null comment '图片路径',
    status         char default '0' null comment '状态：0=正常,1=停用',
    create_by      varchar(50)      null comment '创建者',
    create_time    datetime         null comment '创建时间',
    update_by      varchar(50)      null comment '更新者',
    update_time    datetime         null comment '更新时间',
    remark         varchar(500)     null comment '备注',
    purchase_price mediumtext       null comment '进价',
    constraint uk_flower_code
        unique (flower_code)
)
    comment '花卉信息表' charset = utf8mb3;

create index idx_flower_type
    on flower (flower_type);

create index idx_status
    on flower (status);

