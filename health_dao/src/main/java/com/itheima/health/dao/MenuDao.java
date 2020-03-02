package com.itheima.health.dao;

import com.itheima.health.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName PermissionDao
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/25 14:22
 * @Version V1.0
 */
public interface MenuDao {


    List<Menu> findMenusByRoleId(@Param("roleIdList") List<Integer> roleIdList);
}
