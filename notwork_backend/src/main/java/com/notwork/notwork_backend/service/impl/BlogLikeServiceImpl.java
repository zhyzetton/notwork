package com.notwork.notwork_backend.service.impl;

import com.notwork.notwork_backend.entity.pojo.BlogLike;
import com.notwork.notwork_backend.mapper.BlogLikeMapper;
import com.notwork.notwork_backend.service.IBlogLikeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客点赞表 服务实现类
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Service
public class BlogLikeServiceImpl extends ServiceImpl<BlogLikeMapper, BlogLike> implements IBlogLikeService {

}
