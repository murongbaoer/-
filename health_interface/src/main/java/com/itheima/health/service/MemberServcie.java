package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MemberServcie {
    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMoth(List<String> months);

    Map<String,List<Object>> findMemberCountByMonthDate2(Date startDate, Date endDate);

    Map<String,List<Object>> findMemberCountByMonthDate(Date startDate, Date endDate);

    Map<String,Object> findMemberGender();

    Map<String,Object> findMemberAge();

}
