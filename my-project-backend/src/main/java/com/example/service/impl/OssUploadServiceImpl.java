package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.dto.ImageStore;
import com.example.service.AccountService;
import com.example.service.ImageStoreService;
import com.example.service.OssUploadService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import com.example.utils.PathUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

@Data
@Service
@ConfigurationProperties(prefix = "myoss")
@Slf4j
public class OssUploadServiceImpl implements OssUploadService {

    @Resource
    private AccountService accountService;

    @Resource
    private ImageStoreService imageStoreService;

    @Resource
    FlowUtils flowUtils;

    private String accessKey;

    private String secretKey;

    private String bucket;


    @Override
    public RestBean<String> uploadAvatar(int id,MultipartFile img) {
        String originalFilename = img.getOriginalFilename();

        long size = img.getSize();
        if(size>2*1024*1024)
        {
           return RestBean.failure(400,"文件大小超过2MB");
        }

        //不是图片
        if(!originalFilename.endsWith(".png")&&!originalFilename.endsWith(".jpg"))
        {
            return RestBean.failure(400,"头像非.png或.jpg格式");
        }

        String filePath = PathUtils.generateFilePath(originalFilename);

        String url = uploadOss(img,filePath,id);
        if(url==null)
        {
            return RestBean.failure(400,"上传文件失败");
        }
        //查看用户原来是否有头像，有就删除原来的头像
        Account account = accountService.getById(id);
        String avatar = account.getAvatar();
        if(avatar!=null)
        {
            deleteOss(avatar);
        }
        //更新头像
        account.setAvatar(url);
        if(accountService.updateById(account))
        {
            return RestBean.success(url);
        }
        else
        {
            return RestBean.failure(400,"系统错误");
        }

    }

    //更新头像时删除旧的图像
    private void deleteOss(String avatar) {

        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        UploadManager uploadManager = new UploadManager(cfg);

        Auth auth = Auth.create(accessKey, secretKey);


        BucketManager bucketManager = new BucketManager(auth, cfg);

        String key = avatar.substring(Const.OSS_UPLOAD_ADDRESS.length());
        log.info(key);
        try {
            bucketManager.delete(bucket, key);
        } catch (QiniuException e) {
            log.info("{ }",e);
            throw new RuntimeException(e);
        }

    }


    //上传图片
    private String uploadOss(MultipartFile imgFile, String filePath,int id){
        Configuration cfg = new Configuration(Region.huanan());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        UploadManager uploadManager = new UploadManager(cfg);

        //打开七牛云，把鼠标悬浮在右上角的个人头像，然后就会看到'密钥管理'，点击进入就有你的密钥，把其中的AK和SK复制到下面两行

        //String bucket = "liangjt-campusforum";
        //为避免上面3行暴露信息，我们会把信息写到application.yml里面，然后添加ConfigurationProperties注解、3个成员变量即可读取

        //文件名，如果写成null的话，就以文件内容的hash值作为文件名
        String key =  filePath ;
        try {

            //上面两行是官方写的(注释掉)，下面那几行是我们写的
            InputStream xxinputStream = imgFile.getInputStream();

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(xxinputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                //得到图片网址
                return Const.OSS_UPLOAD_ADDRESS+key;

            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    log.error("七牛云服务出现问题"+ex2.getMessage() ,ex2);

                }
            }
        }catch (Exception e) {
            log.error("出现问题"+e.getMessage(),e);
        }
        return null;
    }


    //上传 文章 图片
    @Override
    public RestBean<String> uploadArticleImg(int id, MultipartFile img) {

        if(img.getSize()>5*1024*1024)
        {
            return RestBean.failure(400,"图片大小超过5MB");
        }

        String key = Const.FORUM_IMAGE_COUNTER+id;//Redis存的请求key
        System.out.println(key);
        if(flowUtils.limitPeriodCheck(key,20,3600))//一小时内不超过20张图片
        {
            System.out.println("不通过");
            return RestBean.failure(400,"图片插入频繁，请一个小时后再试");
        }

        String filePath = PathUtils.generateFilePath(img.getOriginalFilename());

        String url = uploadOss(img, filePath, id);

        if(url==null) return RestBean.failure(400,"上传失败");


        ImageStore imageStore = new ImageStore(id, url, new Date());

        if(imageStoreService.save(imageStore))
        {
            return RestBean.success(url);
        }
        else
        {
            return RestBean.failure(400,"系统异常，请联系管理员");
        }
    }


}
