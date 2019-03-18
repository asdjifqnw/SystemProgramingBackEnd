package com.systemprograming.keshe.controller;

import com.alibaba.fastjson.JSONObject;
import com.systemprograming.keshe.annotation.AdminAccess;
import com.systemprograming.keshe.dao.entity.InvigilationInfo;
import com.systemprograming.keshe.service.InvigilationPersonService;
import com.systemprograming.keshe.service.InvigilationService;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.assertj.core.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@ResponseBody
public class InvigilatePersonController {
    @Autowired
    InvigilationPersonService invigilationPersonService;
    @Autowired
    InvigilationService invigilationService;

    @GetMapping("/test1")
    public void test() {
//        System.out.println(invigilationPersonService.selectAllInvigilationPersonInfo().get(0).get("COUNT(invigilationid)"));

    }

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    @AdminAccess
    @GetMapping("/getInvigilatePerson")
    public Object getInvigilatePerson() {
        JSONObject jsonObject = new JSONObject();
//        得到监考信息的list1
        List<InvigilationInfo> invigilationInfoList = invigilationService.findAll();
//        得到监考人物的list2 map 数据格式为 invigilateID 和count（invigilate）
        List<Map<String, Object>> invigilationPersonList = invigilationPersonService.selectAllInvigilationPersonInfo();
        int i, j = 0;
//        创建list，填装上面两个数据的整和（监考信息和已安排的人数）
        List<Object> list = new ArrayList<>();
        for (i = 0; i < invigilationInfoList.size(); i++) {
//            新建map临时存放数据,将结果加入至list中
            Map<String, Object> map = beanToMap(invigilationInfoList.get(i));
//            防止数组越界
            if (j < invigilationPersonList.size()) {
//                如果id一样则把count(invigilate)加入到map中 并且list2 的计数器要加一
                if (invigilationInfoList.get(i).getInvigilationID() == invigilationPersonList.get(j).get("invigilationid")) {
                    log.info("simple");
                    map.put("arrangedTeacher", invigilationPersonList.get(j).get("COUNT(invigilationid)"));
                    list.add(map);
                    log.info("i= " + i + " j = " + j);
                    j++;
//                    如果不一样则将list1的数据设置为0,并且计数器不累加
                } else {
                    log.info("NOTsimple");
                    map.put("arrangedTeacher", 0);
                    list.add(map);
                    log.info("i= " + i + " j = " + j);
                }
            } else {
                log.info("invigilationPersonList out of bound");
                map.put("arrangedTeacher", 0);
                list.add(map);
            }
        }
        jsonObject.put("InvigilatePersonInfo", list);
        return jsonObject;
    }

    @AdminAccess
    @PostMapping("/getInvigilatePersonInfo")
    public Object getInvigilatePersonInfo(@RequestParam Integer id) {
        JSONObject jsonObject = new JSONObject();
        List<Map<String, Object>> list = invigilationPersonService.findInvigilationPersonInfo(id);
//        log.info(id.toString());
//        log.info(list.get(0).toString());
        jsonObject.put("info", list);
        return jsonObject;
    }

    @AdminAccess
    @PostMapping("/deleteInvigilatePerson")
    public Object deleteInvigilatePerson(@RequestParam Integer invigilationid, @RequestParam Integer userID) {
        JSONObject jsonObject = new JSONObject();
//        log.info(invigilationid.toString()+userID.toString());
        invigilationPersonService.deleteInvigilationPerson(invigilationid, userID);
        jsonObject.put("stateCode", 200);
        jsonObject.put("msg", "删除成功");
        return jsonObject;
    }
}

