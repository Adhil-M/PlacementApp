package com.dbms.placementapp.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

public class AppliedForId implements Serializable {
    private Integer recruiterId;
    private Integer studentId;

}
