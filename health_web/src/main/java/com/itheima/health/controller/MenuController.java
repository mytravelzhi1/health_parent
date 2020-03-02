package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单管理
 *
 * @author fht
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Reference
    MenuService menuService;

    //根据用户名查询 菜单
    @RequestMapping("/findMenus")
    public Result findMenus(){

        try {
            System.out.println("进入菜单管理");
            // 从SpringSecurity中获取用户信息
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = user.getUsername();
            List<Menu> menulist=menuService.findMenus(username);
            return new Result(true, MessageConstant.GET_MENU_SUCCESS,menulist);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }
}
