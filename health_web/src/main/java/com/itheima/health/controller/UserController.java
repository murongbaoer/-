package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.service.UserServcie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Reference
    UserServcie userServcie;
    // 从SpringSecurity中获取用户信息
    @RequestMapping(value ="/getUsername")
    public Result getUsername(){
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
          com.itheima.health.pojo.User u =  userServcie.findUserByUsername(user.getUsername());
          return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername()+"("+u.getGender()+")");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }
    @RequestMapping(value = "/add")
    public Result add(@RequestBody com.itheima.health.pojo.User user, Integer[] checkroleIds) {
        try {
            userServcie.add(user, checkroleIds);
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
    }
        @RequestMapping(value = "/findPage")
        public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
            PageResult pageResult  = userServcie.findPage(queryPageBean.getCurrentPage(),
                    queryPageBean.getPageSize(),
                    queryPageBean.getQueryString()
            );
            return pageResult;
        }
        @RequestMapping(value = "findById")
        public Result findById(Integer id){
            try {
               com.itheima.health.pojo.User user =  userServcie.findById(id);
                return new Result(true,MessageConstant.QUERY_USER_SUCCESS,user);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,MessageConstant.QUERY_USER_FAIL);
            }
        }
        @RequestMapping(value = "findCheckroleIdsByUserId")
        public List<Integer> findCheckroleIdsByUserId(Integer id){
            List<Integer> userList=userServcie.findCheckroleIdsByUserId(id);
            return userList;

        }
        @RequestMapping(value = "/edit")
        public Result edit(@RequestBody com.itheima.health.pojo.User user, Integer[] checkroleIds){
            try {
                userServcie.edit(user,checkroleIds);
                return new Result(true,MessageConstant.EDIT_USER_SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false,MessageConstant.EDIT_USER_FAIL);
            }
        }
        @RequestMapping(value = "/delete")
        public Result delete(Integer id){
            try {
                userServcie.delete(id);
                //删除成功
                return new Result(true,MessageConstant.DELETE_USER_SUCCESS);
            } catch (RuntimeException e) {
                e.printStackTrace();
                return new Result(false, e.getMessage());
            }catch (Exception e) {
                e.printStackTrace();
                return new Result(false, MessageConstant.DELETE_USER_FAIL);
            }
        }
    }

