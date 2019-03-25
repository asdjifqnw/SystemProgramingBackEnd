package com.systemprograming.keshe.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.systemprograming.keshe.annotation.AdminAccess;
import com.systemprograming.keshe.annotation.PassToken;
import com.systemprograming.keshe.annotation.SuperAdminAccess;
import com.systemprograming.keshe.annotation.UserLoginToken;
import com.systemprograming.keshe.dao.entity.User;
import com.systemprograming.keshe.service.AddNewUser;
import com.systemprograming.keshe.service.Login;
import com.systemprograming.keshe.dao.entity.UserNameAndPassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;


/**
 * @author jinbin
 * @date 2018-07-08 20:41
 */
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    Login login;
    @Autowired
    AddNewUser addNewUser;
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        String token = httpServletRequest.getHeader("Authorization");// 从 http 请求头中取出 token
        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
//                log.info(token);
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 user id
                String phoneNumber;
                try {
                    phoneNumber = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new RuntimeException();
                }
                List<UserNameAndPassword> list = login.findByPhoneNumber(phoneNumber);
//                log.info(list.get(0).getPassword());
                if (list.isEmpty()) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(list.get(0).getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new RuntimeException(e.getMessage());
                }
                return true;
            }
        }
        //需要超级管理员权限
        if(method.isAnnotationPresent(SuperAdminAccess.class)){
            log.info("开始检查是否有超级管理权限");
            SuperAdminAccess superAdminAccess = method.getAnnotation(SuperAdminAccess.class);
            if(superAdminAccess.required()){
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 user id
                String phoneNumber;
                try {
                    phoneNumber = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }
                List<UserNameAndPassword> list = login.findByPhoneNumber(phoneNumber);
//                log.info(list.get(0).getPassword());
                if (list.isEmpty()) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(list.get(0).getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new RuntimeException(e.getMessage());
                }
                List<User> list1 = addNewUser.userExist(phoneNumber);
                log.info("用户信息为"+list.get(0).toString());
                if (!list1.get(0).isSuperAdmin()) {
                    throw new RuntimeException("非法操作!");
                }
                log.info("有超级管理权限");
                return true;
            }
        }
        //需要管理员权限
        if(method.isAnnotationPresent(AdminAccess.class)){
            log.info("开始检查是否有管理权限");
            AdminAccess adminAccess = method.getAnnotation(AdminAccess.class);
            if(adminAccess.required()){
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 user id
                String phoneNumber;
                try {
                    phoneNumber = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }
                List<UserNameAndPassword> list = login.findByPhoneNumber(phoneNumber);
//                log.info(list.get(0).getPassword());
                if (list.isEmpty()) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }
                // 验证 token
                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(list.get(0).getPassword())).build();
                try {
                    jwtVerifier.verify(token);
                } catch (JWTVerificationException e) {
                    throw new RuntimeException(e.getMessage());
                }
                List<User> list1 = addNewUser.userExist(phoneNumber);
                log.info("用户信息为"+list1.get(0).isAdmin());
                if ((!list1.get(0).isAdmin())&&(!list1.get(0).isSuperAdmin())) {
                    throw new RuntimeException("非法操作!");
                }
                log.info("有管理权限");
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
