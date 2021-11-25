package com.dbms.placementapp.models.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusPayload {
    private String status;
    private String message;
    private String recruiterName;
    private String roleName;
}
