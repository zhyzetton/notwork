package com.notwork.notwork_backend.service.impl;

import com.notwork.notwork_backend.entity.pojo.BlogTag;
import com.notwork.notwork_backend.mapper.BlogTagMapper;
import com.notwork.notwork_backend.service.IBlogTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客标签表 服务实现类
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements IBlogTagService {

}
