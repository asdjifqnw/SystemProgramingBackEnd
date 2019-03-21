package com.systemprograming.keshe.service;

import com.systemprograming.keshe.dao.entity.TaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TaskInfoService extends JpaRepository<TaskInfo, Integer> {
    @Override
    List<TaskInfo> findAll();

    @Modifying
    @Transactional
    @Query(value = "update task_info set is_open = ?1 where taskid = ?2", nativeQuery = true)
    void modifyState(boolean isOpen, Integer id);

    @Override
    <S extends TaskInfo> S save(S s);

    @Modifying
    @Transactional
    @Query(value = "DELETE from task_info where taskid = ?1", nativeQuery = true)
    void deleteByTaskID(Integer ID);
}
