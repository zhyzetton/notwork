package com.notwork.notwork_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notwork.notwork_backend.entity.pojo.BlogComment;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IBlogCommentService extends IService<BlogComment> {

    void addComment(Long blogId, Long userId, String content, Long parentId);

    IPage<BlogComment> getCommentsByBlogId(Long blogId, int pageNum, int pageSize);

    void deleteComment(Long commentId, Long userId);
}
