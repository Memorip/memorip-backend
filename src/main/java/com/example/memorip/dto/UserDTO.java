package com.example.memorip.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;


public class UserDTO {
    private int user_id;
    private String email;
    private String password;
    private String nick_name;
    private String profile;
    private  int role;
    private Date created_at;
}
