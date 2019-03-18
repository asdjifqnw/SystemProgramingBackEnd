package com.systemprograming.keshe.service;

import com.systemprograming.keshe.dao.entity.InvigilationPerson;
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
    @Query(nativeQuery = true, value = "SELECT invigilationid,COUNT(invigilationid) from invigilation_person GROUP BY invigilationid")
    List<Map<String, Object>> selectAllInvigilationPersonInfo();

    @Query(nativeQuery = true, value = "SELECT user_name,position,phone_number from user where userid in (SELECT user_id from invigilation_person where invigilationID = ?1)")
    List<Map<String, Object>> findInvigilationPersonInfo(Integer userid);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM invigilation_info WHERE invigilation_info.invigilationid = ?1", nativeQuery = true)
    void deleteInvigilation(Integer id);
}
