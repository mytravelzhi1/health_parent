package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName RoleContoller
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:04
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/permission")
public class PermissionContoller {

    @Reference// 订阅 dubbo注解
    PermissionService permissionService;

    @Autowired
    private JedisPool jedisPool;

    //查询所有权限
    @RequestMapping(value = "/findAll")
    public Result findAll() {
        Map map = new HashMap<>();
        //先查redis,看有没有所有权限的数据
        if (jedisPool.getResource().get("permission") == null) {
            //redis中没有权限数据
            System.out.println("redis中没有权限数据,从数据库中查询");
            map = permissionService.findAll();
            //存储数据到redis内
            if (map != null && map.size() > 0) {
                //map转换为json
                String permissionMap = JSON.toJSON(map).toString();
                jedisPool.getResource().set("permission", permissionMap);
                return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, map);
            } else {
                return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
            }
        }else{
            //redis中有权限数据,从redis中读取
            System.out.println("redis中有权限数据,从redis中查询");
            //取出json数据
            String permission = jedisPool.getResource().get("permission");
            map= JSONObject.parseObject(permission,Map.class);
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,map);
        }
    }

    //根据id查询权限
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        Permission permission = permissionService.findById(id);
        if(permission !=null){
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,permission);
        }else {
            return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }

    //编辑权限
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody Permission permission){
        try {
            permissionService.edit(permission);
            return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_PERMISSION_FAIL);
        }
    }

    //删除
    @RequestMapping(value = "/delete")
    public Result delete(Integer id) {
        try {
            permissionService.delete(id);
            return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
        } catch (RuntimeException re) {
            //如果权限与角色存在关联关系,抛出自定义运行时异常
            re.printStackTrace();
            return new Result(false,re.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
    }

    //分页查询
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = permissionService.findPage(
                queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString()
        );
        return pageResult;
    }

    //增加检查项
    @RequestMapping(value = "/add")
    public Result add(@RequestBody Permission permission) {
        try {
            permissionService.add(permission);
            return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
    }
}
