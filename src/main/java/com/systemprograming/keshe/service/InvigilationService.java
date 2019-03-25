package com.systemprograming.keshe.service;

import com.systemprograming.keshe.dao.entity.InvigilationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvigilationService extends JpaRepository<InvigilationInfo, Integer> {
    @Modifying
    @Transactional
    @Query(value = "insert into invigilation_info (course_name,number_of_teacher,invigilation_begin_time) values (?1,?2,?3)", nativeQuery = true)
    void insertNewInvigilation(String courseName, Integer numberOfTeacher, LocalDateTime beginTime);

    @Modifying
    @Transactional
    @Query(value = "update invigilation_info set course_name=?1,number_of_teacher=?2,invigilation_begin_time=?3 where invigilationid = ?4", nativeQuery = true)
    void updateInvigilation(String courseName, Integer numberOfTeacher, LocalDateTime beginTime, Integer id);

    List<InvigilationInfo> findAll();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM invigilation_info WHERE invigilation_info.invigilationid = ?1", nativeQuery = true)
    void deleteInvigilation(Integer id);

}
