package com.example.memorip.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity // DB가 해당 객체 인식 가능! (해당 클래스로 테이블을 만든다)
@AllArgsConstructor
@NoArgsConstructor // 디폴트 생성자 추가
@ToString
@Getter
public class Users {
    @Id // 대표값 지정! like a 주민등록번호
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 1,2,3,... 자동 생성 어노테이션
    private int user_id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String nick_name;

    @Column
    private String profile;

    @Column
    private int role;

    @Column
    private String created_at;

}
