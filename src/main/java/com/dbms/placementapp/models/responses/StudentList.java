package com.dbms.placementapp.models.responses;

import java.util.List;

import com.dbms.placementapp.models.Student;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentList {
    private Integer studentsCount;
    List<StudentDetail> studentDetail;
}
