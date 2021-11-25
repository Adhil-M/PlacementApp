package com.dbms.placementapp.models.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private Acknowledgement acknowledgement;
    private UserDetail userDetail;
}
