package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.DetailsSaveVo;
import com.example.entity.vo.request.EmailModifyVo;
import com.example.mapper.AccountDetailsMapper;
import com.example.service.AccountDetailsService;
import com.example.service.AccountService;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountDetailsServiceImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements AccountDetailsService {

    @Autowired
    AccountService accountService;

    @Override
    public AccountDetails findAccountDetailsById(int id) {

        return this.getById(id);

    }

    @Transactional
    @Override
    public synchronized  boolean saveAccountDetails(int id, DetailsSaveVo detailsSaveVo) {
        Account account = accountService.findAccountById(id);

        if(account==null||account.getId()==id)
        {
            accountService.update()
                    .eq("id",id)
                    .set("username",detailsSaveVo.getUsername())
                    .update();
            AccountDetails accountDetails = new AccountDetails();
            accountDetails.setId(id);
            BeanUtils.copyProperties(detailsSaveVo,accountDetails);
            saveOrUpdate(accountDetails);
            return true;
        }
        return false;

    }

}
