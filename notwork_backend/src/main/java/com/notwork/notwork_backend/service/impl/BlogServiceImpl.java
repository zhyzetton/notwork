package com.notwork.notwork_backend.service.impl;

import com.notwork.notwork_backend.entity.pojo.Blog;
import com.notwork.notwork_backend.mapper.BlogMapper;
import com.notwork.notwork_backend.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

}
