package com.itheima.health.dao;

import com.itheima.health.pojo.Menu;

import java.util.List;

/**
 * @ClassName MenuDao
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/25 14:22
 * @Version V1.0
 */
public interface MenuDao {
    /**
     * 查询多有菜单
     * @return
     */
    List<Menu> findAll();

}
