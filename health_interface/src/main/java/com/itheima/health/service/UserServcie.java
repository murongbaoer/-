package com.itheima.health.service;


import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.User;

import java.util.List;

public interface UserServcie {

    User findUserByUsername(String username);

    void add(User user, Integer[] checkroleIds);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    User findById(Integer id);

    List<Integer> findCheckroleIdsByUserId(Integer id);

    void edit(User user, Integer[] checkroleIds);

    void delete(Integer id);
}
