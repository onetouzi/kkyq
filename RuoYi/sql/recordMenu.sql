-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('读者书籍借阅关系', '1', '1', '/work/record', 'C', '0', 'work:record:view', '#', 'admin', sysdate(), '', null, '读者书籍借阅关系菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('读者书籍借阅关系查询', @parentId, '1',  '#',  'F', '0', 'work:record:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('读者书籍借阅关系新增', @parentId, '2',  '#',  'F', '0', 'work:record:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('读者书籍借阅关系修改', @parentId, '3',  '#',  'F', '0', 'work:record:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('读者书籍借阅关系删除', @parentId, '4',  '#',  'F', '0', 'work:record:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('读者书籍借阅关系导出', @parentId, '5',  '#',  'F', '0', 'work:record:export',       '#', 'admin', sysdate(), '', null, '');
