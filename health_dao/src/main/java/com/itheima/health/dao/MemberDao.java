package com.itheima.health.dao;

import com.itheima.health.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MemberDao {
    Member findMemberByTelephon(String telephon);

    void add(Member member);

    Integer findMemberCountByMoth(String data);

    Integer findTodayNewMember(String date);

    Integer findTotalMember();

    Integer findNewMemberAfterDate(String date);
        //查询指定时间内的会员数量
    Integer findMemberCountMonthDate(@Param("startDate")String startDate, @Param("endDate")String startDateAfter);
    /**
     * 获取男女会员数量占比
     * @return
     */
    List<Map<String,Object>> findMemberGender();
     /**根据年龄段获取会员数量统计, 饼形图
     *按照会员的年龄段（可以指定几个年龄段，例如0-18、18-30、30-45、45以上）来展示各个年龄段的占比
    @return*/
    List<Map<String,Object>> findMemberAge();

}
