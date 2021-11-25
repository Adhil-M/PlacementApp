package com.dbms.placementapp.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "appliedfor")
// @IdClass(AppliedForId.class)
public class AppliedFor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "applicationid")
    private Integer applicationId;
    @Column(name = "student_id")
    private Integer studentId;
    @Column(name = "recruiter_id")
    private Integer recruiterId;
    @Column(name = "applieddate")
    private LocalDate appliedDate;
    private String status;
    private String message;
}
