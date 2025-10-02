package com.notwork.notwork_backend.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 博客-标签关联表
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Getter
@Setter
@TableName("blog_tag_relation")
public class BlogTagRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 博客ID
     */
    private Long blogId;

    /**
     * 标签ID
     */
    private Long tagId;
}
