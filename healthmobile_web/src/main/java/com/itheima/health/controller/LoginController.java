package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberServcie;
import com.itheima.health.utils.RedisMessageConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;


@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Reference
    MemberServcie memberServcie;

    @Autowired
    JedisPool jedisPool;

    //使用手机号和验证码登录
    @RequestMapping(value = "/check")
    public Result check(HttpServletResponse response, @RequestBody Map map){
        // 一：比对验证码
        // 1：从页面上获取手机号和验证码
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        // 2：使用手机号从Redis中获取验证码
        String redisValidateCode = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //3：使用页面输入的验证码和Redis中获取的验证码进行比对
        //* 比对不成功：提示【验证码输入有误】
        if (redisValidateCode==null &&!redisValidateCode.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else {
            //验证码输入正确
            //2：判断当前用户是否为会员
            Member member = memberServcie.findByTelephone(telephone);
            if (member==null){
                //当前用户不是会员，自动完成注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberServcie.add(member);
            }
            //3：:登录成功
            //写入Cookie，跟踪用户，用于分布式系统单点登录
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);//有效期30天（单位秒）
            response.addCookie(cookie);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }
    }
}
