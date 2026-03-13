package com.notwork.notwork_backend.common.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.notwork.notwork_backend.common.enums.RoleCode;
import com.notwork.notwork_backend.entity.pojo.User;
import com.notwork.notwork_backend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserDetailsService implements UserDetailsService {

    private final IUserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));

        if(user == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        return new LoginUser(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getAvatarUrl(),
                List.of(new SimpleGrantedAuthority(RoleCode.getByCode(user.getRole()).getMessage()))
        );
    }
}
