package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;


public interface CheckItemDao {

   // @Insert("insert into t_checkitem(id,code,name,sex,age,price,type,remark,attention) value(1,'aaa','bbb','0','0-100',10f,'2','无','无')")
    void add(CheckItem checkItem);

    Page<CheckItem> findPage1(String queryString);

    Integer findCheckGroupAndCheckItemCountByCheckItemId(Integer id);

    void delete(Integer id);

    CheckItem findById(Integer id);

   void edit(CheckItem checkItem);

    List<CheckItem> findAll();


    List<CheckItem> findCheckItemsByCheckGroupId(Integer checkGroupId);
}
