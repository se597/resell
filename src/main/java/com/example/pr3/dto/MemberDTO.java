package com.example.pr3.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
    private int user_no;       // PK
    private String id;         // 아이디 (UNIQUE)
    private String pw;         // 비밀번호
    private String name;           // 이름
    private String zipcode;
    private String address;        // 주소
    private String address_detail;
    private String birth;          // 생년월일
    private String phone_number;   // 전화번호
    private String email;          // 이메일
    private String nickname;       // 닉네임
    private String rank;           // 권한 (default 'user')
}
