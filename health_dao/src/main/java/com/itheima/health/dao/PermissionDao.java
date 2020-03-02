package com.itheima.health.dao;

import com.itheima.health.pojo.Permission;

import java.util.List;
import java.util.Set;

/**
 * @ClassName PermissionDao
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/25 14:22
 * @Version V1.0
 */
public interface PermissionDao {

    Set<Permission> findPermissionsByRoleId(Integer roleId);

    //查询所有权限
    List<Permission> findAll();

    //新增检查项
    void add(Permission permission);

    //分页查询
    List<Permission> findPage(String queryString);

    //检查某权限与角色是否有关联关系
    Long findRoleAndPermissionCountByPermissionId(Integer id);

    //删除检查项
    void delete(Integer id);

    //编辑检查项
    void edit(Permission permission);

    //根据id查询检查项
    Permission findById(Integer id);

    //统计总权限数
    Long findPermissionTotal();
}
