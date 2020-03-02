package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import com.itheima.health.utils.FreemarkerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PermissionServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:03
 * @Version V1.0
 */
@Service // dubbo提供
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionDao permissionDao;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Autowired
    private JedisPool jedisPool;


    @Value("${output_path01}") //从属性文件读取输出目录的路径
    private String output_path;


    //增加权限
    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
        //清空redis的值
        deletePermissionInRedis();
    }

    private void deletePermissionInRedis() {
        jedisPool.getResource().del("permission");
    }

    //分页查询
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //使用mybatis分页插件
        //1.初始化数据
        PageHelper.startPage(currentPage, pageSize);
        List<Permission> list = permissionDao.findPage(queryString);
        PageInfo<Permission> pageInfo = new PageInfo<>(list);

        //total,查询总记录数
        Long total = pageInfo.getTotal();
        //rows,查询单页的记录数
        List<Permission> rows = pageInfo.getList();

        PageResult pageResult = new PageResult(total, rows);
        return pageResult;
    }

    //删除权限
    @Override
    public void delete(Integer id) {
        //检查权限与角色是否有关联关系,有则不能删除,没有则可以删除
        Long count = permissionDao.findRoleAndPermissionCountByPermissionId(id);
        if (count > 0) {
            throw new RuntimeException(MessageConstant.GET_PERMISSIONANDROLEERROR);
        } else {
            permissionDao.delete(id);
            //清空redis的值
            deletePermissionInRedis();
        }
    }

    //编辑权限
    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
        //清空redis的值
        deletePermissionInRedis();
    }

    //根据id查询权限
    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    //查询所有权限
    @Override
    public Map findAll() {
        Map map = new HashMap<>();
        //findAll的时候生成静态页面,模板需要的数据是dataList和total
        List<Permission> permissionList = permissionDao.findAll();
        map.put("dataList",permissionList);
        Long total = permissionDao.findPermissionTotal();
        map.put("total",total);
        //查询所有时,redis没有,从数据库中查,静态页面也随之改变
        generatePermissionStaticHtml(permissionList,total);
        return map;
    }

    //生成权限静态页面
    private void generatePermissionStaticHtml(List<Permission> permissionList,Long total) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("dataList", permissionList);
        dataMap.put("total", total);
        FreemarkerUtils.generateHtml(freeMarkerConfigurer, output_path,
                "permission_static.ftl", "permission_static.html", dataMap);
    }

}
