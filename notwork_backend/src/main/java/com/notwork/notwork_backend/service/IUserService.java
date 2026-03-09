package com.notwork.notwork_backend.service;

import com.notwork.notwork_backend.entity.dto.LoginFormDto;
import com.notwork.notwork_backend.entity.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IUserService extends IService<User> {

    User login(LoginFormDto dto);

    User register(LoginFormDto dto);
}
