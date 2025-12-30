create table book_type
(
    id        int auto_increment comment '主键'
        primary key,
    type_name varchar(30) null comment '类型名称',
    order_num int         null comment '排序',
    status    char        null comment '状态'
)
    comment '图书类别';

