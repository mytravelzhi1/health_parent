package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName UserContoller
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:04
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/user")
public class UserContoller {

    @Reference// 订阅 dubbo注解
            UserService userService;

    // 使用SpringSecurity，获取当前登录的用户信息
    @RequestMapping(value = "/findUsername")
    public Result findUsername() {
        try {
            // 从SpringSecurity中获取用户信息
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 返回用户名，返回用户姓名，用户的地址...
            // com.itheima.health.pojo.User u = userService.findUserByUsername(user.getUsername());
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    /**
     * 分页查询用户
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = userService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody com.itheima.health.pojo.User user, Integer[] roleIds) {
        try {
            userService.add(user, roleIds);
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
    }

    /**
     * 根据查询id回显用户数据
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    //@RequestBody 接收前端传递给后端的json字符串中的数据的(请求体中的数据的)
    public Result findById(Integer id) {
        try {
            com.itheima.health.pojo.User user = userService.findById(id);
            return new Result(true, MessageConstant.QUERY_USER_SUCCESS, user);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
    }

    /**
     * 查询当前用户包含的所有角色id
     *
     * @param id
     * @return
     */
    @RequestMapping("/findRoleId")
    public List<Integer> findRoleId(Integer id) {
        List<Integer> list = userService.findRoleId(id);
        return list;
    }

    /**
     * 编辑保存用户
     *
     * @param user
     * @param roleIds
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody com.itheima.health.pojo.User user, Integer[] roleIds) {
        try {
            userService.edit(user, roleIds);
            return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_USER_FAIL);
        }
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            userService.delete(id);
            return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
        } catch (RuntimeException runtime) {
            return new Result(false, runtime.getMessage());
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_USER_FAIL);
        }
    }
}
