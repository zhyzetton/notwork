package com.notwork.notwork_backend.entity.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱，用于登录
     */
    private String email;

    /**
     * 加密后的密码
     */
    private String passwordHash;

    /**
     * 用户头像地址（MinIO URL）
     */
    private String avatarUrl;

    /**
     * 个人简介
     */
    private String bio;

    /**
     * 角色：0=普通用户 1=管理员
     */
    private Byte role;

    /**
     * 状态：0=禁用 1=正常
     */
    private Byte status;

    /**
     * 注册时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
