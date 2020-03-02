package com.itheima.health.dao;

import com.github.pagehelper.Page;
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

    /**
     * 分页查询菜单
     * @param queryString
     * @return
     */
    Page<Menu> findPage(String queryString);

    /**
     * 新增菜单
     * @param menu
     */
    void add(Menu menu);

    /**
     * 根据menuId查回显数据
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
     * 根据menuId查询和角色是否有关联
     * @param id
     * @return
     */
    long findMenuAndRoleCountByMenuId(Integer id);

    /**
     * 删除菜单
     * @param id
     */
    void delete(Integer id);
}
