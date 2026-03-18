package com.notwork.notwork_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notwork.notwork_backend.entity.dto.BlogSearchDto;
import com.notwork.notwork_backend.entity.dto.BlogSubmitDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.notwork.notwork_backend.entity.vo.BlogSearchVo;

import java.util.List;

public interface IBlogService extends IService<Blog> {

    void insertBlogAndTag(BlogSubmitDto dto, Long userId);

    void updateBlogAndTag(Long id, BlogSubmitDto dto, Long userId);

    IPage<BlogSearchVo> getBlogList(BlogSearchDto dto);

    List<Long> getBlogIdsByUserId(Long userId);
}
