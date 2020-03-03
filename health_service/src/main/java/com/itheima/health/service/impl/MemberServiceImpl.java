package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import com.itheima.health.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName MemberServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:03
 * @Version V1.0
 */
@Service // dubbo提供
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberDao memberDao;

    @Override
    public Member findMemberByTelephone(String telepone) {
        Member member = memberDao.findMemberByTelephone(telepone);
        return member;
    }

    @Override
    public void add(Member member) {
        // 判断密码是否为空（密码为空：使用手机快速登录注册；密码不为空：使用用户登录注册的应用场景）
        if(member!=null && member.getPassword()!=null){
            member.setPassword(MD5Utils.md5(member.getPassword())); // 将明文的密码使用md5进行加密
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findCountByBeforeRegTime(List<String> months) {
        List<Integer> memberCount  = new ArrayList<>();
        // 遍历
        for (String month : months) {
            // 方案一：使用Calader的日历类，计算每个月的最后1天，添加到后面（回去按照讲的方法，查）
            // 方案二：补-31
            String regTime = month+"-31";
            Integer count = memberDao.findCountByBeforeRegTime(regTime);
            memberCount.add(count);
        }
        return memberCount;
    }

    @Override
    public List<Integer> findMemberCountByList(List<String> list, String mode) {
        if (mode.equals("month")){
            // 组织查询结果
            List<Integer> memberCountList = new ArrayList<>();
            for (String month : list) {
                String regTime = month+"-31"; // 已知当前月，查询当前月的最后1天
                Integer memberCount = memberDao.findMemberCountByRegTime(regTime);
                memberCountList.add(memberCount);
            }
            return memberCountList;
        }
        if (mode.equals("day")){
            // 组织查询结果
            List<Integer> memberCountList = new ArrayList<>();
            for (String day : list)  {
                Integer memberCount = memberDao.findMemberCountByDays(day);
                memberCountList.add(memberCount);
            }
            return memberCountList;
        }

        if (mode.equals("week")){
            // 组织查询结果
            List<Integer> memberCountList = new ArrayList<>();
            for (String day : list)  {
                Integer memberCount = memberDao.findMemberCountByRegTime(day);
                memberCountList.add(memberCount);
            }
            return memberCountList;
        }

        if (mode.equals("year")){
            // 组织查询结果
            List<Integer> memberCountList = new ArrayList<>();
            for (String day : list)  {
                Integer memberCount = memberDao.findMemberCountByRegTime(day);
                memberCountList.add(memberCount);
            }
            return memberCountList;
        }
        return null;
    }

    @Override
    public List<Map> findSexCountByMemberSex() {

        List<Map> list =memberDao.findSexCountByMemberSex();
        return  list;
    }

    @Override
    public List<Map> findAgeCountByMemberAge() {
       return  memberDao.findAgeCountByMemberAge();

    }


}
