package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {



    Set<Role> findRolesByUserId(Integer userId);

    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll();

    /**
     * 分页查询角色
     * @param queryString
     * @return
     */
    Page<Role> findPage(String queryString);

    /**
     * 保存角色
     * @param role
     */
    void add(Role role);

    /**
     * 向角色菜单中间表插入数据
     * @param map
     */
    void addRoleAndMenu(Map map);

    /**
     * 向角色权限中间表插入数据
     * @param map
     */
    void addRoleAndPermission(Map map);

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    Role findById(Integer id);

    /**
     * 查询当前角色包含的所有权限id
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

    /**
     * 编辑保存角色
     * @param role
     */
    void edit(Role role);

    /**
     * 使用角色的id，先删除权限关联的数据
     * @param id
     */
    void deleteRoleAndPermissionByRoleId(Integer id);

    /**
     * 使用角色的id，先删除权限关联的数据
     * @param id
     */
    void deleteRoleAndMenuByRoleId(Integer id);

    /**
     * 判断角色和权限之间是否存在关联关系
     * @param id
     * @return
     */
    Long findRoleAndPermissionByRoleId(Integer id);

    /**
     * 判断角色和菜单之间是否存在关联关系
     * @param id
     * @return
     */
    Long findRoleAndMenuByRoleId(Integer id);

    /**
     * 删除角色
     * @param id
     */
    void delete(Integer id);

    List<Integer> findRolesByUsername(String username);
}
