package com.dbms.placementapp.models.responses;

import com.dbms.placementapp.models.Student;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StudentDetailResponse {
    private Acknowledgement acknowledgement;
    private Student studentDetail;
}
