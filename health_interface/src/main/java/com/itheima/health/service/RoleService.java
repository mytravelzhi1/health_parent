package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Role;

import java.util.List;

public interface RoleService {
    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll();

    /**
     * 分页查询角色
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 新增角色
     * @param role
     * @param permissionIds
     * @param menuIds
     */
    void add(Role role, Integer[] permissionIds, Integer[] menuIds);

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    Role findById(Integer id);

    /**
     * 查询当前角色包含的所有菜单id
     * @param id
     * @return
     */
    List<Integer> findPermissionId(Integer id);

    /**
     * 查询当前角色包含的所有角色id
     * @param id
     * @return
     */
    List<Integer> findMenuId(Integer id);
}
