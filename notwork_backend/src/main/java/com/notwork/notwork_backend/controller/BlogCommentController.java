package com.notwork.notwork_backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.notwork.notwork_backend.common.constants.CommonConstants;
import com.notwork.notwork_backend.common.result.Result;
import com.notwork.notwork_backend.entity.pojo.BlogComment;
import com.notwork.notwork_backend.service.IBlogCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Validated
@Tag(name = "博客评论接口")
public class BlogCommentController {

    private final IBlogCommentService blogCommentService;

    @PostMapping("/blogs/{blogId}/comments")
    @Operation(summary = "发表评论")
    public Result<Void> addComment(
            @PathVariable Long blogId,
            @RequestParam @NotBlank(message = "评论内容不能为空") String content,
            @RequestParam(required = false) Long parentId,
            Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        blogCommentService.addComment(blogId, userId, content, parentId);
        return Result.success();
    }

    @GetMapping("/blogs/{blogId}/comments")
    @Operation(summary = "分页查询评论列表")
    public Result<IPage<BlogComment>> getComments(
            @PathVariable Long blogId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return Result.success(blogCommentService.getCommentsByBlogId(blogId, pageNum, pageSize));
    }

    @DeleteMapping("/comments/{id}")
    @Operation(summary = "删除评论")
    public Result<Void> deleteComment(@PathVariable Long id, Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        blogCommentService.deleteComment(id, userId);
        return Result.success();
    }
}
