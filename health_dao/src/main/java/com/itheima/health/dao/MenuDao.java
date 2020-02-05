package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Menu;

public interface MenuDao {

    void add(Menu menu);

    Page<Menu> findPage(String queryString);

    Menu findById(Integer id);

    void edit(Menu menu);

    Integer findRoleAndMenuCountByMenuId(Integer id);

    void delete(Integer id);


}
