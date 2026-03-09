package com.notwork.notwork_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notwork.notwork_backend.common.enums.ResultCode;
import com.notwork.notwork_backend.common.exception.BusinessException;
import com.notwork.notwork_backend.entity.pojo.BlogComment;
import com.notwork.notwork_backend.mapper.BlogCommentMapper;
import com.notwork.notwork_backend.service.IBlogCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements IBlogCommentService {

    @Override
    public void addComment(Long blogId, Long userId, String content, Long parentId) {
        BlogComment comment = new BlogComment();
        comment.setBlogId(blogId);
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setParentId(parentId);
        comment.setLikeCount(0);
        save(comment);
    }

    @Override
    public IPage<BlogComment> getCommentsByBlogId(Long blogId, int pageNum, int pageSize) {
        Page<BlogComment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BlogComment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BlogComment::getBlogId, blogId)
                .orderByDesc(BlogComment::getCreateTime);
        return page(page, wrapper);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        BlogComment comment = getById(commentId);
        if (comment == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "评论不存在");
        }
        if (!comment.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "无权删除此评论");
        }
        removeById(commentId);
    }
}
