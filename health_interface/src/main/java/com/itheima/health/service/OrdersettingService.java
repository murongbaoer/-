package com.itheima.health.service;

import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrdersettingService {

    void addList(List<OrderSetting> orderSettingList);

  List<Map> getOrderSettingByMonth(String date);

    void updateNumberByDate(OrderSetting orderSetting);
}
