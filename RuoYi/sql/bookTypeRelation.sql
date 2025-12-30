create table book_type_relation
(
    book_id      int          not null comment '图书id',
    book_type_id int          not null comment '图书类别id',
    book_name    varchar(100) not null comment '图书名称（加长长度，适配实际业务）',
    type_name    varchar(30)  null comment '类型名称',
    constraint idx_book_type
        unique (book_id, book_type_id)
)
    comment '图书类比关系表';

