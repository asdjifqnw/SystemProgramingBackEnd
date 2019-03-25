package com.systemprograming.keshe.controller.Admin;

import com.alibaba.fastjson.JSONObject;
import com.systemprograming.keshe.annotation.AdminAccess;
import com.systemprograming.keshe.dao.entity.TaskInfo;
import com.systemprograming.keshe.service.TaskInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
public class TaskInfoController {
    @Autowired
    TaskInfoService taskInfoService;

    @AdminAccess
    @PostMapping("/insertNewTask")
    public Object insertNewTask(@RequestParam String taskDescription,
                                @RequestParam Integer taskType,
                                @RequestParam String taskName,
                                @RequestParam String taskDeadline) {
        JSONObject jsonObject = new JSONObject();
        if (taskDescription.isEmpty() || taskType == null || taskName.isEmpty() || taskDeadline.isEmpty()) {
            jsonObject.put("stateCode", 401);
            jsonObject.put("msg", "数据错误!");
            return jsonObject;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(taskDeadline, df);
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskDeadline(ldt);
        taskInfo.setTaskDescription(taskDescription);
        taskInfo.setTaskName(taskName);
        taskInfo.setTaskType(taskType);
        taskInfo.setOpen(true);
        taskInfoService.save(taskInfo);
        jsonObject.put("stateCode", 200);
        jsonObject.put("msg", "添加成功！");
        return jsonObject;
    }

    @AdminAccess
    @GetMapping("/getTaskInfo")
    public List<TaskInfo> getTaskInfo() {
        List<TaskInfo> list = taskInfoService.findAll();
        return list;
    }

    @AdminAccess
    @PostMapping("/modifyTaskInfo")
    public JSONObject modifyTaskInfo(@RequestParam Integer taskID,
                                     @RequestParam boolean open) {
        JSONObject jsonObject = new JSONObject();
        taskInfoService.modifyState(!open, taskID);
        jsonObject.put("stateCode", 200);
        jsonObject.put("msg", "修改成功！");
        return jsonObject;
    }

    @AdminAccess
    @PostMapping("/modifyTaskInfo2")
    public JSONObject modifyTaskInfo2(@RequestParam Integer taskID,
                                      @RequestParam String taskDescription,
                                      @RequestParam Integer taskType,
                                      @RequestParam String taskName,
                                      @RequestParam String taskDeadline) {
        JSONObject jsonObject = new JSONObject();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime ldt = LocalDateTime.parse(taskDeadline, df);
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setTaskDeadline(ldt);
        taskInfo.setTaskDescription(taskDescription);
        taskInfo.setTaskName(taskName);
        taskInfo.setTaskType(taskType);
        taskInfo.setTaskID(taskID);
        taskInfoService.save(taskInfo);
        jsonObject.put("stateCode", 200);
        jsonObject.put("msg", "修改成功！");
        return jsonObject;
    }
    @AdminAccess
    @PostMapping("/deleteTaskInfo")
    public Object deleteTaskInfo(@RequestParam Integer taskID){
        JSONObject jsonObject = new JSONObject();
        taskInfoService.deleteByTaskID(taskID);
        jsonObject.put("stateCode", 200);
        jsonObject.put("msg", "删除成功！");
        return jsonObject;
    }
}
