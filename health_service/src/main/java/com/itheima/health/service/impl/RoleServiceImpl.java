package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:03
 * @Version V1.0
 */
@Service // dubbo提供
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    /**
     * 查询所有角色
     *
     * @return
     */
    @Override
    public List<Role> findAll() {
        List<Role> list = roleDao.findAll();
        return list;
    }

    /**
     * 分页查询角色
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Role> page = roleDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 新增角色
     * @param role
     * @param permissionIds
     * @param menuIds
     */
    @Override
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //保存角色 返回角色id
        roleDao.add(role);
        //角色权限中间表插入数据
        this.setRoleAndPermission(role.getId(),permissionIds);
        //角色菜单中间表插入数据
        this.setRoleAndMenu(role.getId(),menuIds);
    }

    /**
     * 根据id查询角色信息
     * @param id
     * @return
     */
    @Override
    public Role findById(Integer id) {
        return roleDao.findById(id);
    }

    /**
     * 查询当前角色包含的所有权限id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findPermissionId(Integer id) {
        List<Integer> list = roleDao.findPermissionId(id);
        return list;
    }

    /**
     * 查询当前角色包含的所有角色id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findMenuId(Integer id) {
        List<Integer> list = roleDao.findMenuId(id);
        return list;
    }

    private void setRoleAndMenu(Integer roleId, Integer[] menuIds) {
        // 遍历
        if(menuIds!=null && menuIds.length>0){
            for (Integer menuId : menuIds) {
                Map map = new HashMap();
                map.put("roleId",roleId);
                map.put("menuId",menuId);
                roleDao.addRoleAndMenu(map);
            }
        }
    }

    private void setRoleAndPermission(Integer roleId, Integer[] permissionIds) {
        // 遍历
        if(permissionIds!=null && permissionIds.length>0){
            for (Integer permissionId : permissionIds) {
                Map map = new HashMap();
                map.put("roleId",roleId);
                map.put("permissionId",permissionId);
                roleDao.addRoleAndPermission(map);
            }
        }
    }


}
