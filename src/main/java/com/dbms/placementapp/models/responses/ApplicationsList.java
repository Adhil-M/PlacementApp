package com.dbms.placementapp.models.responses;

import java.util.List;

import com.dbms.placementapp.models.AppliedFor;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationsList {
    private Integer applicationsCount;
    private List<AppliedFor> applications;
}
