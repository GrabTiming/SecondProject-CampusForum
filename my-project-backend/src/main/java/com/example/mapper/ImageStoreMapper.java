package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.ImageStore;
import org.apache.ibatis.annotations.Mapper;

/**
 * (ImageStore)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-29 14:51:33
 */
@Mapper
public interface ImageStoreMapper extends BaseMapper<ImageStore> {

}

