package com.notwork.notwork_backend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.notwork.notwork_backend.entity.dto.BlogSearchDto;
import com.notwork.notwork_backend.entity.pojo.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.notwork.notwork_backend.entity.vo.BlogSearchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 博客表 Mapper 接口
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    IPage<BlogSearchVo> searchBlog(Page<?> page, @Param("dto") BlogSearchDto dto);

}
