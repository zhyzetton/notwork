package com.notwork.notwork_backend.controller;

import com.notwork.notwork_backend.common.result.Result;
import com.notwork.notwork_backend.service.IFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/upload")
@Tag(name = "文件上传接口")
public class UploadController {

    private final IFileService fileService;

    @PostMapping("/image")
    @Operation(summary = "上传图片到MinIO")
    public Result<String> uploadImage(MultipartFile file) {
        String fileUrl = fileService.uploadImage(file);
        return Result.success(fileUrl);
    }
}
