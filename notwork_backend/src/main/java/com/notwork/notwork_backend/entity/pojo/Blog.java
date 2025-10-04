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
 * 博客表
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Getter
@Setter
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 博客ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 作者ID（对应user.id）
     */
    private Long userId;

    /**
     * 博客标题
     */
    private String title;

    /**
     * Markdown 原文
     */
    private String contentMarkdown;

    /**
     * 渲染后的 HTML
     */
    private String contentHtml;

    /**
     * 封面图URL（MinIO）
     */
    private String coverUrl;

    /**
     * 状态：0=草稿 1=已发布
     */
    private Byte status;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞次数
     */
    private Integer likeCount;

    /**
     * 收藏次数
     */
    private Integer collectCount;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
