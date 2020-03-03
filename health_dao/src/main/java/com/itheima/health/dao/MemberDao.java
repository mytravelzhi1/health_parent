package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberDao {

    Member findMemberByTelephone(String telepone);

    void add(Member member);

    Integer findCountByBeforeRegTime(String regTime);

    Integer findTodayNewMember(String date);

    Integer findTotalMember();

    Integer findThisWeekAndMonthNewMember(String date);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       

    Integer findMemberCountByRegTime(String regTime);

    Integer findMemberCountByDays(String day);

    List<Map> findSexCountByMemberSex();

    List<Map> findAgeCountByMemberAge();

}
