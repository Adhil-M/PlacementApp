package com.dbms.placementapp.models.payloads;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class AcadDetailPayload {
    private String regNo;
    private String dob;
    private Double cgpa;
}
