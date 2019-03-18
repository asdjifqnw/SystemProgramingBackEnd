package com.systemprograming.keshe.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.systemprograming.keshe.dao.entity.UserNameAndPassword;
import org.springframework.stereotype.Service;

@Service("TokenService")
public class TokenService {
    public String getToken(String phoneNumber, String Password) {
        String token="";
        token= JWT.create().withAudience(phoneNumber)// 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(Password));// 以 password 作为 token 的密钥
        return token;
    }
}