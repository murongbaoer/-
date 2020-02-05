package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/checkgroup")
public class CheckGroupController {
    @Reference
    CheckGroupService checkGroupService;
    @RequestMapping(value = "/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.add(checkGroup,checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       PageResult pageResult  = checkGroupService.findPage(queryPageBean.getCurrentPage(),
                                                queryPageBean.getPageSize(),
                                                   queryPageBean.getQueryString()
       );
        return pageResult;
    }
    @RequestMapping(value = "findById")
    public Result findById(Integer id){
        try {
          CheckGroup checkGroup =  checkGroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
    @RequestMapping(value = "findCheckItemIdsByCheckGroupId")
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id){
            List<Integer> checkGroupList=checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return checkGroupList;

    }
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){
        try {
            checkGroupService.edit(checkGroup,checkitemIds);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }
    @RequestMapping(value = "/delete")
    public Result delete(Integer id){
        try {
            checkGroupService.delete(id);
            //删除成功
            return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }
    @RequestMapping(value = "findAll")
    public Result findAll(){

            List<CheckGroup> checkGroupList =  checkGroupService.findAll();
            if (checkGroupList!=null && checkGroupList.size()>0 ){
                return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupList);
            }else {
                return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
            }
    }
}
