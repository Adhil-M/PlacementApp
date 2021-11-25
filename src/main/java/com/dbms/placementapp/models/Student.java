package com.dbms.placementapp.models;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "student")
public class Student implements Serializable {
    @Id
    @Column(name = "student_Id")
    private Integer studentId;
    private LocalDate dob;
    private Double cgpa;
    @Column(name = "placementstatus")
    private String pStatus;
    @Column(name = "regno")
    private String regNo;
    private String message;
}
