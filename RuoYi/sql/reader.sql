create table reader
(
    id          int auto_increment
        primary key,
    reader_no   varchar(255) not null,
    reader_name varchar(20)  null,
    phone       varchar(20)  null,
    create_time datetime     null
);

