package com.systemprograming.keshe.service;

import com.systemprograming.keshe.dao.entity.User;
import com.systemprograming.keshe.dao.entity.UserNameAndPassword;
import net.bytebuddy.implementation.bytecode.assign.TypeCasting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface Login extends JpaRepository<UserNameAndPassword, Integer> {
    //    List<UserNameAndPassword> findByUserName(String userName);
    List<UserNameAndPassword> findByPhoneNumber(String phoneNumber);

    //    List<UserNameAndPassword> findByUserNameAndPassword(String userName,String password);
    @Query(value = "select * from user_name_and_password where phone_number = ?1 and password = ?2", nativeQuery = true)
    List<UserNameAndPassword> findByPhoneNumberAndPassword(String phoneNumber, String password);

    @Query(value = "select * from user where phone_number = ?1", nativeQuery = true)
    Object getUserInfo(String phoneNumber);

    @Modifying
    @Transactional
    @Query(value = "insert into user_name_and_password (password,phone_number,user_name) values(?1,?2,?3)",nativeQuery = true)
    void addUser(String password,String phoneNumber,String userName);
}
