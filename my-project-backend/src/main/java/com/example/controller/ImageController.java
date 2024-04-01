package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.OssUploadService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
public class ImageController {


    @Resource
    private OssUploadService uploadService;

    @PostMapping("/avatar")
    public RestBean<String> uploadAvatar(@RequestParam("file") MultipartFile avatar,
                                         @RequestAttribute(Const.ATTR_USER_ID) int id)
    {
        return uploadService.uploadAvatar(id,avatar);
    }

//    @PostMapping("/image")
//    public RestBean<String> uploadImage(@RequestParam("file") MultipartFile avatar,
//                                         @RequestAttribute(Const.ATTR_USER_ID) int id)
//    {
//        return uploadService.uploadArticleImg(id,avatar);
//    }

}
