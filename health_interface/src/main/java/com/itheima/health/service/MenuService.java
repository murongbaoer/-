package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Menu;

public interface MenuService {
    void add(Menu menu);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    Menu findById(Integer id);

    void edit(Menu menu);

    void delete(Integer id);
}
