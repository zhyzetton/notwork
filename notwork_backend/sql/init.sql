-- ============================================================
-- Notwork 数据库初始化脚本
-- ============================================================

CREATE DATABASE IF NOT EXISTS `notwork` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `notwork`;

-- -----------------------------------------------------------
-- 1. 用户表
-- -----------------------------------------------------------
CREATE TABLE `user` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`      VARCHAR(50)  NOT NULL COMMENT '用户名',
    `email`         VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `password_hash` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
    `avatar_url`    VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
    `bio`           VARCHAR(500) DEFAULT NULL COMMENT '个人简介',
    `role`          TINYINT      NOT NULL DEFAULT 0 COMMENT '角色：0=普通用户 1=管理员',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：0=禁用 1=正常',
    `create_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    `update_time`   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- -----------------------------------------------------------
-- 2. 博客表
-- -----------------------------------------------------------
CREATE TABLE `blog` (
    `id`               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '博客ID',
    `user_id`          BIGINT       NOT NULL COMMENT '作者ID',
    `title`            VARCHAR(255) NOT NULL COMMENT '博客标题',
    `summary`          VARCHAR(500) DEFAULT NULL COMMENT '博客摘要',
    `content_markdown` LONGTEXT     DEFAULT NULL COMMENT 'Markdown原文',
    `content_html`     LONGTEXT     DEFAULT NULL COMMENT '渲染后的HTML',
    `cover_url`        VARCHAR(512) DEFAULT NULL COMMENT '封面图URL',
    `status`           TINYINT      NOT NULL DEFAULT 0 COMMENT '状态：0=草稿 1=已发布',
    `view_count`       INT          NOT NULL DEFAULT 0 COMMENT '浏览次数',
    `like_count`       INT          NOT NULL DEFAULT 0 COMMENT '点赞次数',
    `collect_count`    INT          NOT NULL DEFAULT 0 COMMENT '收藏次数',
    `create_time`      DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客表';

-- -----------------------------------------------------------
-- 3. 博客标签表
-- -----------------------------------------------------------
CREATE TABLE `blog_tag` (
    `id`       BIGINT      NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `tag_code` VARCHAR(50) NOT NULL COMMENT '标签code',
    `tag_name` VARCHAR(50) NOT NULL COMMENT '标签名称',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tag_code` (`tag_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客标签表';

-- -----------------------------------------------------------
-- 4. 博客-标签关联表
-- -----------------------------------------------------------
CREATE TABLE `blog_tag_relation` (
    `blog_id` BIGINT NOT NULL COMMENT '博客ID',
    `tag_id`  BIGINT NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`blog_id`, `tag_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客-标签关联表';

-- -----------------------------------------------------------
-- 5. 博客点赞表
-- -----------------------------------------------------------
CREATE TABLE `blog_like` (
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
    `blog_id`     BIGINT   NOT NULL COMMENT '博客ID',
    `user_id`     BIGINT   NOT NULL COMMENT '点赞用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_blog_user` (`blog_id`, `user_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客点赞表';

-- -----------------------------------------------------------
-- 6. 博客收藏表
-- -----------------------------------------------------------
CREATE TABLE `blog_collect` (
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `blog_id`     BIGINT   NOT NULL COMMENT '博客ID',
    `user_id`     BIGINT   NOT NULL COMMENT '收藏用户ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_blog_user` (`blog_id`, `user_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客收藏表';

-- -----------------------------------------------------------
-- 7. 博客评论表
-- -----------------------------------------------------------
CREATE TABLE `blog_comment` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `blog_id`     BIGINT       NOT NULL COMMENT '博客ID',
    `user_id`     BIGINT       NOT NULL COMMENT '评论用户ID',
    `parent_id`   BIGINT       DEFAULT NULL COMMENT '父评论ID，NULL=一级评论',
    `content`     TEXT         NOT NULL COMMENT '评论内容',
    `like_count`  INT          NOT NULL DEFAULT 0 COMMENT '点赞数',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    `update_time` DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_blog_id` (`blog_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客评论表';

-- ============================================================
-- 初始数据
-- ============================================================

-- 默认标签
INSERT INTO `blog_tag` (`tag_code`, `tag_name`) VALUES
    ('backend',    '后端'),
    ('frontend',   '前端'),
    ('devops',     '运维'),
    ('ai',         '人工智能'),
    ('database',   '数据库');

-- 默认管理员（密码: admin123，BCrypt 加密）
INSERT INTO `user` (`username`, `password_hash`, `role`, `status`) VALUES
    ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', 1, 1);
