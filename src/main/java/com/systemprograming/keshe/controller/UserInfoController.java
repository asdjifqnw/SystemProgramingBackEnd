package com.systemprograming.keshe.controller;

import com.alibaba.fastjson.JSONObject;
import com.systemprograming.keshe.annotation.AdminAccess;
import com.systemprograming.keshe.annotation.SuperAdminAccess;
import com.systemprograming.keshe.dao.entity.User;
import com.systemprograming.keshe.service.AddNewUser;
import com.systemprograming.keshe.service.CRUDTest;
import com.systemprograming.keshe.service.Login;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@ResponseBody
@Controller
public class UserInfoController {
    @Autowired
    AddNewUser addNewUser;
    @Autowired
    Login login;
    @Autowired
    CRUDTest crudTest;

    /**
     * 1,检测是否为超级管理员
     * 2,分析参数，检测正确与否
     * 3,执行插入，返回结果
     */
    @PostMapping("/addNewUser")
    @SuperAdminAccess
    public Object addNewUser(@RequestParam String userName,
                             @RequestParam String userPosition,
                             @RequestParam String userPhoneNumber,
                             @RequestParam boolean userIsAdmin,
                             @RequestParam String userDescription,
                             @RequestParam String userPassword) {
        JSONObject object = new JSONObject();
        log.info(userPosition);
        List<User> list = addNewUser.userExist(userPhoneNumber);
        if (!list.isEmpty()) {
            object.put("stateCode", 401);
            object.put("msg", "用户手机号已存在");
            return object;
        }
        addNewUser.addUser(userName, userPhoneNumber, userDescription, userPosition, userIsAdmin, false);
        String md5Password = DigestUtils.md5DigestAsHex(userPassword.getBytes());
        login.addUser(md5Password, userPhoneNumber, userName);
        object.put("stateCode", 200);
        object.put("msg", "插入成功");
        return object;
    }

    /**
     * 1,检测是否为超级管理员
     * 2,分析参数，检测正确与否
     * 3,执行插入，返回结果
     */
    @GetMapping("/listUser")
    @AdminAccess
    public List<User> listUser() {
        List<User> list = crudTest.findAll();
        return list;
    }

    @AdminAccess
    @PostMapping("/modifyUserInfo")
    public Object modifyUserInfo(
            @RequestParam Integer userID,
            @RequestParam String userName,
            @RequestParam String position,
            @RequestParam String introduction,
            @RequestParam boolean admin,
            @RequestParam String phoneNumber) {
        JSONObject jsonObject = new JSONObject();
        List<User> list = addNewUser.userExist(phoneNumber);
        if (!list.isEmpty()) {
            if (list.get(0).getUserID() != userID) {
                jsonObject.put("stateCode", 401);
                jsonObject.put("msg", "用户手机号已存在");
                return jsonObject;
            }
        }
        log.info(userID + "-->" + userName + "-->" + position + "-->" + introduction + "-->" + admin + "-->" + phoneNumber);
        String beforePhoneNumber = crudTest.findUserPhoneNumber(userID);
        log.info("before:" + beforePhoneNumber + " after: " + phoneNumber);
        crudTest.updateUserInfo(introduction, admin, phoneNumber, position, userName, userID);
        Integer id = crudTest.findUserID(beforePhoneNumber);
        log.info(id.toString());
        crudTest.updateUserPhoneNumber(phoneNumber, id);
        jsonObject.put("stateCode", 200);
        jsonObject.put("msg", "插入成功");
        return jsonObject;
    }

    @AdminAccess
    @PostMapping("/modifyUserPassword")
    public Object modifyUserPassword(@RequestParam String phoneNumber,
                                     @RequestParam String Password,
                                     @RequestParam String retypePassword) {
        JSONObject jsonObject = new JSONObject();
        log.info("phoneNumber :" + phoneNumber + " Passwpord: " + Password);
        if (!Password.equals(retypePassword)) {
            jsonObject.put("stateCode", "401");
            jsonObject.put("msg", "两次密码输入不一致");
            return jsonObject;
        }
        String md5Password = DigestUtils.md5DigestAsHex(Password.getBytes());
        crudTest.modifyPassword(md5Password, phoneNumber);
        jsonObject.put("stateCode", "200");
        jsonObject.put("msg", "更新成功");
        return jsonObject;
    }
}
