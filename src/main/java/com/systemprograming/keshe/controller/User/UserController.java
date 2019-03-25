package com.systemprograming.keshe.controller.User;

import com.alibaba.fastjson.JSONObject;
import com.systemprograming.keshe.annotation.UserLoginToken;
import com.systemprograming.keshe.dao.entity.InvigilationInfo;
import com.systemprograming.keshe.dao.entity.User;
import com.systemprograming.keshe.service.AddNewUser;
import com.systemprograming.keshe.service.CRUDTest;
import com.systemprograming.keshe.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.*;

@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AddNewUser addNewUser;
    @Autowired
    CRUDTest crudTest;

    @UserLoginToken
    @PostMapping("/updateUserInfo")
    public Object updateUserInfo(@RequestParam Integer userId,
                                 @RequestParam String userName,
                                 @RequestParam String userPosition,
                                 @RequestParam boolean userIsAdmin,
                                 @RequestParam boolean userIsSuperAdmin,
                                 @RequestParam String userDescription,
                                 @RequestParam String userPhoneNumber) {
        JSONObject jsonObject = new JSONObject();
        List<User> list = addNewUser.userExist(userPhoneNumber);
        if (!list.isEmpty()) {
            if (list.get(0).getUserID() != userId) {
                jsonObject.put("msg", "用户手机号已存在");
                jsonObject.put("stateCode", 401);
                return jsonObject;
            }
        }
        User user = new User();
        user.setUserID(userId);
        user.setPosition(userPosition);
        user.setAdmin(userIsAdmin);
        user.setSuperAdmin(userIsSuperAdmin);
        user.setIntroduction(userDescription);
        user.setPhoneNumber(userPhoneNumber);
        user.setUserName(userName);
        userService.save(user);
        crudTest.updateUserPhoneNumber(userPhoneNumber, userId);
        jsonObject.put("msg", "更新成功");
        jsonObject.put("stateCode", 200);
        return jsonObject;
    }

    @UserLoginToken
    @PostMapping("/getMyInvogilation")
    public Object getMyInvogilation(@RequestParam Integer userId) {
        List<Map<String, Object>> list = userService.findUserInvigilation(userId);
        return list;
    }

    @UserLoginToken
    @PostMapping("/checkStaff")
    public Object checkStaff(@RequestParam Integer checkingRow) {
        List<Map<String, Object>> list = userService.checkStaff(checkingRow);
        return list;
    }

    @UserLoginToken
    @GetMapping("/getAllInvigilation")
    public Object getAllInvigilation() {
        List<Map<String, Object>> list = userService.listInvigilation();
        return list;
    }

    @UserLoginToken
    @GetMapping("/getAllTask")
    public Object getAllTask() {
        List<Map<String, Object>> list = userService.listTask();
        return list;
    }

    @UserLoginToken
    @RequestMapping("/fileUpload")
    public List<String> fileUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Collection<Part> collection = request.getParts();
        Iterator<Part> iterator = collection.iterator();
        List<String> URL = new ArrayList<>();
        while (iterator.hasNext()) {
            Part part = iterator.next();
            InputStream is = part.getInputStream();
            File file = new File("src/fileUpload/" + part.getSubmittedFileName());
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream os = new FileOutputStream(file);
            byte[] bytes = new byte[is.available()];
            if ((is.read(bytes)) != -1) {
                os.write(bytes);
            }
            URL.add(file.getAbsolutePath());
        }
        return URL;
    }
}
