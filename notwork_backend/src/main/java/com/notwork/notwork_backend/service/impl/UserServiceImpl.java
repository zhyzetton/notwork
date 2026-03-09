package com.notwork.notwork_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notwork.notwork_backend.common.enums.ResultCode;
import com.notwork.notwork_backend.common.exception.BusinessException;
import com.notwork.notwork_backend.entity.dto.LoginFormDto;
import com.notwork.notwork_backend.entity.pojo.User;
import com.notwork.notwork_backend.mapper.UserMapper;
import com.notwork.notwork_backend.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public User login(LoginFormDto dto) {
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BusinessException(ResultCode.USERNAME_NOT_FOUND);
        }
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    public User register(LoginFormDto dto) {
        long count = count(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (count > 0) {
            throw new BusinessException(ResultCode.USERNAME_ALREADY_EXISTS);
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole((byte) 0);
        user.setStatus((byte) 1);
        save(user);
        return user;
    }
}
