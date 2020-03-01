package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName RoleContoller
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:04
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/role")
public class RoleContoller {

    @Reference// 订阅 dubbo注解
    RoleService roleService;

    /**
     *查询所有角色
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        List<Role> list = roleService.findAll();
        if (list!=null && list.size()>0){
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,list);
        }else {
            return new Result(true, MessageConstant.QUERY_ROLE_FAIL);
        }
    }
}
