package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName MenuContoller
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:04
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/menu")
public class MenuContoller {

    @Reference// 订阅 dubbo注解
    MenuService menuService;

    @RequestMapping("/findAll")
    public Result findAll() {
        List<Menu> list = menuService.findAll();
        if (list != null && list.size() > 0) {
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, list);
        } else {
            return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }
}
