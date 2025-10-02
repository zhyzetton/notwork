package com.notwork.notwork_backend.service.impl;

import com.notwork.notwork_backend.entity.pojo.BlogComment;
import com.notwork.notwork_backend.mapper.BlogCommentMapper;
import com.notwork.notwork_backend.service.IBlogCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客评论表 服务实现类
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements IBlogCommentService {

}
