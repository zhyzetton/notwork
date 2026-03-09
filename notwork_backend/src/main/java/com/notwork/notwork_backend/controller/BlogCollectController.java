package com.notwork.notwork_backend.controller;

import com.notwork.notwork_backend.common.result.Result;
import com.notwork.notwork_backend.service.IBlogCollectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/blogs/{blogId}/collects")
@Tag(name = "博客收藏接口")
public class BlogCollectController {

    private final IBlogCollectService blogCollectService;

    @PostMapping
    @Operation(summary = "收藏/取消收藏")
    public Result<Boolean> toggleCollect(@PathVariable Long blogId, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        boolean collected = blogCollectService.toggleCollect(blogId, userId);
        return Result.success(collected);
    }

    @GetMapping("/status")
    @Operation(summary = "查询当前用户是否收藏")
    public Result<Boolean> hasCollected(@PathVariable Long blogId, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return Result.success(blogCollectService.hasCollected(blogId, userId));
    }
}
