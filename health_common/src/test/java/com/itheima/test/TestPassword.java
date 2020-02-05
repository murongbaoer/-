package com.itheima.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {
    @Test
    public void test(){
    // encode：对密码进行加密，用在向数据库保存数据
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String passwordEncode = bCryptPasswordEncoder.encode("234");
        System.out.println(passwordEncode);
        String passwordEncode2 = bCryptPasswordEncoder.encode("234");
        System.out.println(passwordEncode2);
        //matches：使用页面登录的密码和数据库中的密码进行比对，返回是一个boolean类型，true：匹配成功，可以登录
        boolean matches = bCryptPasswordEncoder.matches("234", "$2a$10$czI3UZCQfckTKPCvwuB4duMxyFXdIJ431yK0UVQIorEozLW/IlWS.");
        System.out.println(matches);

    }
}
