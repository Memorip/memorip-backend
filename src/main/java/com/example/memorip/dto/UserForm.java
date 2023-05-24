package com.example.memorip.dto;

import com.example.memorip.entity.Users;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class UserForm {
    private int id;
    private String email;
    private String password;
    private String nickname;
    private String profile;
    private int role;
    private String created_at;

    public Users toEntity(){
        return new Users(id,email,password,nickname,profile,role,created_at);
    }
}
