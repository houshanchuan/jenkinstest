DROP TABLE IF EXISTS `ct_srm_loginuser`;
CREATE TABLE `ct_srm_loginuser` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_code` varchar(64) NOT NULL COMMENT '用户工号',
  `user_name` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '状态',
  `phone` varchar(32) DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录表';

INSERT INTO srm.ct_srm_loginuser (user_code,user_name,password,status,phone) VALUES
	 ('admin','admin','$2a$10$j7vdeKZkvFFW1XvQSw25suTZbi5vxCSxM7zSN9vWbLyq6zIevbR5e','0',NULL);


 DROP TABLE IF EXISTS `cth_srm_ruleworkflow`;
 CREATE TABLE `cth_srm_ruleworkflow` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
   `role_id` varchar(64) NOT NULL COMMENT '角色id',
   `workflow_id` varchar(64) NOT NULL COMMENT '工作流id',
   `status` char(1) DEFAULT '0' COMMENT '状态',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='角色工作流';


 DROP TABLE IF EXISTS `cth_srm_workflow`;
 CREATE TABLE `cth_srm_workflow` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
   `name` varchar(64) NOT NULL COMMENT '工作流名称',
   `status` char(1) DEFAULT '0' COMMENT '状态',
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='工作流';

 DROP TABLE IF EXISTS `cth_srm_menu`;

  CREATE TABLE `cth_srm_menu` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code` varchar(50) NOT NULL COMMENT '菜单代码',
    `name` varchar(30) NOT NULL COMMENT '菜单名称',
    `parent_id` varchar(30) NOT NULL COMMENT '上级菜单',
    `menu_url` varchar(30) DEFAULT NULL COMMENT '路径',
    `icon` varchar(30)  COMMENT '图标',
    `order_num` tinyint NOT NULL DEFAULT '0'  COMMENT '排序',
    `status` char(1) DEFAULT '0' COMMENT '状态',
    `is_frame` tinyint NOT NULL DEFAULT '0' COMMENT '是否frame',
    `is_tab` tinyint NOT NULL DEFAULT '0' COMMENT '是否菜单',
    PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='菜单';
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(1, 'System', '系统管理', '0', '111', 'test', 0, '0', 0, 0);
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(2, 'sys/RoleControl', '角色管理', '1', 'RoleManage', 'test', 2, '0', 0, 1);
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(3, 'sys/MenuControl', '菜单管理', '1', 'MenuManage', 'test', 3, '0', 0, 1);
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(4, 'sys/Test', '测试His', '1', '/srm/test.html', 'test', 4, '0', 1, 1);
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(5, 'sys/UserList', '用户管理', '1', 'FirstPage', 'test', 1, '0', 0, 1);
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(6, 'Main', 'Main', '0', '111', 'test', 0, '0', 0, 0);
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(7, 'test', 'test', '0', 'test', 'test', 1, '1', 0, 0);
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(43, 'sys/Test2', '1111', '1', '1', '1', 11, '0', 1, 1);
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(44, 'sys/Test3', 'test3', '1', '1', '1', 12, '0', 1, 1);
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(45, 'sys/Test4', 'Tes4', '1', '1', '1', 13, '0', 1, 1);
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(46, 'sys/Test6', 'Test6', '1', '', '1', 14, '0', 1, 1);
INSERT INTO cth_srm_menu
(id, code, name, parent_id, menu_url, icon, order_num, status, is_frame, is_tab)
VALUES(50, 'sys/Test99', 'Test21', '1', '1', '', 15, '0', 1, 1);

  DROP TABLE IF EXISTS `cth_srm_role`;
   CREATE TABLE `cth_srm_role` (
     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
     `code` varchar(64) NOT NULL COMMENT '名称',
     `name` varchar(64) NOT NULL COMMENT '名称',
     `status` char(1) DEFAULT '0' COMMENT '状态',
     PRIMARY KEY (`id`)
   ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='角色';
insert into cth_srm_role (code,name,status) values ('管理员','管理员',1)
insert into cth_srm_role (code,name,status) values ('普通用户','普通用户',1)


 DROP TABLE IF EXISTS `cth_srm_rolemenu`;
   CREATE TABLE `cth_srm_rolemenu` (
       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
       `role_id` varchar(64) NOT NULL COMMENT '角色ID',
       `menu_id` varchar(64) NOT NULL COMMENT '菜单ID',
       PRIMARY KEY (`id`)
     ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4  COMMENT='角色';

     INSERT INTO cth_srm_rolemenu
     (id, role_id, menu_id)
     VALUES(7, '3', '1');
     INSERT INTO cth_srm_rolemenu
     (id, role_id, menu_id)
     VALUES(8, '3', '5');
     INSERT INTO cth_srm_rolemenu
     (id, role_id, menu_id)
     VALUES(9, '3', '2');
     INSERT INTO cth_srm_rolemenu
     (id, role_id, menu_id)
     VALUES(10, '3', '3');
     INSERT INTO cth_srm_rolemenu
     (id, role_id, menu_id)
     VALUES(11, '3', '4');
     INSERT INTO cth_srm_rolemenu
     (id, role_id, menu_id)
     VALUES(12, '3', '43');
     INSERT INTO cth_srm_rolemenu
     (id, role_id, menu_id)
     VALUES(13, '3', '6');
     INSERT INTO cth_srm_rolemenu
     (id, role_id, menu_id)
     VALUES(14, '2', '1');
     INSERT INTO cth_srm_rolemenu
     (id, role_id, menu_id)
     VALUES(15, '2', '5');
     INSERT INTO cth_srm_rolemenu
     (id, role_id, menu_id)
     VALUES(16, '2', '2');

     ---------------小程序app------------------

     DROP TABLE IF EXISTS `app_srm_loginuser`;
     CREATE TABLE `app_srm_loginuser` (
            `id` char(36) CHARACTER SET utf8mb4  NOT NULL COMMENT '主键',
            `wx_open_id` varchar(64) NOT NULL COMMENT '用户工号',
            `wx_name` varchar(64) NOT NULL COMMENT '用户名',
            `wx_phone` varchar(64) NOT NULL COMMENT '密码',
            `status` char(1) DEFAULT '0' COMMENT '状态',
             `create_date` date NULL DEFAULT NULL COMMENT '创建日期',
            PRIMARY KEY (`id`)
          ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录表';

     DROP TABLE IF EXISTS `app_srm_chat`;
          CREATE TABLE `app_srm_chat` (
                 `id` char(36) CHARACTER SET utf8mb4  NOT NULL COMMENT '主键',
                 `from_user_id` varchar(64) NOT NULL COMMENT '发送人',
                 `to_user_id` varchar(64) NOT NULL COMMENT '接收人',
                 PRIMARY KEY (`id`)
               ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天主表';
     DROP TABLE IF EXISTS `app_srm_chat_user`;
               CREATE TABLE `app_srm_chat_user` (
                      `id` char(36) CHARACTER SET utf8mb4  NOT NULL COMMENT '主键',
                      `from_user_id` varchar(64) NOT NULL COMMENT '发送人',
                      `to_user_id` varchar(64) NOT NULL COMMENT '接收人',
                      PRIMARY KEY (`id`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天主表';
     DROP TABLE IF EXISTS `app_srm_chat_detail`;
               CREATE TABLE `app_srm_chat_detail` (
                      `id` char(36) CHARACTER SET utf8mb4  NOT NULL COMMENT '主键',
                      `from_user_id` varchar(64) NOT NULL COMMENT '发送人',
                      `to_user_id` varchar(64) NOT NULL COMMENT '接收人',
                      `is_show` char(1) DEFAULT '0' COMMENT '是否显示',
                      `is_delete` char(1) DEFAULT '0' COMMENT '是否删除',
                      `is_last` char(1) DEFAULT '0' COMMENT '是否最后一条',
                      `is_read` char(1) DEFAULT '0' COMMENT '是否已读',
                      `create_date` date NULL DEFAULT NULL COMMENT '创建日期',
                       content TEXT,
                      PRIMARY KEY (`id`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天信息';