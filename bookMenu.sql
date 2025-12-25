-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图书管理', '1', '1', '/business/book', 'C', '0', 'business:book:view', '#', 'admin', sysdate(), '', null, '图书管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图书管理查询', @parentId, '1',  '#',  'F', '0', 'business:book:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图书管理新增', @parentId, '2',  '#',  'F', '0', 'business:book:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图书管理修改', @parentId, '3',  '#',  'F', '0', 'business:book:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图书管理删除', @parentId, '4',  '#',  'F', '0', 'business:book:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('图书管理导出', @parentId, '5',  '#',  'F', '0', 'business:book:export',       '#', 'admin', sysdate(), '', null, '');
