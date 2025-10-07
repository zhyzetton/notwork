package com.notwork.notwork_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notwork.notwork_backend.entity.dto.BlogSearchDto;
import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.notwork.notwork_backend.entity.vo.BlogSearchVo;

import java.io.IOException;

/**
 * <p>
 * 博客表 服务类
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
public interface IBlogService extends IService<Blog> {

    void insertBlogAndTag(BlogSubmitDto dto) throws IOException;

    IPage<BlogSearchVo> getBlogList(BlogSearchDto dto);

    String chatWithRag(String userId, String query);

}
