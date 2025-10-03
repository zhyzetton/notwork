package com.notwork.notwork_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 博客标签表
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-03
 */
@Getter
@Setter
@TableName("blog_tag")
public class BlogTag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标签code
     */
    private String tagCode;

    /**
     * 标签名称
     */
    private String tagName;
}
