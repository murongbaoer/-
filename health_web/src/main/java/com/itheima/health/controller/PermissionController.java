package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionServcie;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/permission")
public class PermissionController {

    @Reference
    PermissionServcie permissionServcie;
    // 从SpringSecurity中获取用户信息
    @RequestMapping(value ="/findAll")
    public Result findAll() {
        List<Permission> list = permissionServcie.findAll();
        // 此时存在数据
        if (list!=null && list.size()>0){
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,list);
            // 此时不存在数据
        }else {
            return new Result(true,MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }
    //新增检查项
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Permission permission) {
        try {
            permissionServcie.add(permission);
            //新增成功
            return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //新增失败
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
    }
    // 分页查询检查项
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = permissionServcie.findPage(queryPageBean.getCurrentPage()
                ,queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }
    @RequestMapping(value = "/delete")
    public Result delete(Integer id){
        try {
            permissionServcie.delete(id);
            //删除成功
            return new Result(true,MessageConstant.DELETE_PERMISSION_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }
    // ID查询权限
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        Permission permission = permissionServcie.findById(id);
        // 此时存在数据
        if (permission!=null){
            return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
        }else {
            // 此时不存在数据
            return new Result(false,MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }
    // 编辑检查项
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody Permission permission){
        try {
            permissionServcie.edit(permission);
            return new Result(true,MessageConstant.EDIT_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_PERMISSION_FAIL);
        }
    }
}
