package com.notwork.notwork_backend.service;

import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 博客表 服务类
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
public interface IBlogService extends IService<Blog> {

    void insertBlog(BlogSubmitDto dto);

}
