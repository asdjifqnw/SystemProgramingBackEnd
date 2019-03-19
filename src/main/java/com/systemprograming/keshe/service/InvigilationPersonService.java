package com.systemprograming.keshe.service;

import com.systemprograming.keshe.dao.entity.InvigilationPerson;
import com.systemprograming.keshe.other.UserWithAllocated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface InvigilationPersonService extends JpaRepository<InvigilationPerson, Integer> {
    //    每场考试一共分配了多少个人 [(考试id，已分配人数),(考试id，已分配人数),(考试id，已分配人数)]
    @Query(nativeQuery = true, value = "SELECT invigilationid,COUNT(invigilationid) from invigilation_person GROUP BY invigilationid")
    List<Map<String, Object>> selectAllInvigilationPersonInfo();
    //    所有用户分配了多少次考试 [(用户id，已分配次数),(用户id，已分配次数),(用户id，已分配次数)]
    @Query(nativeQuery = true, value = "SELECT user_id,COUNT(user_id) from invigilation_person GROUP BY user_id")
    List<Map<String, Object>> findUserallocatedNumberOfInvigilation();
    //    通过userid找到用户信息
    @Query(nativeQuery = true, value = "SELECT userID,user_name,position,phone_number from user where userid in (SELECT user_id from invigilation_person where invigilationID = ?1)")
    List<Map<String, Object>> findInvigilationPersonInfo(Integer userid);

    //    删除用户监考信息
    @Modifying
    @Transactional
    @Query(value = "delete from invigilation_person where invigilationid = ?1 and user_id = ?2", nativeQuery = true)
    void deleteInvigilationPerson(Integer invigilationid, Integer userID);

    //    增加用户监考信息
    @Modifying
    @Transactional
    @Query(value = "insert into invigilation_person (invigilationid,user_id) VALUES (?1,?2)", nativeQuery = true)
    void insertInvigilatePerson(Integer invigilationid, Integer userID);

    //    单个考试需要多少人（用于校验是否超过所需人数）
    @Query(nativeQuery = true, value = "SELECT number_of_teacher from invigilation_info where invigilationid = ?1")
    Integer needNumberOfTeacher(Integer invigilationid);

    //    单个考试已分配多少人（用于校验是否超过所需人数）
    @Query(nativeQuery = true, value = "SELECT COUNT(invigilationid) from invigilation_person where invigilationid=?1 GROUP BY invigilationid")
    Integer allocatedNumberOfTeacher(Integer invigilationid);
}
