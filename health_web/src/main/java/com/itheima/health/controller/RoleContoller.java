package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.RequestBody;
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
     * 查询所有角色
     *
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        List<Role> list = roleService.findAll();
        if (list != null && list.size() > 0) {
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, list);
        } else {
            return new Result(true, MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    /**
     * 分页查询角色
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = roleService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 新增角色
     *
     * @param role
     * @param permissionIds
     * @param menuIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer[] permissionIds, Integer[] menuIds) {
        try {
            roleService.add(role, permissionIds, menuIds);
            return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
    }

    /**
     * 根据id查询角色信息
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Role role = roleService.findById(id);
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    /**
     *查询当前角色包含的所有权限id
     * @param id
     * @return
     */
    @RequestMapping("/findPermissionId")
    public List<Integer> findPermissionId(Integer id) {
        List<Integer> list = roleService.findPermissionId(id);
        return list;
    }

    /**
     *查询当前角色包含的所有菜单id
     * @param id
     * @return
     */
    @RequestMapping("/findMenuId")
    public List<Integer> findMenuId(Integer id) {
        List<Integer> list = roleService.findMenuId(id);
        return list;
    }

    /**
     * 编辑保存角色
     * @param role
     * @param permissionIds
     * @param menuIds
     * @return
     */
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody Role role, Integer[] permissionIds, Integer[] menuIds){
        try {
            roleService.edit(role,permissionIds,menuIds);
            return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_ROLE_FAIL);
        }
    }

    @RequestMapping(value = "/delete")
    public Result delete(Integer id){
        try {
            roleService.delete(id);
            return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
        } catch(RuntimeException ex){
            ex.printStackTrace();
            return new Result(false, ex.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_ROLE_FAIL);
        }
    }

}
