package com.dbms.placementapp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "isplaced")
public class IsPlaced {
    @Id
    @Column(name = "student_id")
    private Integer studentId;
    @Column(name = "recruiter_id")
    private Integer recruiterId;
}
