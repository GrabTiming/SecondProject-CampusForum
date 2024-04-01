package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户 隐私设置 信息是否公开 默认全公开
 */
@Data
@TableName("db_account_privacy")
public class AccountPrivacy implements BaseData {

    @TableId(type = IdType.AUTO)
    private final Integer id;

    private boolean phone  = true;

    private boolean email =true;

    private boolean wx = true;

    private boolean qq = true;

    private boolean gender = true;


    //隐藏表 哪些信息需要隐藏
    public String[] hiddenFields()
    {
        List<String> strings  = new ArrayList<>();

        Field[] fields = this.getClass().getDeclaredFields();

        for(Field field : fields)
        {
            try{
                if(field.getType().equals(boolean.class)&&!field.getBoolean(this))
                {
                    strings.add(field.getName());
                }
            }catch (Exception ignored) {}
        }
        return strings.toArray(String[]::new);
    }

}
