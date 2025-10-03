package com.notwork.notwork_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notwork.notwork_backend.entity.dto.LoginFormDto;
import com.notwork.notwork_backend.entity.pojo.User;
import com.notwork.notwork_backend.service.IUserService;
import com.notwork.notwork_backend.utils.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zhyzetton
 * @since 2025-10-01
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    @RequestMapping("/login")
    public Result login(@RequestBody LoginFormDto dto) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (user == null) return Result.error("用户名不存在");
        if (!Objects.equals(user.getPasswordHash(), dto.getPassword())) return Result.error("密码错误");
        user.setPasswordHash(null);
        return Result.success(user);
    }
}
