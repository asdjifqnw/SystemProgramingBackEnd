package com.systemprograming.keshe.other;

import com.systemprograming.keshe.dao.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.time.LocalDate;

@ToString
@Setter
@Getter
public class UserWithAllocated {
    private Integer userID;
    private String userName;
    private String position;
    private String introduction;
    private String phoneNumber;
    private boolean isAdmin;
    private boolean isSuperAdmin;
    private LocalDate insertTime;
    private LocalDate updateTime;
}
