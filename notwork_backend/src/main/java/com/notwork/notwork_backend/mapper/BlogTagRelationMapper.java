package com.notwork.notwork_backend.mapper;

import com.notwork.notwork_backend.entity.pojo.BlogTagRelation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 博客-标签关联表 Mapper 接口
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Mapper
public interface BlogTagRelationMapper extends BaseMapper<BlogTagRelation> {

}
