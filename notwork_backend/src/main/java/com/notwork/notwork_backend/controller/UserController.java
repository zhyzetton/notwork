package com.notwork.notwork_backend.controller;

import com.notwork.notwork_backend.common.result.Result;
import com.notwork.notwork_backend.entity.dto.LoginFormDto;
import com.notwork.notwork_backend.entity.pojo.User;
import com.notwork.notwork_backend.entity.vo.LoginVo;
import com.notwork.notwork_backend.common.utils.JwtUtils;
import com.notwork.notwork_backend.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Tag(name = "用户管理接口")
public class UserController {

    private final IUserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginVo> login(@Valid @RequestBody LoginFormDto dto) {
        User user = userService.login(dto);
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setUserId(user.getId());
        loginVo.setUsername(user.getUsername());
        loginVo.setAvatarUrl(user.getAvatarUrl());
        loginVo.setRole(user.getRole());
        return Result.success(loginVo);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<LoginVo> register(@Valid @RequestBody LoginFormDto dto) {
        User user = userService.register(dto);
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        LoginVo loginVo = new LoginVo();
        loginVo.setToken(token);
        loginVo.setUserId(user.getId());
        loginVo.setUsername(user.getUsername());
        loginVo.setAvatarUrl(user.getAvatarUrl());
        loginVo.setRole(user.getRole());
        return Result.success(loginVo);
    }
}
