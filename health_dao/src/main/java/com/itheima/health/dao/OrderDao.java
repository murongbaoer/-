package com.itheima.health.dao;


import com.itheima.health.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    List<Order> findOrderListByConidtion(Order order);

    void add(Order order);

    Map findById(Integer id);

    Integer findTodayOrderNumber(String today);

    Integer findTodayVisitsNumber(String today);

    Integer findOrderNumberBetweenDate(Map weekMap);

    Integer findVisitsNumberBetweenDate(Map weekMap);

    List<Map> findhotSetmeal();

}
