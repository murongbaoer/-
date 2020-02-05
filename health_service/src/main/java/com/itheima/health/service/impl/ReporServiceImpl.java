package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrdersettingDao;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.MemberServcie;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = ReportService.class)// interfaceClass目的也是为了解决事务问题
@Transactional
public class ReporServiceImpl implements ReportService {
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() {
        try {
            //获取今天日期
            String today = DateUtils.parseDate2String(DateUtils.getToday());
            //获取本周对应的周一
            String monday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            //获取本周对应的周日
            String sunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            //获取本月的第一天
            String firstMonthDay = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            //获取本月最后一天
            String lastMonthDay = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());
            // 今日新增会员数
             Integer todayNewMember = memberDao.findTodayNewMember(today);
            // 总会员数
            Integer totalMember = memberDao.findTotalMember();
            // 本周新增会员数
            Integer thisWeekNewMember = memberDao.findNewMemberAfterDate(monday);
            // 本月新增会员数
            Integer thisMonthNewMember = memberDao.findNewMemberAfterDate(firstMonthDay);
            // 今天预约数
            Integer todayOrderNumber = orderDao.findTodayOrderNumber(today);
            // 今天到诊数
            Integer todayVisitsNumber =orderDao.findTodayVisitsNumber(today);
            // 本周预约数
            Map weekMap = new HashMap();
            weekMap.put("begin", monday);
            weekMap.put("end",sunday);
            Integer thisWeekOrderNumber = orderDao.findOrderNumberBetweenDate(weekMap);
            // 本周到诊数
            Integer thisWeekVisitsNumber = orderDao.findVisitsNumberBetweenDate(weekMap);
            // 本月预约数
            Map mothMap = new HashMap();
            mothMap.put("begin",firstMonthDay);
            mothMap.put("end",lastMonthDay);
            Integer thisMonthOrderNumber = orderDao.findOrderNumberBetweenDate(mothMap);
            // 本月到诊数
            Integer thisMonthVisitsNumber = orderDao.findVisitsNumberBetweenDate(mothMap);

            List<Map> hotSetmeal= orderDao.findhotSetmeal();

            Map<String,Object> map = new HashMap<>();
            map.put("reportDate",today);  // 字符串
            map.put("todayNewMember",todayNewMember ); // 整型（Integer，Long）
            map.put("totalMember",totalMember );
            map.put("thisWeekNewMember",thisWeekNewMember );
            map.put("thisMonthNewMember",thisMonthNewMember );
            map.put("todayOrderNumber",todayOrderNumber );
            map.put("todayVisitsNumber",todayVisitsNumber );
            map.put("thisWeekOrderNumber",thisWeekOrderNumber );
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber );
            map.put("thisMonthOrderNumber",thisMonthOrderNumber );
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber );
            map.put("hotSetmeal",hotSetmeal );// List<Map>
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
