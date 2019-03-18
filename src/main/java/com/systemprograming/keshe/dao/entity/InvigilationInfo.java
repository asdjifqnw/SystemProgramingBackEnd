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
public class InvigilationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer invigilationID;
    @Column(nullable = false,
            columnDefinition = "DATETIME NOT NULL " +
                    "DEFAULT CURRENT_TIMESTAMP",
            updatable = false,
            insertable = false)
    private LocalDate insertTime;
    @Column(nullable = false,
            columnDefinition = "DATETIME NOT NULL " +
                    "DEFAULT CURRENT_TIMESTAMP ON UPDATE " +
                    "CURRENT_TIMESTAMP",
            updatable = false,
            insertable = false)
    private LocalDate updateTime;
    @Column(nullable = false)
    private LocalDateTime invigilationBeginTime;
    @Column(nullable = false)
    private String courseName;
    @Column(nullable = false)
    private int numberOfTeacher;
}
