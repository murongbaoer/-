package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/role")
public class RoleController {
    @Reference
    RoleService roleService;

    // 分页查询检查项
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = roleService.findPage(queryPageBean.getCurrentPage()
        ,queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Role role, Integer[] permissionIds) {
        try {
            roleService.add(role, permissionIds);
            return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
    }
    @RequestMapping(value = "findPermissionIdsByRoleId")
    public List<Integer> findPermissionIdsByRoleId(Integer id){
        List<Integer> permissionIds=roleService.findPermissionIdsByRoleId(id);
        return permissionIds;
    }
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody Role role,Integer[] permissionIds){
        try {
            roleService.edit(role,permissionIds);
            return new Result(true,MessageConstant.EDIT_ROLE_SUCCESS );
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ROLE_FAIL);
        }
    }
    @RequestMapping(value = "/findById")
    public Result findById(Integer id) {
        try {
            Role role = roleService.findById(id);
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }
    @RequestMapping(value = "/delete")
    public Result delete(Integer id){
        try {
            roleService.delete(id);
            //删除成功
            return new Result(true,MessageConstant.DELETE_ROLE_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_ROLE_FAIL);
        }
    }
    // 查询所有角色
    @RequestMapping(value = "findAll")
    public Result findAll(){
        List<Role> list = roleService.findAll();
        // 此时存在数据
        if (list!=null && list.size()>0){
            return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,list);
            // 此时不存在数据
        }else {
            return new Result(true,MessageConstant.QUERY_ROLE_FAIL);
        }
    }
}
