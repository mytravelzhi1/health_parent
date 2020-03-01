package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.User;

import java.util.List;

public interface UserService {


    User findUserByUsername(String username);

    /**
     * 分页查询用户
     *
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 添加用户
     *
     * @param user
     * @param roleIds
     */
    void add(User user, Integer[] roleIds);

    /**
     * 根据查询id回显用户数据
     *
     * @param id
     * @return
     */
    User findById(Integer id);

    /**
     * 查询当前用户包含的所有角色id
     *
     * @param id
     * @return
     */
    List<Integer> findRoleId(Integer id);

    /**
     * 编辑保存用户
     *
     * @param user
     * @param roleIds
     */
    void edit(User user, Integer[] roleIds);

    /**
     * 删除用户
     *
     * @param id
     */
    void delete(Integer id);
}
