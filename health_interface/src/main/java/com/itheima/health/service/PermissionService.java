package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Permission;

import java.util.Map;

public interface PermissionService {

    //查询所有权限
    Map findAll();

    //新增权限
    void add(Permission permission);

    //分页查询
    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    //删除权限
    void delete(Integer id);

    //编辑权限
    void edit(Permission permission);

    //根据id查询权限,回显数据
    Permission findById(Integer id);

}
