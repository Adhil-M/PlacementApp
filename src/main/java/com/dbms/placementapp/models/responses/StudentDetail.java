package com.dbms.placementapp.models.responses;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDetail {
    private Integer userId;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String regNo;
    private LocalDate dob;
    private Double cgpa;
    private String placementStatus;
    private String message;
}
