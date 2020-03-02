package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisPicConstant;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @ClassName MenuServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:03
 * @Version V1.0
 */
@Service // dubbo提供
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuDao menuDao;

    @Autowired
    JedisPool jedisPool;

    /**
     * 查询所有权限
     * @return
     */
    @Override
    public List<Menu> findAll() {
        List<Menu> list = menuDao.findAll();
        return list;
    }

    /**
     * 分页查询菜单
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Menu> page = menuDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**根据menuId查回显数据
     * 新增菜单
     * @param menu
     */
    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
        //Redis的集合存储，保存套餐数据图片的时候，将图片名称，存放到Redis中一份（key值setmeal_pic_db_resource），然后在保存到数据库
        jedisPool.getResource().sadd(RedisPicConstant.MENU_PIC_DB_RESOURCE,menu.getIcon());
    }

    /**
     * 根据menuId查回显数据
     * @param id
     * @return
     */
    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    /**
     * 编辑保存菜单
     * @param menu
     */
    @Override
    public void edit(Menu menu) {
        Menu menu_db = menuDao.findById(menu.getId());
        String icon_db = menu_db.getIcon();
        // (3)判断是否改变图片名称
        // 如果改变套餐图片的名称，需要做处理
        if(menu.getIcon()!=null && !menu.getIcon().equals(icon_db)){
            // 删除之前七牛云上的存放图片
            QiniuUtils.deleteFileFromQiniu(icon_db);
            // 删除Redis中key值是SETMEAL_PIC_RESOURCE的图片名称
            jedisPool.getResource().srem(RedisPicConstant.MENU_PIC_RESOURCES,icon_db);
            // 删除Redis中key值是SETMEAL_PIC_DB_RESOURCE的图片名称
            jedisPool.getResource().srem(RedisPicConstant.MENU_PIC_DB_RESOURCE,icon_db);
            // 添加Redis中key值是SETMEAL_PIC_DB_RESOURCE的新的图片名称
            jedisPool.getResource().sadd(RedisPicConstant.MENU_PIC_DB_RESOURCE,menu.getIcon());
        }
        menuDao.edit(menu);
    }

    /**
     * 删除菜单
     * @param id
     */
    @Override
    public void delete(Integer id) {
        // 1：在删除检查项之前，给予判断，判断当前检查项是否被检查组关联，如果没有关联，可以删除；如果关联，是不能删除，给予提示
        long count = menuDao.findMenuAndRoleCountByMenuId(id);
        // 存在数据，此时不能删除
        if(count>0){
            // 给予提示（异常），将异常抛给Controller，只有运行时异常，spring的事务处理才能捕获
            throw new RuntimeException(MessageConstant.GET_MENUANDROLEERROR);
        }
        Menu menu_db = menuDao.findById(id);
        String icon_db = menu_db.getIcon();
        // 七牛云删除图片
        if(icon_db!=null && !"".equals(icon_db)){
            // 删除七牛云上图片
            QiniuUtils.deleteFileFromQiniu(icon_db);
            // 删除Redis中key值是SETMEAL_PIC_RESOURCE的图片名称
            jedisPool.getResource().srem(RedisPicConstant.MENU_PIC_RESOURCES,icon_db);
            // 删除Redis中key值是SETMEAL_PIC_DB_RESOURCE的图片名称
            jedisPool.getResource().srem(RedisPicConstant.MENU_PIC_DB_RESOURCE,icon_db);
        }
        // 不存在，删除检查项
        menuDao.delete(id);
    }
}
