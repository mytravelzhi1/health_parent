package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:03
 * @Version V1.0
 */
@Service // dubbo提供
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);
        return user;
    }

    /**
     * 分页查询用户
     *
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        //1.初始化数据
        PageHelper.startPage(currentPage, pageSize);
        //2.查询数据
        List<User> userList = userDao.findPage(queryString);
        //3.封装
        PageInfo<User> pageInfo = new PageInfo<>(userList);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 添加用户
     *
     * @param user
     * @param roleIds
     */
    @Override
    public void add(User user, Integer[] roleIds) {
        // 第一步：接收到用户的对象user，执行保存用户，保存用户的同时，返回用户ID
        //密码加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        userDao.add(user);
        // 第二步：接收到Integer类型的数组，存放检查项的id，和保存返回的检查组id，向中间表中插入数据
        this.setUserAndRole(user.getId(), roleIds);
    }

    /**
     * 根据查询id回显用户数据
     *
     * @param id
     * @return
     */
    @Override
    public User findById(Integer id) {
        User user = userDao.findById(id);
        return user;
    }

    /**
     * 查询当前用户包含的所有角色id
     *
     * @param id
     * @return
     */
    @Override
    public List<Integer> findRoleId(Integer id) {
        List<Integer> list = userDao.findRoleId(id);
        return list;
    }

    /**
     * 编辑保存用户
     *
     * @param user
     * @param roleIds
     */
    @Override
    public void edit(User user, Integer[] roleIds) {
        //密码加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        // 1：修改编辑用户的相关信息字段，更新t_user
        userDao.edit(user);
        // 2：修改用户和角色的中间表，更新t__user_role（中间表）
        // （1）使用用户的id，先删除之前的数据
        // 根据用户id删除中间表的数据
        userDao.deleteUserAndRoleByUserId(user.getId());
        // （2）重新建立用户和角色的关联关系，新增中间表的数据
        this.setUserAndRole(user.getId(), roleIds);
    }

    /**
     * 删除用户
     *
     * @param id
     */
    @Override
    public void delete(Integer id) {
        // 删除用户之前，判断用户和角色之间是否存在关联关系，如果存在关联关系不能删除
        Long count1 = userDao.findUserAndRoleByUserId(id);
        if (count1 > 0) {
            throw new RuntimeException(MessageConstant.GET_USERANDROLEERROR);
        }
        userDao.delete(id);
    }

    private void setUserAndRole(Integer userId, Integer[] roleIds) {
        if (roleIds != null && roleIds.length > 0) {
            for (Integer roleId : roleIds) {
                // 保存（传递多个参数，在Dao的方法中，通过@Param指定方法参数的名称
                userDao.addUserAndRole(userId, roleId);
            }
        }
    }
}
