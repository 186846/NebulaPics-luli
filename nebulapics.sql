/*
 Navicat Premium Dump SQL

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 50736 (5.7.36-log)
 Source Host           : localhost:3306
 Source Schema         : nebulapics

 Target Server Type    : MySQL
 Target Server Version : 50736 (5.7.36-log)
 File Encoding         : 65001

 Date: 11/05/2025 14:47:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for picture
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片 url',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片名称',
  `introduction` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '简介',
  `category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类',
  `tags` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签（JSON 数组）',
  `picSize` bigint(20) NULL DEFAULT NULL COMMENT '图片体积',
  `picWidth` int(11) NULL DEFAULT NULL COMMENT '图片宽度',
  `picHeight` int(11) NULL DEFAULT NULL COMMENT '图片高度',
  `picScale` double NULL DEFAULT NULL COMMENT '图片宽高比例',
  `picFormat` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片格式',
  `userId` bigint(20) NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `editTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `reviewStatus` int(11) NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核; 1-通过; 2-拒绝',
  `reviewMessage` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审核信息',
  `reviewerId` bigint(20) NULL DEFAULT NULL COMMENT '审核人 ID',
  `reviewTime` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `thumbnailUrl` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '缩略图 url',
  `spaceId` bigint(20) NULL DEFAULT NULL COMMENT '空间 id（为空表示公共空间）',
  `picColor` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '图片主色调',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_name`(`name`) USING BTREE,
  INDEX `idx_introduction`(`introduction`) USING BTREE,
  INDEX `idx_category`(`category`) USING BTREE,
  INDEX `idx_tags`(`tags`) USING BTREE,
  INDEX `idx_userId`(`userId`) USING BTREE,
  INDEX `idx_reviewStatus`(`reviewStatus`) USING BTREE,
  INDEX `idx_spaceId`(`spaceId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1913872188667002883 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '图片' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '帖子标题',
  `content` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '帖子内容',
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '帖子分类',
  `userId` bigint(20) NOT NULL COMMENT '发帖用户 ID',
  `viewCount` int(11) NOT NULL DEFAULT 0 COMMENT '浏览次数',
  `replyCount` int(11) NOT NULL DEFAULT 0 COMMENT '回复次数',
  `heartCount` int(11) NOT NULL DEFAULT 0,
  `editTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE,
  INDEX `idx_title`(`title`) USING BTREE,
  INDEX `idx_category`(`category`) USING BTREE,
  CONSTRAINT `post_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '帖子' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reply
-- ----------------------------
DROP TABLE IF EXISTS `reply`;
CREATE TABLE `reply`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `postId` bigint(20) NOT NULL COMMENT '所属帖子 ID',
  `userId` bigint(20) NOT NULL COMMENT '回复用户 ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '回复内容',
  `likeCount` bigint(20) NOT NULL DEFAULT 0 COMMENT '点赞个数',
  `dislikeCount` bigint(20) NOT NULL DEFAULT 0 COMMENT '下踩个数\r\n',
  `editTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isReview` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否被查看\r\n',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `userId`(`userId`) USING BTREE,
  INDEX `idx_postId`(`postId`) USING BTREE,
  CONSTRAINT `reply_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `post` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `reply_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '回复' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for reply_comment
-- ----------------------------
DROP TABLE IF EXISTS `reply_comment`;
CREATE TABLE `reply_comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `replyId` bigint(20) NOT NULL COMMENT '所属评论 ID',
  `userId` bigint(20) NOT NULL COMMENT '发送用户 ID',
  `receiveUserId` bigint(20) NOT NULL COMMENT '接收用户 ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '回复内容',
  `likeCount` bigint(20) NOT NULL DEFAULT 0 COMMENT '点赞个数',
  `dislikeCount` bigint(20) NOT NULL DEFAULT 0 COMMENT '下踩个数',
  `editTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isReview` tinyint(4) NOT NULL DEFAULT 0,
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `sendUserId`(`userId`) USING BTREE,
  INDEX `receiveUserId`(`receiveUserId`) USING BTREE,
  INDEX `idx_replyId`(`replyId`) USING BTREE,
  CONSTRAINT `reply_comment_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `reply_comment_ibfk_3` FOREIGN KEY (`receiveUserId`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 54 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评论回复' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for space
-- ----------------------------
DROP TABLE IF EXISTS `space`;
CREATE TABLE `space`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spaceName` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '空间名称',
  `spaceLevel` int(11) NULL DEFAULT 0 COMMENT '空间级别：0-普通版 1-专业版 2-旗舰版',
  `maxSize` bigint(20) NULL DEFAULT 0 COMMENT '空间图片的最大总大小',
  `maxCount` bigint(20) NULL DEFAULT 0 COMMENT '空间图片的最大数量',
  `totalSize` bigint(20) NULL DEFAULT 0 COMMENT '当前空间下图片的总大小',
  `totalCount` bigint(20) NULL DEFAULT 0 COMMENT '当前空间下的图片数量',
  `userId` bigint(20) NOT NULL COMMENT '创建用户 id',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `editTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `spaceType` int(11) NOT NULL DEFAULT 0 COMMENT '空间类型：0-私有 1-团队',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_userId`(`userId`) USING BTREE,
  INDEX `idx_spaceName`(`spaceName`) USING BTREE,
  INDEX `idx_spaceLevel`(`spaceLevel`) USING BTREE,
  INDEX `idx_spaceType`(`spaceType`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1887666548026499083 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '空间' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for space_user
-- ----------------------------
DROP TABLE IF EXISTS `space_user`;
CREATE TABLE `space_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `spaceId` bigint(20) NOT NULL COMMENT '空间 id',
  `userId` bigint(20) NOT NULL COMMENT '用户 id',
  `spaceRole` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'viewer' COMMENT '空间角色：viewer/editor/admin',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_spaceId_userId`(`spaceId`, `userId`) USING BTREE,
  INDEX `idx_spaceId`(`spaceId`) USING BTREE,
  INDEX `idx_userId`(`userId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '空间用户关联' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL,
  `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
  `userProfile` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户简介',
  `userRole` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin',
  `editTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '编辑时间',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`, `userAccount`) USING BTREE,
  UNIQUE INDEX `uk_userAccount`(`userAccount`) USING BTREE,
  INDEX `idx_userName`(`userName`) USING BTREE,
  INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_action
-- ----------------------------
DROP TABLE IF EXISTS `user_action`;
CREATE TABLE `user_action`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '操作记录ID',
  `userId` bigint(20) NOT NULL COMMENT '用户ID',
  `postId` bigint(20) NOT NULL DEFAULT 0 COMMENT '论坛id',
  `replyId` bigint(20) NOT NULL COMMENT '回复ID',
  `replyCommentId` bigint(20) NOT NULL COMMENT '回复评论id',
  `receiveUserId` bigint(20) NOT NULL,
  `action` tinyint(2) NOT NULL DEFAULT 2 COMMENT '操作类型',
  `postAction` tinyint(2) NOT NULL DEFAULT 0,
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除',
  `isReview` tinyint(4) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`userId`, `replyId`, `replyCommentId`, `postId`) USING BTREE,
  INDEX `idx_user_operation`(`userId`, `action`) USING BTREE,
  INDEX `fk_reply`(`replyId`) USING BTREE,
  CONSTRAINT `fk_user` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1897875743707209731 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户行为记录表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
