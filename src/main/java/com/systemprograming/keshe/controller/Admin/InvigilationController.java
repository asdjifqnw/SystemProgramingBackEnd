package com.systemprograming.keshe.controller.Admin;

import com.alibaba.fastjson.JSONObject;
import com.systemprograming.keshe.annotation.AdminAccess;
import com.systemprograming.keshe.dao.entity.InvigilationInfo;
import com.systemprograming.keshe.service.InvigilationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Slf4j
@ResponseBody
@Controller
public class InvigilationController {
    @Autowired
    InvigilationService invigilationService;

    @PostMapping("/insertNewInvigilation")
    public Object insertNewInvigilation(@RequestParam String courseName,
                                        @RequestParam Integer numberOfTeacher,
                                        @RequestParam String beginTime) {
        JSONObject jsonObject = new JSONObject();
//        beginTime = beginTime.replace("T", " ");
//        beginTime = beginTime.replace(".000Z", "");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(beginTime, df);
        System.out.println("String类型的时间转成LocalDateTime：" + ldt);
        log.info("courseName: " + courseName + " numberOfTeacher: " + numberOfTeacher + " beginTime: " + beginTime);
        invigilationService.insertNewInvigilation(courseName, numberOfTeacher,ldt);
        jsonObject.put("stateCode", 200);
        jsonObject.put("msg", "插入成功");
        return jsonObject;
    }
    @AdminAccess
    @GetMapping("/listInvigilationInfo")
    public List<InvigilationInfo> listInvigilationInfo(){
        List<InvigilationInfo> list = invigilationService.findAll();
        return list;
    }
    @AdminAccess
    @PostMapping("/modifyInvigilation")
    public Object modifyInvigilation(@RequestParam String courseName,
                                     @RequestParam Integer numberOfTeacher,
                                     @RequestParam String invigilationBeginTime,
                                     @RequestParam Integer invigilationID){
        JSONObject jsonObject  = new JSONObject();
        invigilationBeginTime = invigilationBeginTime.replace("T", " ");
        invigilationBeginTime = invigilationBeginTime.replace(".000Z", "");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(invigilationBeginTime, df);
        log.info("courseName: " + courseName + " numberOfTeacher: " + numberOfTeacher + " beginTime: " + invigilationBeginTime);
        log.info(ldt.toString());
        invigilationService.updateInvigilation(courseName, numberOfTeacher, ldt, invigilationID);
        jsonObject.put("stateCode", 200);
        jsonObject.put("msg", "修改成功");
        return jsonObject;
    }
    @AdminAccess
    @PostMapping("/deleteInvigilation")
    public Object deleteInvigilation(@RequestParam Integer invigilationID){
        JSONObject jsonObject  = new JSONObject();
        invigilationService.deleteInvigilation(invigilationID);
        jsonObject.put("stateCode", 200);
        jsonObject.put("msg", "修改成功");
        return jsonObject;
    }
}
