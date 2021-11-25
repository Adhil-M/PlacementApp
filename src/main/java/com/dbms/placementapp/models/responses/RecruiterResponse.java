package com.dbms.placementapp.models.responses;

import com.dbms.placementapp.models.Recruiter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruiterResponse {
    private Acknowledgement acknowledgement;
    private Recruiter recruiterDetail;
}
