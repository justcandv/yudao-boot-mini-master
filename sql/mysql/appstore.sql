-- ----------------------------
-- 安卓 TV 应用商店模块 SQL 脚本
-- ----------------------------

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for appstore_category（应用分类）
-- ----------------------------
DROP TABLE IF EXISTS `appstore_category`;
CREATE TABLE `appstore_category` (
    `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '分类编号',
    `name`        varchar(64)  NOT NULL                COMMENT '分类名称',
    `icon_url`    varchar(512)          DEFAULT ''     COMMENT '分类图标 URL',
    `sort`        int          NOT NULL DEFAULT 0      COMMENT '排序权重',
    `status`      tinyint      NOT NULL DEFAULT 0      COMMENT '状态（0 开启 1 关闭）',
    `creator`     varchar(64)           DEFAULT ''     COMMENT '创建者',
    `create_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`     varchar(64)           DEFAULT ''     COMMENT '更新者',
    `update_time` datetime     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     bit(1)       NOT NULL DEFAULT b'0'   COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '应用分类表';

-- ----------------------------
-- Table structure for appstore_app（应用信息）
-- ----------------------------
DROP TABLE IF EXISTS `appstore_app`;
CREATE TABLE `appstore_app` (
    `id`                bigint        NOT NULL AUTO_INCREMENT COMMENT '应用编号',
    `category_id`       bigint        NOT NULL                COMMENT '所属分类编号',
    `name`              varchar(128)  NOT NULL                COMMENT '应用名称',
    `package_name`      varchar(256)  NOT NULL                COMMENT '安卓包名',
    `icon_url`          varchar(512)           DEFAULT ''     COMMENT '应用图标 URL',
    `description`       text                                  COMMENT '应用描述',
    `screenshots`       varchar(2048)          DEFAULT ''     COMMENT '截图 URL（JSON 数组）',
    `developer`         varchar(128)           DEFAULT ''     COMMENT '开发者名称',
    `status`            tinyint       NOT NULL DEFAULT 0      COMMENT '状态（0 草稿 1 已上架 2 已下架）',
    `download_count`    bigint        NOT NULL DEFAULT 0      COMMENT '累计下载次数',
    `latest_version_id` bigint                 DEFAULT NULL   COMMENT '最新版本编号',
    `creator`           varchar(64)            DEFAULT ''     COMMENT '创建者',
    `create_time`       datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`           varchar(64)            DEFAULT ''     COMMENT '更新者',
    `update_time`       datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`           bit(1)        NOT NULL DEFAULT b'0'   COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_package_name` (`package_name`, `deleted`) USING BTREE,
    INDEX `idx_category_id` (`category_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '应用信息表';

-- ----------------------------
-- Table structure for appstore_app_version（应用版本）
-- ----------------------------
DROP TABLE IF EXISTS `appstore_app_version`;
CREATE TABLE `appstore_app_version` (
    `id`                  bigint        NOT NULL AUTO_INCREMENT COMMENT '版本编号',
    `app_id`              bigint        NOT NULL                COMMENT '关联应用编号',
    `version_name`        varchar(32)   NOT NULL                COMMENT '版本号（如 1.2.0）',
    `version_code`        int           NOT NULL                COMMENT '安卓 versionCode',
    `apk_url`             varchar(512)  NOT NULL                COMMENT 'APK 下载地址',
    `apk_size`            bigint                 DEFAULT 0      COMMENT 'APK 文件大小（字节）',
    `update_description`  text                                  COMMENT '更新说明',
    `status`              tinyint       NOT NULL DEFAULT 0      COMMENT '状态（0 草稿 1 已发布）',
    `creator`             varchar(64)            DEFAULT ''     COMMENT '创建者',
    `create_time`         datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater`             varchar(64)            DEFAULT ''     COMMENT '更新者',
    `update_time`         datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`             bit(1)        NOT NULL DEFAULT b'0'   COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_app_id` (`app_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '应用版本表';

-- ----------------------------
-- 菜单与权限初始化数据（插入 system_menu）
-- 注意：id 使用较大数值避免与已有数据冲突，实际部署时按需调整
-- ----------------------------

-- 一级目录：应用商店
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (3000, '应用商店', '', 1, 50, 0, '/appstore', 'ep:shop', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 二级菜单：应用分类
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (3010, '应用分类', '', 2, 1, 3000, 'category', 'ep:menu', 'appstore/category/index', 'AppstoreCategory', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 按钮权限：应用分类
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (3011, '分类查询', 'appstore:category:query',  3, 1, 3010, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
       (3012, '分类新增', 'appstore:category:create', 3, 2, 3010, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
       (3013, '分类修改', 'appstore:category:update', 3, 3, 3010, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
       (3014, '分类删除', 'appstore:category:delete', 3, 4, 3010, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 二级菜单：应用管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (3020, '应用管理', '', 2, 2, 3000, 'app', 'ep:cellphone', 'appstore/app/index', 'AppstoreApp', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 按钮权限：应用管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (3021, '应用查询', 'appstore:app:query',  3, 1, 3020, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
       (3022, '应用新增', 'appstore:app:create', 3, 2, 3020, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
       (3023, '应用修改', 'appstore:app:update', 3, 3, 3020, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
       (3024, '应用删除', 'appstore:app:delete', 3, 4, 3020, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 二级菜单：版本管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (3030, '版本管理', '', 2, 3, 3000, 'app-version', 'ep:document', 'appstore/appVersion/index', 'AppstoreAppVersion', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- 按钮权限：版本管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (3031, '版本查询', 'appstore:app-version:query',  3, 1, 3030, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
       (3032, '版本新增', 'appstore:app-version:create', 3, 2, 3030, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
       (3033, '版本修改', 'appstore:app-version:update', 3, 3, 3030, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0'),
       (3034, '版本删除', 'appstore:app-version:delete', 3, 4, 3030, '', '', '', '', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

SET FOREIGN_KEY_CHECKS = 1;
