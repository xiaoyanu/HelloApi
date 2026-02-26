SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for api_apps
-- ----------------------------
DROP TABLE IF EXISTS `api_apps`;
CREATE TABLE `api_apps`
(
    `id`            int(10)      NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
    `title`         varchar(255) NOT NULL COMMENT '接口名',
    `smallTitle`    varchar(255) NOT NULL COMMENT '接口描述',
    `status`        int(2)       NOT NULL COMMENT '状态，正常0，异常1，维护2',
    `type`          int(2)       NOT NULL COMMENT '接口类型，免费0，付费1',
    `url`           varchar(255) NOT NULL COMMENT '请求地址',
    `sendType`      int(2)       NOT NULL COMMENT '请求类型，0GET、1POST',
    `returnType`    varchar(255) NOT NULL COMMENT '返回类型',
    `returnContent` longtext     NOT NULL COMMENT '返回内容',
    `created`       bigint(13)   NOT NULL COMMENT '创建时间，时间戳',
    `user_id`       int(10)      NOT NULL COMMENT '这个接口的用户',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for api_keys
-- ----------------------------
DROP TABLE IF EXISTS `api_keys`;
CREATE TABLE `api_keys`
(
    `api_id`  int(10)      NOT NULL COMMENT '对应的API的ID',
    `key`     varchar(255) NOT NULL COMMENT 'api密钥',
    `created` bigint(13)   NOT NULL COMMENT '创建时间',
    `type`    int(2)       NOT NULL COMMENT '计费类型：0时间，1次数',
    `started` bigint(13)   NOT NULL COMMENT '开始时间',
    `expired` bigint(13)   NOT NULL COMMENT '过期时间',
    `count`   bigint(13)   NOT NULL COMMENT '可用次数',
    PRIMARY KEY (`key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for api_params
-- ----------------------------
DROP TABLE IF EXISTS `api_params`;
CREATE TABLE `api_params`
(
    `api_id`   int(10)      NOT NULL COMMENT '对应的Api唯一ID',
    `name`     varchar(255) NOT NULL COMMENT '参数名',
    `required` int(2)       NOT NULL COMMENT '是否必填，0否，1是',
    `type`     varchar(255) NOT NULL COMMENT '参数类型',
    `msg`      varchar(255) NOT NULL COMMENT '描述'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for api_request_logs
-- ----------------------------
DROP TABLE IF EXISTS `api_request_logs`;
CREATE TABLE `api_request_logs`
(
    `api_id` int(10) NOT NULL COMMENT '对应的API的ID',
    `ip`     varchar(255) DEFAULT NULL COMMENT '请求IP',
    `time`   datetime     DEFAULT CURRENT_TIMESTAMP COMMENT '日期时间',
    `header` json         DEFAULT NULL COMMENT '请求头',
    `body`   json         DEFAULT NULL COMMENT '请求参数',
    INDEX `idx_api_id` (`api_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for api_views
-- ----------------------------
DROP TABLE IF EXISTS `api_views`;
CREATE TABLE `api_views`
(
    `api_id` int(10)    NOT NULL,
    `count`  bigint(13) NOT NULL,
    PRIMARY KEY (`api_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for user_keys
-- ----------------------------
DROP TABLE IF EXISTS `user_keys`;
CREATE TABLE `user_keys`
(
    `user_id` int(10)      NOT NULL COMMENT '对应的用户id',
    `key`     varchar(255) NOT NULL COMMENT '用户接口key',
    `created` bigint(13)   NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`       int(10)      NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID',
    `name`     varchar(255) NOT NULL COMMENT '用户名',
    `password` varchar(255) NOT NULL COMMENT '密码',
    `nick`     varchar(255) NOT NULL COMMENT '用户昵称',
    `mode`     int(2)       NOT NULL COMMENT '用户类型0为普通，1为管理员',
    `mail`     varchar(255) DEFAULT NULL COMMENT '邮箱',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;