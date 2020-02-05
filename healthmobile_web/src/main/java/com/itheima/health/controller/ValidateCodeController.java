package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.RedisMessageConstant;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping(value = "/validateCode")
public class ValidateCodeController {
    @Autowired
    JedisPool jedisPool;
    // 体检预约订单业务，【发送验证码】
    @RequestMapping(value = "/send4Order")
    public Result send4Order(String telephone){
        try {
            // 1：获取手机号
            // 2：生成4位验证码
            String code4 = ValidateCodeUtils.generateValidateCode(4).toString();
            System.out.println("手机接收的验证码："+code4);
            // 3：使用阿里云短信服务，给当前手机号发送验证码
            // SMSUtils.sendShortMessage(telephone,code4);
            // key：手机号+常量001                 value：验证码
            jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_ORDER,5*60,code4 );
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
    @RequestMapping(value = "/send4Login")
    public Result send4Login(String telephone){
        try {
            // 1：获取手机号
            // 2：生成6位验证码
            String code6 = ValidateCodeUtils.generateValidateCode(6).toString();
            System.out.println(code6);
            // 3：使用阿里云短信服务，给当前手机号发送验证码
            // SMSUtils.sendShortMessage(telephone,code6);
            // key：手机号+常量002                 value：验证码
            jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,5*60,code6);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
