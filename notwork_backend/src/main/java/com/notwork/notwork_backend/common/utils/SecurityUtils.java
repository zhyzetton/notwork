package com.notwork.notwork_backend.common.utils;

import com.notwork.notwork_backend.common.auth.LoginUser;
import com.notwork.notwork_backend.common.enums.RoleCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    /**
     * 获取当前登录用户 ID
     */
    public static Long getCurrentUserId() {

        LoginUser loginUser = getCurrentLoginUser();
        return loginUser != null ? loginUser.getUserId() : null;
    }

    /**
     * 获取当前 LoginUser 对象
     */
    public static LoginUser getCurrentLoginUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof LoginUser user) {
            return user;
        }
        return null;
    }

    /**
     * 获取当前用户角色字符串，如 "ROLE_ADMIN"
     */
    public static String getCurrentRole() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    /**
     * 判断当前用户是否为管理员
     */
    public static boolean isAdmin() {
        return RoleCode.ADMIN.getMessage().equals(getCurrentRole());
    }
}