package com.notwork.notwork_backend.controller;

import com.notwork.notwork_backend.common.auth.LoginUser;
import com.notwork.notwork_backend.common.result.Result;
import com.notwork.notwork_backend.service.IBlogLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/blogs/{blogId}/likes")
@Tag(name = "博客点赞接口")
public class BlogLikeController {

    private final IBlogLikeService blogLikeService;

    @PostMapping
    @Operation(summary = "点赞/取消点赞")
    public Result<Boolean> toggleLike(@PathVariable Long blogId, Authentication authentication) {
        LoginUser user = (LoginUser) authentication.getPrincipal();
        boolean liked = blogLikeService.toggleLike(blogId, user.getUserId());
        return Result.success(liked);
    }

    @GetMapping("/status")
    @Operation(summary = "查询当前用户是否点赞")
    public Result<Boolean> hasLiked(@PathVariable Long blogId, Authentication authentication) {
        LoginUser user = (LoginUser) authentication.getPrincipal();
        return Result.success(blogLikeService.hasLiked(blogId, user.getUserId()));
    }
}
