package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.AccountVo;
import com.example.entity.vo.request.ChangePasswordVo;
import com.example.entity.vo.request.DetailsSaveVo;
import com.example.entity.vo.request.EmailModifyVo;
import com.example.entity.vo.request.PrivacySaveVo;
import com.example.entity.vo.response.AccountDetailsVo;
import com.example.entity.vo.response.AccountPrivacyVo;
import com.example.service.AccountDetailsService;
import com.example.service.AccountPrivacyService;
import com.example.service.AccountService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    //用户服务
    @Resource
    private AccountService accountService;

    //用户信息服务
    @Resource
    private AccountDetailsService accountDetailsService;

    //用户隐私服务
    @Resource
    private AccountPrivacyService accountPrivacyService;

    /**
     * 获取当前用户信息
     * @param id 用户id
     */
    @GetMapping("/info")
    public RestBean<AccountVo> info(@RequestAttribute(Const.ATTR_USER_ID) int id)
    {
        Account account = accountService.findAccountById(id);
        return RestBean.success(account.asViewObject(AccountVo.class));
    }

    @GetMapping("/details")
    public RestBean<AccountDetailsVo> getDetails(@RequestAttribute(Const.ATTR_USER_ID) int id)
    {

        AccountDetails accountDetails = Optional
                .ofNullable(accountDetailsService.findAccountDetailsById(id))
                .orElseGet(AccountDetails::new);
        return RestBean.success(accountDetails.asViewObject(AccountDetailsVo.class));
    }

    //保存用户信息
    @PostMapping("/save-details")
    public RestBean<Void> saveDetails(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid DetailsSaveVo vo)
    {
        boolean success = accountDetailsService.saveAccountDetails(id, vo);
        return success ? RestBean.success() :RestBean.failure(400,"此用户名已存在,请修改其他用户名");
    }

    /**
     * 邮箱重置
     */
    @PostMapping("/modify-email")
    public  RestBean<Void> modifyEmail(@RequestAttribute(Const.ATTR_USER_ID) int id,@RequestBody @Valid EmailModifyVo vo)
    {
        String msg = accountService.modifyEmail(id, vo);

        return msg==null ? RestBean.success() :RestBean.failure(400,msg);
    }

    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    public RestBean<Void> ChangePasswd(@RequestAttribute(Const.ATTR_USER_ID) int id, @RequestBody @Valid ChangePasswordVo vo)
    {
        String msg =  accountService.changePassword(id,vo);

        return msg==null ? RestBean.success() :RestBean.failure(400,msg);
    }

    /**
     * 保存隐私设置
     */
    @PostMapping("/save-privacy")
    public  RestBean<Void> savePrivacy(@RequestAttribute(Const.ATTR_USER_ID) int id,
                                       @RequestBody @Valid PrivacySaveVo vo)
    {
        accountPrivacyService.savePrivacy(id,vo);
        return RestBean.success();
    }

    @GetMapping("/privacy")
    public  RestBean<AccountPrivacyVo> getPrivacy(@RequestAttribute(Const.ATTR_USER_ID) int id)
    {
        AccountPrivacyVo accountPrivacyVo = accountPrivacyService.getAccountPrivacyById(id).asViewObject(AccountPrivacyVo.class);
        return RestBean.success(accountPrivacyVo);
    }

}
