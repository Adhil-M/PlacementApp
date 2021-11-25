package com.dbms.placementapp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usercredentials")
public class UserCreds {
    @Id
    @Column(name = "user_id")
    private Integer userId;
    private String username;
    private String password;
}
