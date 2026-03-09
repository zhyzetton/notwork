package com.notwork.notwork_backend.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {

    String uploadImage(MultipartFile file);
}
