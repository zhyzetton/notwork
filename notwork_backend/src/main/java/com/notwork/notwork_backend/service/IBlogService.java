package com.notwork.notwork_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notwork.notwork_backend.entity.dto.BlogSearchDto;
import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.notwork.notwork_backend.entity.vo.BlogSearchVo;

/**
 * <p>
 * 博客表 服务类
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
public interface IBlogService extends IService<Blog> {

    void insertBlogAndTag(BlogSubmitDto dto);

    IPage<BlogSearchVo> getBlogList(BlogSearchDto dto);

}
