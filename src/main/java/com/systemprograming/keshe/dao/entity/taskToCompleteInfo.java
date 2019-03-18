package com.systemprograming.keshe.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@ToString
public class taskToCompleteInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer taskId;
    @Column(nullable = false)
    private Integer userID;
    @Column(nullable = false)
    private Integer taskType;
    @Column(nullable = false)
    private String content;
    private String uploadFIleURL;
    private String IPAdress;
    @Column(nullable = false)
    private boolean isTimeOut;
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

}
