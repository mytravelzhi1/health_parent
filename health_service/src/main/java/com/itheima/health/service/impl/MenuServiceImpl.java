package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单实现类
 *
 * @author fht
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    RoleDao roleDao;

    @Autowired
    MenuDao menuDao;

    @Override
    public List<Menu> findMenus(String username) {

        //根据用户 查询,获得当前用户的角色id
        List<Integer> roleIdList = roleDao.findRolesByUsername(username);

        //根据角色id 查询关联的菜单项
        List<Menu> menuList=menuDao.findMenusByRoleId(roleIdList);

        //定义返回的menu
        List<Menu> resultList= new ArrayList<>();

        // 将菜单和子菜单绑定
        for (Menu menu : menuList) {
            //遍历菜单项.判断是否是父节点
            if (menu.getParentMenuId()==null){
                //定义子节点的集合
                List<Menu> childList = new ArrayList<>();
                for (Menu child : menuList) {
                    if (child.getParentMenuId()==menu.getId()){
                        //说明是子节点
                        childList.add(child);//将所有的子节点放入集合
                        //menuList.remove(child);
                    }
                }
                //将所有的子节点集合存入menu
                menu.setChildren(childList);
                resultList.add(menu);

            }
        }
        return resultList;
    }
}
