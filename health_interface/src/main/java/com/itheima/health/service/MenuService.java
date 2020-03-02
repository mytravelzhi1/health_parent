package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Menu;

import java.util.List;

public interface MenuService {
    /**
     * 查询所有菜单
     * @return
     */
    List<Menu> findAll();

    /**
     * 分页查询菜单
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     *
     * @param menu
     */
    void add(Menu menu);

    /**
     *
     * @param id
     * @return
     */
    Menu findById(Integer id);

    /**
     * 编辑保存菜单
     * @param menu
     */
    void edit(Menu menu);

    /**
     * 删除菜单
     * @param id
     */
    void delete(Integer id);


    List<Menu> findMenus(String username);
}
