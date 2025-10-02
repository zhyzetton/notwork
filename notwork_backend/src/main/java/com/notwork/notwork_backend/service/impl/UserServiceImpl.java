package com.notwork.notwork_backend.service.impl;

import com.notwork.notwork_backend.entity.pojo.User;
import com.notwork.notwork_backend.mapper.UserMapper;
import com.notwork.notwork_backend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
