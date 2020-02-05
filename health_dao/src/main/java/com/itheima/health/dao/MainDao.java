package com.itheima.health.dao;


import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;

import java.util.List;

public interface MainDao {

    User findRoleAndMenu(String username);

    Role findRoleByUser(Integer id);

    List<Menu> findMenuByRole(Integer id);
}
