package com.dbms.placementapp.models.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPayload {
    private String name;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String retypePassword;
}
