package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/checkitem")
public class CheckItemController {
    @Reference
    CheckItemService checkItemService;
    //新增检查项
    @RequestMapping(value = "/add")
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
            //新增成功
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            //新增失败
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }
    // 分页查询检查项
    @RequestMapping(value = "/findPage")
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_QUERY')")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = checkItemService.findPage(queryPageBean.getCurrentPage()
        ,queryPageBean.getPageSize(),queryPageBean.getQueryString());
        return pageResult;
    }
    // 删除检查项
    @RequestMapping(value = "/delete")
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_DELETE')")
    public Result delete(Integer id){
        try {
            checkItemService.delete(id);
            //删除成功
            return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }
    // ID查询检查项
    // 增删改的时候，可以使用try...catch判断，查询的时候使用if...else
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
      CheckItem checkItem = checkItemService.findById(id);
        // 此时存在数据
    if (checkItem!=null){
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }else {
        // 此时不存在数据
        return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }
  }
    // 编辑检查项
  @RequestMapping(value = "/edit")
  @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_EDIT')")
    public Result edit(@RequestBody CheckItem checkItem){
      try {
          checkItemService.edit(checkItem);
          return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
      } catch (Exception e) {
          e.printStackTrace();
          return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
      }
  }
    // 查询所有检查项
  @RequestMapping(value = "findAll")
    public Result findAll(){
      List<CheckItem> list = checkItemService.findAll();
      // 此时存在数据
      if (list!=null && list.size()>0){
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
          // 此时不存在数据
      }else {
          return new Result(true,MessageConstant.QUERY_CHECKITEM_FAIL);
      }
    }
}
