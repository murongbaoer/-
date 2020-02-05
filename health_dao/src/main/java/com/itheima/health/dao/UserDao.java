package com.itheima.health.dao;


import com.github.pagehelper.Page;
import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    User findUserByUsername(String username);

    void add(User user);

    void addUserAndRole(@Param(value = "userId")Integer userid, @Param(value = "roleId")Integer checkroleId);

    Page<User> findPage(String queryString);

    User findById(Integer id);

    List<Integer> findCheckroleIdsByUserId(Integer id);

    void edit(User user);

    void deleteUserAndRoleByUserId(Integer id);

    Integer findUserAndRoleCountByRoleId(Integer id);

    void delete(Integer id);
}
