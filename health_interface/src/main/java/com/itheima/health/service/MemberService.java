package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {

    Member findMemberByTelephone(String telepone);

    void add(Member member);

    List<Integer> findCountByBeforeRegTime(List<String> months);

    List<Integer> findMemberCountByList(List<String> list, String mode);

    List<Map> findSexCountByMemberSex();

    List<Map> findAgeCountByMemberAge();
}
