package com.itheima.health.service;


import com.itheima.health.pojo.Menu;

import java.util.List;

public interface MainServcie {


    List<Menu> findMenu(String username);

}
