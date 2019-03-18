package com.systemprograming.keshe.service;

import com.systemprograming.keshe.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AddNewUser extends JpaRepository<User,Integer> {
    @Query(value = "select * from user where phone_number = ?1", nativeQuery = true)
    List<User> userExist(String phoneNumber);
    @Modifying
    @Transactional
    @Query(value = "insert into user (user_name,phone_number,introduction,position,is_admin,is_super_admin) VALUES (?1,?2,?3,?4,?5,?6)", nativeQuery = true)
    void addUser(String userName,String phoneNumber,String Introduction,String position,boolean isAdmin,boolean isSuperAdmin);
}
