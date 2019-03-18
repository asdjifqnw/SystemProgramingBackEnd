package com.systemprograming.keshe.service;

import com.systemprograming.keshe.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CRUDTest extends JpaRepository<User, Integer> {
    List<User> findAll();

    @Modifying
    @Transactional
    @Query(value = "update user set introduction = ?1, is_admin = ?2, phone_number = ?3, position= ?4, user_name = ?5 WHERE userid = ?6", nativeQuery = true)
    void updateUserInfo(String introduction, boolean isAdmin, String phoneNumber, String position, String username, Integer userid);

    @Query(value = "select phone_number from user where userid = ?1", nativeQuery = true)
    String findUserPhoneNumber(Integer id);

    @Modifying
    @Transactional
    @Query(value = "update user_name_and_password set password = ?1 where phone_number = ?2", nativeQuery = true)
    void modifyPassword(String password, String phoneNumber);

    @Query(value = "select id from user_name_and_password where phone_number = ?1", nativeQuery = true)
    Integer findUserID(String phoneNumber);

    @Modifying
    @Transactional
    @Query(value = "update user_name_and_password set phone_number = ?1 where id = ?2", nativeQuery = true)
    void updateUserPhoneNumber(String phoneNumber, Integer id);
}
