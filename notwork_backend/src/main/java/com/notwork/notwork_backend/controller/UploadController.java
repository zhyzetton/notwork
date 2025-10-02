package com.notwork.notwork_backend.controller;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/upload")
public class UploadController {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    @PostMapping("/image")
    public Map<String, Object> uploadImage(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, String> succMap = new HashMap<>();
        List<String> errFiles = new ArrayList<>();

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            // 拼接文件访问URL
            String fileUrl = endpoint + "/" + bucketName + "/" + fileName;
            succMap.put(file.getOriginalFilename(), fileUrl);
            data.put("succMap", succMap);
            data.put("errFiles", errFiles);
            result.put("msg", "上传成功");
            result.put("code", 200);
            result.put("data", data);
        }catch (MinioException e) {
            errFiles.add(file.getOriginalFilename());
            data.put("succMap", succMap);
            data.put("errFiles", errFiles);

            result.put("msg", e.getMessage());
            result.put("code", 500);
            result.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
