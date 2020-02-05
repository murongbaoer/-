package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrdersettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrdersettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = OrdersettingService.class)// interfaceClass目的也是为了解决事务问题
@Transactional
public class OrdersettingServiceImpl implements OrdersettingService {
    @Autowired
    OrdersettingDao ordersettingDao;


    @Override
    public void addList(List<OrderSetting> orderSettingList) {
        // 遍历，单条导入
        if(orderSettingList!=null && orderSettingList.size()>0){
            for (OrderSetting orderSetting : orderSettingList) {
                // 1：使用预约设置时间，判断当前预约时间在数据库中是否存在记录
                Long count = ordersettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if(count>0){
                    // 2：如果存在记录，使用预约设置时间执行更新最多预约人数的操作
                    ordersettingDao.updateNumberByOrderDate(orderSetting);
                }
                else{
                    // 3：如果不存在记录，保存预约设置
                    ordersettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        // 使用当前月的初始时间和当前月的结束时间，查询当前月的数据
        String beginDate = date+"-1";
        String endDate = date+"-31";
        Map map = new HashMap();
        map.put("beginDate",beginDate);
        map.put("endDate",endDate);
        List<OrderSetting> list = ordersettingDao.getOrderSettingByMonth(map);
        // 将List<OrderSetting>转换成List<Map>
        List<Map> returnList = new ArrayList<>();
        if(list!=null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                Map returnMap = new HashMap();
            /*    *
                 * orderSetting.getOrderDate().getDate()：返回天
                 * orderSetting.getOrderDate().getDay()：返回周
                 * orderSetting.getOrderDate().getYear()：返回年
                 * orderSetting.getOrderDate().getMonth()：返回月，从0开始
*/                returnMap.put("date",orderSetting.getOrderDate().getDate());
                returnMap.put("number",orderSetting.getNumber());
                returnMap.put("reservations",orderSetting.getReservations());
                returnList.add(returnMap);
            }
        }
        return returnList;
    }

    @Override
    public void updateNumberByDate(OrderSetting orderSetting) {
        // 1：使用预约设置时间，判断当前预约时间在数据库中是否存在记录
        Long count = ordersettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if(count>0){
            // 2：如果存在记录，使用预约设置时间执行更新最多预约人数的操作
            ordersettingDao.updateNumberByOrderDate(orderSetting);
        }
        else{
            // 3：如果不存在记录，保存预约设置
            ordersettingDao.add(orderSetting);
        }
    }
    }

