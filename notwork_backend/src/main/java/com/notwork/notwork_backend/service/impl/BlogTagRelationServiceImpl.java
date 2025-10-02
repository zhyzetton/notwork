package com.notwork.notwork_backend.service.impl;

import com.notwork.notwork_backend.entity.pojo.BlogTagRelation;
import com.notwork.notwork_backend.mapper.BlogTagRelationMapper;
import com.notwork.notwork_backend.service.IBlogTagRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 博客-标签关联表 服务实现类
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Service
public class BlogTagRelationServiceImpl extends ServiceImpl<BlogTagRelationMapper, BlogTagRelation> implements IBlogTagRelationService {

}
