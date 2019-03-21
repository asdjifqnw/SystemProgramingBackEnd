package com.systemprograming.keshe.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@ToString
public class TaskInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskID;
    @Column(nullable = false)
    private Integer taskType;
    @Column(nullable = false)
    private String taskDescription;
    @Column(nullable = false)
    private String taskName;
    @Column(nullable = false,
            columnDefinition = "DATETIME NOT NULL " +
                    "DEFAULT CURRENT_TIMESTAMP",
            updatable = false,
            insertable = false)
    private LocalDateTime insertTime;
    @Column(nullable = false,
            columnDefinition = "DATETIME NOT NULL " +
                    "DEFAULT CURRENT_TIMESTAMP ON UPDATE " +
                    "CURRENT_TIMESTAMP",
            updatable = false,
            insertable = false)
    private LocalDateTime updateTime;
    @Column(nullable = false)
    private LocalDateTime taskDeadline;
    @Column(nullable = false)
    private boolean isOpen;
}
