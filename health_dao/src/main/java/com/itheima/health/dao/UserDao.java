package com.itheima.health.dao;

import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    User findUserByUsername(String username);

    /**
     * 分页查询用户
     *
     * @param queryString
     * @return
     */
    List<User> findPage(String queryString);

    /**
     * 新增保存用户
     *
     * @param user
     */
    void add(User user);

    /**
     * 新增用户和角色中间表的数据
     *
     * @param userId
     * @param roleId
     */
    void addUserAndRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    /**
     * 根据查询id回显用户数据
     *
     * @param id
     * @return
     */
    User findById(Integer id);

    /**
     * 查询当前用户包含的所有角色id
     *
     * @param id
     * @return
     */
    List<Integer> findRoleId(Integer id);

    /**
     * 编辑保存用户
     *
     * @param user
     */
    void edit(User user);

    /**
     * 根据用户id删除中间表的数据
     *
     * @param id
     */
    void deleteUserAndRoleByUserId(Integer id);

    /**
     * 使用用户ID，判断用户和角色中间表中是否存在数据
     *
     * @param id
     * @return
     */
    Long findUserAndRoleByUserId(Integer id);

    /**
     * 删除用户
     *
     * @param id
     */
    void delete(Integer id);
}
