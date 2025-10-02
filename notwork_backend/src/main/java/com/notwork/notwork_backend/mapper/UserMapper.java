package com.notwork.notwork_backend.mapper;

import com.notwork.notwork_backend.entity.pojo.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
