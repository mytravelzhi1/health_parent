package com.itheima.health.dao;

import com.itheima.health.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {

    List<Order> findOrderListByCondition(Order order);

    void add(Order order);

    Map findById(Integer id);

    Integer findTodayOrderNumber(String date);

    Integer findTodayVisitsNumber(String date);

    Integer findThisWeekAndMonthOrderNumber(Map map);

    Integer findThisWeekAndMonthVisitsNumber(Map map);

    List<Map> findHotSetmeal();
}
