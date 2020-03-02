package com.itheima.health.service;

import com.itheima.health.pojo.Permission;

import java.util.List;

public interface PermissionService {
    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findAll();

}
