package com.systemprograming.keshe.service;

import com.systemprograming.keshe.dao.entity.InvigilationInfo;
import com.systemprograming.keshe.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Repository
public interface UserService extends JpaRepository<User,Integer> {
    @Override
    <S extends User> S save(S s);

    @Modifying
    @Transactional
    @Query(value = "insert into user (user_name,phone_number,introduction,position,is_admin,is_super_admin) VALUES (?1,?2,?3,?4,?5,?6)", nativeQuery = true)
    void addUser(String userName,String phoneNumber,String Introduction,String position,boolean isAdmin,boolean isSuperAdmin);

    @Query(value = "SELECT * from invigilation_info where invigilationid in (SELECT invigilationid from invigilation_person where user_id = ?1 )", nativeQuery = true)
    List<Map<String,Object>> findUserInvigilation(Integer userId);

    @Query(value = "SELECT * from user where userid in (SELECT user_id from invigilation_person where invigilationid = ?1)", nativeQuery = true)
    List<Map<String,Object>> checkStaff(Integer invigilationId);
    @Query(nativeQuery = true,value="select * from invigilation_info")
    List<Map<String,Object>> listInvigilation();
    @Query(nativeQuery = true,value="select * from task_info where is_open = true")
    List<Map<String,Object>> listTask();
}
