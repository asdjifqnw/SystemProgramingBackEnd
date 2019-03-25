package com.systemprograming.keshe.controller.Admin;

import com.alibaba.fastjson.JSONObject;
import com.systemprograming.keshe.service.Login;
import com.systemprograming.keshe.dao.entity.UserNameAndPassword;
import com.systemprograming.keshe.service.TokenService;
import com.systemprograming.keshe.annotation.PassToken;
import com.systemprograming.keshe.service.CRUDTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@ResponseBody
@Controller
@Slf4j
public class LoginCoontroller {
    @Autowired
    CRUDTest crudTest;
    @Autowired
    Login login;
    @PassToken
    @PostMapping("/login")
    public Object login(@RequestParam String username,@RequestParam String password){
        String msg = "";
        String token = "";
        String statusCode = "0";
        Object userInfo = new Object();
        TokenService tokenService = new TokenService();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        List<UserNameAndPassword> list = login.findByPhoneNumberAndPassword(username, md5Password);
        log.info(list.toString());
        if(!list.isEmpty()){
            token = tokenService.getToken(username,md5Password);
            statusCode = "1";
            msg = "登陆成功!";
            userInfo = login.getUserInfo(username);
        }else{
            statusCode = "2";
            msg = "用户名或密码错误!";
            userInfo = null;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("msg", msg);
        jsonObject.put("token", token);
        jsonObject.put("statusCode",statusCode);
        jsonObject.put("userInfo", userInfo);
        return jsonObject;
    }
}
