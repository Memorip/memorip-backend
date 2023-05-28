package com.example.memorip.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class UserDTO {

    private int id;
    private String email;
    private String password;
    private String nickname;
    private String profile;
    private int role;
    private Date created_at;
}
