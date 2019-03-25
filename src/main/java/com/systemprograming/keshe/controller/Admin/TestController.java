package com.systemprograming.keshe.controller.Admin;

import com.systemprograming.keshe.service.CRUDTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveUserDetailsServiceAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
@Controller
public class TestController {
    @Autowired
    CRUDTest crudTest;
    @GetMapping("/test")
    public Object test(){
        return crudTest.findAll();
    }
}
