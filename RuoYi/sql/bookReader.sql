create table borrow_record
(
    borrow_id     bigint auto_increment comment '借阅主键ID，自增唯一'
        primary key,
    reader_no     varchar(255)                           not null comment '读者ID，关联读者表主键',
    reader_name   varchar(50)                            not null comment '读者姓名，冗余存储提升查询效率',
    book_no       varchar(255)                           null comment '书籍ID，关联书籍表主键',
    book_name     varchar(100)                           not null comment '书籍名称，冗余存储提升查询效率',
    borrow_time   datetime     default CURRENT_TIMESTAMP not null comment '借阅时间，默认当前时间',
    return_time   datetime                               null comment '归还时间（NULL表示未归还）',
    borrow_status tinyint      default 1                 null comment '借阅状态：1-借阅中 2-已归还 3-逾期未还',
    overdue_days  int unsigned default '0'               null comment '逾期天数，无逾期为0，非负数',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '记录创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '记录更新时间，自动刷新',
    constraint uk_reader_book_unreturn
        unique (reader_no, book_no, borrow_status) comment '唯一约束：同一读者不可同时借阅同一本书（借阅中状态）'
)
    comment '读者书籍借阅关系表' collate = utf8mb4_unicode_ci;

create index idx_book_id
    on borrow_record (book_no)
    comment '书籍ID索引，快速查某书籍所有借阅记录';

create index idx_borrow_status
    on borrow_record (borrow_status)
    comment '状态索引，快速筛选借阅中/已归还数据';

create index idx_reader_id
    on borrow_record (reader_no)
    comment '读者ID索引，快速查某读者所有借阅记录';

