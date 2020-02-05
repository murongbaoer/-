package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MainServcie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/main")
public class MainController {

    @Reference
    MainServcie mainServcie;
    @RequestMapping(value = "/getListMenu")
    public Result user(){
        //拿名字调用业务查
        try {
            org.springframework.security.core.userdetails.User user =
                    (org.springframework.security.core.userdetails.User)
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            List<Menu> list = mainServcie.findMenu(user.getUsername());
            //查询到多个角色
            return new  Result(true, MessageConstant.QUERY_MENU_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new  Result(false,MessageConstant.QUERY_MENU_FAIL);
        }
    }

    }

