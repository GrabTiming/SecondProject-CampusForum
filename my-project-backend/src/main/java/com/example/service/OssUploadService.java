package com.example.service;

import com.example.entity.RestBean;
import org.springframework.web.multipart.MultipartFile;

public interface OssUploadService {

    RestBean<String> uploadAvatar(int id,MultipartFile img);

    RestBean<String> uploadArticleImg(int id,MultipartFile img);

}
