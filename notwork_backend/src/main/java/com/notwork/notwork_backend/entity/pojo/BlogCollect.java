package com.notwork.notwork_backend.entity.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 博客收藏表
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Getter
@Setter
@TableName("blog_collect")
public class BlogCollect implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 收藏ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 博客ID
     */
    private Long blogId;

    /**
     * 收藏用户ID
     */
    private Long userId;

    /**
     * 收藏时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
