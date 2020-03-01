package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ReportServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:03
 * @Version V1.0
 */
@Service // dubbo提供
@Transactional
public class ReportServiceImpl implements ReportService {

    // 订单相关
    @Autowired
    OrderDao orderDao;

    // 会员相关
    @Autowired
    MemberDao memberDao;

    @Override
    public Map<String, Object> findBusinessReportData() {
        Map<String,Object> map = new HashMap<>();
        try {
            // 当前时间
            String date = DateUtils.parseDate2String(DateUtils.getToday());
            // 计算本周的周一
            String mondayOfWeek = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            // 计算本周的周日
            String sundayOfWeek = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
            // 计算本月的1号
            String firstDatyOfMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            // 计算本月的最后1天
            String lastDatyOfMonth = DateUtils.parseDate2String(DateUtils.getLastDay4ThisMonth());

            /**会员相关统计数据*/
            // 今日新增会员数
            Integer todayNewMember = memberDao.findTodayNewMember(date);
            // 总会员数
            Integer totalMember = memberDao.findTotalMember();
            // 本周新增会员数
            Integer thisWeekNewMember = memberDao.findThisWeekAndMonthNewMember(mondayOfWeek);
            // 本月新增会员数
            Integer thisMonthNewMember = memberDao.findThisWeekAndMonthNewMember(firstDatyOfMonth);

            /**预约订单相关统计数据*/
            // 今日预约数
            Integer todayOrderNumber = orderDao.findTodayOrderNumber(date);
            // 今日到诊数
            Integer todayVisitsNumber = orderDao.findTodayVisitsNumber(date);
            // 周一到周日的参数
            Map weekMap = new HashMap();
            weekMap.put("begin",mondayOfWeek);
            weekMap.put("end",sundayOfWeek);

            // 1号到最后1号的参数
            Map monthMap = new HashMap();
            monthMap.put("begin",firstDatyOfMonth);
            monthMap.put("end",lastDatyOfMonth);

            // 本周预约数
            Integer thisWeekOrderNumber = orderDao.findThisWeekAndMonthOrderNumber(weekMap);
            // 本周到诊数
            Integer thisWeekVisitsNumber = orderDao.findThisWeekAndMonthVisitsNumber(weekMap);
            // 本月预约数
            Integer thisMonthOrderNumber = orderDao.findThisWeekAndMonthOrderNumber(monthMap);
            // 本月到诊数
            Integer thisMonthVisitsNumber = orderDao.findThisWeekAndMonthVisitsNumber(monthMap);

            // 热门套餐
            List<Map> hotSetmeal = orderDao.findHotSetmeal();

            map.put("reportDate",date);  //String  ：存放当前时间
            map.put("todayNewMember",todayNewMember);  //Integer
            map.put("totalMember",totalMember);  //Integer
            map.put("thisWeekNewMember",thisWeekNewMember);  //Integer
            map.put("thisMonthNewMember",thisMonthNewMember);  //Integer
            map.put("todayOrderNumber",todayOrderNumber);  //Integer
            map.put("todayVisitsNumber",todayVisitsNumber);  //Integer
            map.put("thisWeekOrderNumber",thisWeekOrderNumber);  //Integer
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);  //Integer
            map.put("thisMonthOrderNumber",thisMonthOrderNumber);  //Integer
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);  //Integer
            map.put("hotSetmeal",hotSetmeal);  //List<Map>
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("抛出运行时异常...");
        }

        return map;
    }
}
