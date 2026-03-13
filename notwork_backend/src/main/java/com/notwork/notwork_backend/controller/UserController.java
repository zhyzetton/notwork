package com.notwork.notwork_backend.controller;

import com.notwork.notwork_backend.common.auth.LoginUser;
import com.notwork.notwork_backend.common.enums.RoleCode;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<LoginVo> login(@Valid @RequestBody LoginFormDto dto) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                dto.getUsername(),
                                dto.getPassword()
                        )
                );
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 取出第一个权限作为角色
        String role = loginUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(RoleCode.USER.getMessage());

        String token = jwtUtils.generateToken(loginUser.getUserId(), role);
        LoginVo vo = LoginVo.builder()
                .userId(loginUser.getUserId())
                .username(loginUser.getUsername())
                .token(token)
                .avatarUrl(loginUser.getAvatar())
                .build();
        return Result.success(vo);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<LoginVo> register(@Valid @RequestBody LoginFormDto dto) {
        User user = userService.register(dto);

        // 新注册用户默认角色
        String role = RoleCode.getByCode(user.getRole()).getMessage();
        String token = jwtUtils.generateToken(user.getId(), role);

        LoginVo loginVo = LoginVo.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole()).build();
        return Result.success(loginVo);
    }
}
