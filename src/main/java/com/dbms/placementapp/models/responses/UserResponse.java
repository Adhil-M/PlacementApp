package com.dbms.placementapp.models.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Acknowledgement acknowledgement;
    private StudentDetail details;
}
