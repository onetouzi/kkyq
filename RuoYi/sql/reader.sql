CREATE TABLE if not exists reader (
    id INT NOT NULL PRIMARY KEY ,
    reader_no INT NOT NULL,
    reader_name VARCHAR(20),
    phone VARCHAR(20),
    create_time DATETIME
);