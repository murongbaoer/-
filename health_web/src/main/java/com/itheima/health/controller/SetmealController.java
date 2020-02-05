package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@RestController
@RequestMapping(value = "/setmeal")
public class SetmealController {
    @Reference
    SetmealService setmealService;
    @Autowired
    JedisPool jedisPool;
    @RequestMapping(value = "/upload")
    //获取对应上传文件的文件名
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile){
        //获取原始文件名
        try {
            //获取原始文件名
            String filename = imgFile.getOriginalFilename();
            //生成UUID方式上传
            String uuid = UUID.randomUUID().toString();
            String fix = filename.substring(filename.lastIndexOf("."));
            filename=uuid+fix;
            QiniuUtils.uploadqiniu(imgFile.getBytes(),filename);
            // 图片上传的时候，存放Redis，使用Set的方式存放，key值使用： setmealPicResource
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,filename);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[]  checkgroupIds){

        try {
            setmealService.add(setmeal,checkgroupIds);
            return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
    }
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        System.out.println("执行了web层");
        PageResult pageResult = setmealService.findPage(queryPageBean.getCurrentPage(),
                                                        queryPageBean.getPageSize(),
                                                        queryPageBean.getQueryString());
        return pageResult;
    }
}
