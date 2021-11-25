package com.dbms.placementapp.models.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetail {
    private Integer userId;
    private String username;
    private String name;
    private String email;
    private String phone;

}
