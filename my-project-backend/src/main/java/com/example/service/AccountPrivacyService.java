package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVo;
import com.example.mapper.AccountPrivacyMapper;


public interface AccountPrivacyService extends IService<AccountPrivacy> {

    void savePrivacy(int id, PrivacySaveVo vo);

    AccountPrivacy getAccountPrivacyById(int id);


}
