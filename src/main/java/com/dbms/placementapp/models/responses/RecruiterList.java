package com.dbms.placementapp.models.responses;

import java.util.List;

import com.dbms.placementapp.models.Recruiter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecruiterList {
    private Integer recruiterCount;
    private List<Recruiter> recruiterDetails;
}
