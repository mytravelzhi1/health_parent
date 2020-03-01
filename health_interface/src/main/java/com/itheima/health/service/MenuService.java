package com.itheima.health.service;

import com.itheima.health.pojo.Menu;

import java.util.List;

public interface MenuService {
    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> findAll();

}
