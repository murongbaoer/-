package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping(value = "/setmeal")
public class SetmealMobilController {
    @Autowired
    JedisPool jedisPool;
    @Reference
    SetmealService setmealService;

    // 查询所有套餐
   @RequestMapping(value = "/getSetmeal")
    public Result getSetmeal(){
       try {   Jedis jedis = jedisPool.getResource();
           String setmealId = jedis.get("setmealId");
           if (setmealId!=null){
               System.out.println("加载了redis缓存");
               JSONArray list = JSONArray.parseArray(setmealId);
               return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,list);
           }
           List<Setmeal> list = setmealService.findAll();
           list.forEach(e -> {
               e.setImg(QiniuUtils.DOMAIN + e.getImg());
           });
           String string = JSON.toJSONString(list);
           System.out.println("设置了redis缓存");
           jedis.set("setmealId",string);
           return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
       } catch (Exception e) {
           e.printStackTrace();
           return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
       }
   }
    // 使用套餐id，查询套餐详情
   @RequestMapping(value = "findById")
    public Result findById(Integer id){ Jedis jedis = jedisPool.getResource();
       String detail = jedis.hget("detail",id+" ");
       if (detail!=null){
           System.out.println("加载了redis缓存");
           Setmeal  setmeal = JSON.parseObject(detail,Setmeal.class);
           return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);

       }
      Setmeal setmeal =  setmealService.findById(id);
       setmeal.setImg(QiniuUtils.DOMAIN+setmeal.getImg());
       String string = JSON.toJSONString(setmeal);
       jedis.hset("detail",id+" ",string);
       System.out.println("设置了redis缓存");
       return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
   }
}
