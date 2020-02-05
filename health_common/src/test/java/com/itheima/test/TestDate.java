package com.itheima.test;

import com.itheima.health.utils.DateUtils;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @ClassName TestPassword
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/1/11 12:00
 * @Version V1.0
 */
public class TestDate {

    /**
     日期测试类：
     获取过去1年的日期统计
     */
    @Test
    public void test(){
        List<String> months = new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MONTH,-12); // 过去1年（2019-02，2019-03....2020-01)
        for (int i = 0; i < 12; i++) {
            instance.add(Calendar.MONTH,1);
            months.add(new SimpleDateFormat("yyyy-MM").format(instance.getTime()));
        }
        // 输出
        if(months!=null && months.size()>0){
            for (String month : months) {
                System.out.println(month);
            }
        }
    }


    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    public static String getLastDayOfMonth(String yearMonth) {
        int year = Integer.parseInt(yearMonth.split("-")[0]);  //年
        int month = Integer.parseInt(yearMonth.split("-")[1]); //月
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        // cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.MONTH, month); //设置当前月的上一个月
        // 获取某月最大天数
        //int lastDay = cal.getActualMaximum(Calendar.DATE);
        int lastDay = cal.getMinimum(Calendar.DATE); //获取月份中的最小值，即第一天
        // 设置日历中月份的最大天数
        //cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1); //上月的第一天减去1就是当月的最后一天
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }

    public static void main(String[] args) {
        String date = getLastDayOfMonth(2020, 2);
        System.out.println(date);
    }
    @Test
    public void test01() {

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
            System.out.println("今天:"+today+"号");
            System.out.println("这周一:"+monday+"号");
            System.out.println("这周日:"+sunday+"号");
            System.out.println("本月第一天:"+firstMonthDay+"号");
            System.out.println("本月最后一天:"+lastMonthDay+"号");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
