package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberServcie;
import com.itheima.health.utils.DateUtils;
import com.itheima.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = MemberServcie.class)
@Transactional
public class MemberServiceImpl implements MemberServcie {
    @Autowired
    MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {

        return memberDao.findMemberByTelephon(telephone);
    }

    @Override
    public void add(Member member) {
        if (member.getPassword() != null) {
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMoth(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        if (months != null && months.size() > 0) {
            for (String month : months) {
                String data = month + "-31";
                System.out.println(data);
                Integer count = memberDao.findMemberCountByMoth(data);
                memberCount.add(count);
            }
        }
        System.out.println(memberCount);
        return memberCount;
    }

    /**
     * 获取指定时间内的会员数量统计,折线图
     * 查询的是每月底到起始时间的会员数
     * @param sDate 起始时间
     * @param eDate 结束时间
     * @return
     */
    @Override
    public Map<String, List<Object>> findMemberCountByMonthDate2(Date sDate, Date eDate) {
        try {
            String startDate = DateUtils.parseDate2String(sDate);
            String endDate = DateUtils.parseDate2String(eDate);
            //获得开始时间
            String[] startArrDate = startDate.split("-");//将日期切割成字符串数组
            //将字符串数组转换为Integer类型数组,包含年,月,日三个元素
            Integer[] startIntDate = stringToInt(startArrDate);
            //获得结束时间
            String[] endArrDate = endDate.split("-");
            Integer[] endIntDate = stringToInt(endArrDate);
            //获取指定时间内的每一天
            Calendar calendar = Calendar.getInstance();
            //存储每月会员数集合
            List<Object> memberCount = new ArrayList<>();
            List<Object> months = new ArrayList<>();//存储月份集合

            //获取起始时间的那个月的最后一天
            String startDateAfter = DateUtils.getDateOfThisMonthLast(startIntDate[0], startIntDate[1], startIntDate[2]);
            //调用dao查询起始时间那个月的会员数量
            Integer startCount = memberDao.findMemberCountMonthDate(startDate, startDateAfter);
            if (startCount == null) {
                //如果为空,赋值 0
                startCount = 0;
            }
            memberCount.add(startCount);//开始时间到月底的会员数

            //起始时间到结束时间中间月的会员数量
            List<String> monthBetween = DateUtils.getMonthBetween(startDate, endDate, "yyyy-MM");//获取中间的月份,返回值月份列表
            startDate = startDate.replace("-", ".");
            months.add(startDate);//开始时间

            //不包含起始时间和结束时间的月份
            String m = null;
            for (int i = 1; i < monthBetween.size() - 1; i++) {
                m = monthBetween.get(i) + "-31";
                Integer countBetween = memberDao.findMemberCountMonthDate(startDate, m);//查询每月(到起始时间)的会员数
                memberCount.add(countBetween);//中间月的会员数量
                m = m.substring(0, 7);
                months.add(m);//中间月
            }
            //调用dao查询结束时间那个月开始到结束时间的会员数量
            Integer endCount = memberDao.findMemberCountMonthDate(startDate, endDate);//查询起始时间到结束时间的会员数
            if (endCount == null) {
                //如果为空,赋值 0
                endCount = 0;
            }
            memberCount.add(endCount);//结束时间到当月的月初的会员
            endDate = endDate.replace("-", ".");
            months.add(endDate);//结束日期

            Map<String, List<Object>> map = new HashMap<>();
            map.put("memberCount", memberCount);//每月的会员数量
            map.put("months", months);//月份
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, List<Object>> findMemberCountByMonthDate(Date sDate, Date eDate) {
        try {
            String startDate = DateUtils.parseDate2String(sDate);
            String endDate = DateUtils.parseDate2String(eDate);
            //获得开始时间
            String[] startArrDate = startDate.split("-");//将日期切割成字符串数组
            //将字符串数组转换为Integer类型数组,包含年,月,日三个元素
            Integer[] startIntDate = stringToInt(startArrDate);
            //获得结束时间
            String[] endArrDate = endDate.split("-");
            Integer[] endIntDate = stringToInt(endArrDate);
            //获取指定时间内的每一天
            Calendar calendar = Calendar.getInstance();
            //存储每月会员数集合
            List<Object> memberCount = new ArrayList<>();
            List<Object> months = new ArrayList<>();//存储月份集合

            //获取起始时间的那个月的最后一天
            String startDateAfter = DateUtils.getDateOfThisMonthLast(startIntDate[0], startIntDate[1], startIntDate[2]);
            //调用dao查询起始时间那个月的会员数量
            Integer startCount = memberDao.findMemberCountMonthDate(startDate, startDateAfter);
            if (startCount == null) {
                //如果为空,赋值 0
                startCount = 0;
            }
            memberCount.add(startCount);//开始时间到月底的会员数

            //起始时间到结束时间中间月的会员数量
            List<String> monthBetween = DateUtils.getMonthBetween(startDate, endDate, "yyyy-MM");//获取中间的月份,返回值月份列表
            startDate = startDate.replace("-", ".");
            months.add(startDate);//开始时间

            //不包含起始时间和结束时间的月份
            String m1 = null;
            String m2 = null;
            for (int i = 1; i < monthBetween.size() - 1; i++) {
                m1=  monthBetween.get(i) + "-01";
                m2 = monthBetween.get(i) + "-31";
                Integer countBetween = memberDao.findMemberCountMonthDate(m1,m2);//查询每月的会员数
                memberCount.add(countBetween);//中间月的会员数量
                m2 = m2.substring(0, 7);
                months.add(m2);//中间月
            }

            //获取结束时间的那个月的第一天
            String endDateBefore = DateUtils.getDateOfThisMonthFirst(endIntDate[0], endIntDate[1], endIntDate[2]);
            //调用dao查询结束时间那个月开始到结束时间的会员数量
            Integer endCount = memberDao.findMemberCountMonthDate(endDateBefore, endDate);
            if (endCount == null) {
                //如果为空,赋值 0
                endCount = 0;
            }
            memberCount.add(endCount);//结束时间到当月的月初的会员
            endDate = endDate.replace("-", ".");
            months.add(endDate);//结束日期

            Map<String, List<Object>> map = new HashMap<>();
            map.put("memberCount", memberCount);//每月的会员数量
            map.put("months", months);//月份
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取男女会员数量占比,饼形图
     * @return
     */
    @Override
    public Map<String, Object> findMemberGender() {
        Map<String,Object>map = new HashMap<>();
        //各个性别对应的会员数量
        List<Map<String,Object>>memberGenderCount = memberDao.findMemberGender();//获取男女及其它会员数量
        List<String> memberGender = new ArrayList<>();//存储性别
        for (Map<String, Object> member : memberGenderCount) {//遍历,获取性别 存储到 性别集合
            String sex = (String) member.get("name");//取出性别
            memberGender.add(sex);//添加到性别集合
        }
        map.put("MemberGender", memberGender);
        map.put("MemberCount", memberGenderCount);//各个性别及其对应的会员数量
        return map;
    }

    @Override
    public Map<String, Object> findMemberAge() {
        Map<String, Object> map = new HashMap<>();

        List<String> listAge = new ArrayList<>();//存储年龄段集合
        //存储各个年龄段和年龄段的数量,查询dao
        List<Map<String, Object>> memberAgeCount =  memberDao.findMemberAge();
        for (Map<String, Object> m : memberAgeCount) {
            String name = (String) m.get("name");//获取年龄段
            listAge.add(name);//添加到年龄段集合
        }
        map.put("MemberAge", listAge);//年龄段集合
        map.put("MemberCount", memberAgeCount);//年龄段和年龄数量集合
        return map;
    }

    public Integer[] stringToInt(String[] str) {
        Integer[] integers = new Integer[3];
        integers[0] = Integer.parseInt(str[0]);
        integers[1] = Integer.parseInt(str[1]);
        integers[2] = Integer.parseInt(str[2]);
        return integers;
    }

}
