create table book
(
    id          int auto_increment comment '主键'
        primary key,
    book_no     varchar(20)                            not null comment '图书编号',
    book_name   varchar(100)                           not null comment '图书名称（加长长度，适配实际业务）',
    img         varchar(255)                           null comment '封面图片（加长存储路径）',
    author      varchar(50)                            null comment '作者',
    press       varchar(100)                           null comment '出版社',
    public_time date                                   null comment '出版时间（改用DATE类型，支持完整日期）',
    stock       int          default 0                 null comment '库存（默认0）',
    status      char         default '1'               null comment '状态（0正常 1下架）',
    type_name   varchar(255) default ''                null comment '图书类比，用逗号分割(科幻,日常)',
    create_time datetime     default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '图书表';

