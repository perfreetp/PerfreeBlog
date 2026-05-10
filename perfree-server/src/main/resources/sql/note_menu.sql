-- 笔记管理模块菜单SQL
-- 执行此SQL以添加笔记管理菜单

-- 笔记管理主菜单 (父菜单: 内容管理 af4cb0b4a4c1807faf4cb0b4a4c1807f)
INSERT INTO `p_menu` VALUES ('note_menu_001', 'af4cb0b4a4c1807faf4cb0b4a4c1807f', '笔记管理', '/admin/note', 'fa-solid fa-note-sticky', 3, 0, 0, NOW(), NOW(), 1, NULL, NULL, '/view/NoteView', 'note', 'note', '', 1, 1, 1, 1);

-- 笔记管理权限菜单
INSERT INTO `p_menu` VALUES ('note_menu_002', 'note_menu_001', '创建笔记', '', '', 0, 0, 0, NOW(), NULL, 2, NULL, NULL, '', '', '', 'admin:note:create', 1, 1, NULL, 1);

INSERT INTO `p_menu` VALUES ('note_menu_003', 'note_menu_001', '修改笔记', '', '', 0, 0, 0, NOW(), NULL, 2, NULL, NULL, '', '', '', 'admin:note:update', 1, 1, NULL, 1);

INSERT INTO `p_menu` VALUES ('note_menu_004', 'note_menu_001', '删除笔记', '', '', 0, 0, 0, NOW(), NULL, 2, NULL, NULL, '', '', '', 'admin:note:delete', 1, 1, NULL, 1);

-- 为超级管理员角色添加笔记管理权限
-- 假设超级管理员角色ID为1，你可能需要根据实际情况调整
INSERT INTO `p_role_menu` (id, roleId, menuId) VALUES
(UUID(), 1, 'note_menu_001'),
(UUID(), 1, 'note_menu_002'),
(UUID(), 1, 'note_menu_003'),
(UUID(), 1, 'note_menu_004');
