package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisPicConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import com.itheima.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

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

    /**
     * 分页查询菜单
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = menuService.findPage(queryPageBean.getCurrentPage(),
                queryPageBean.getPageSize(),
                queryPageBean.getQueryString());
        return pageResult;
    }

    /**
     * 新增菜单
     * @param menu
     * @return
     */
    @RequestMapping(value = "/add")
    public Result menu(@RequestBody Menu menu){
        try {
            menuService.add(menu);
            return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MENU_FAIL);
        }
    }

    /**
     * 根据menuId查回显数据
     * @param id
     * @return
     */
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        Menu menu = menuService.findById(id);
        if(menu!=null){
            return new Result(true, MessageConstant.EDIT_MENU_SUCCESS,menu);
        }else{
            return new Result(false, MessageConstant.EDIT_MENU_FAIL);
        }
    }

    /**
     * 编辑保存菜单
     * @param menu
     * @return
     */
    @RequestMapping(value = "/edit")
    public Result edit(@RequestBody Menu menu){
        try {
            menuService.edit(menu);
            return new Result(true, MessageConstant.EDIT_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_MENU_FAIL);
        }
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete")
    public Result delete(Integer id){
        try {
            menuService.delete(id);
            return new Result(true, MessageConstant.DELETE_MENU_SUCCESS);
        } catch (RuntimeException re){
            re.printStackTrace();
            return new Result(false, re.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MENU_FAIL);
        }
    }

    @Autowired
    JedisPool jedisPool;

    /**
     * 图片上传
     * @param imgFile
     * @return
     */
    @RequestMapping(value = "/upload")
    public Result upload(MultipartFile imgFile){
        try {
            // * 实现将图片存放到七牛云上
            // 参数一：字节数组（springmvc的方式），使用imgFile.getBytes()
            // 参数二：上传文件名（唯一）
            // 获取上传的文件名（1.jpg）
            String originalFilename = imgFile.getOriginalFilename();
            // 使用UUID的方式生成文件名
            String fileName = UUID.randomUUID()+originalFilename.substring(originalFilename.lastIndexOf("."));
            // * 实现将图片存放到七牛云上
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            // （1）Redis的集合存储，上传图片的时候，将图片名称，存放到Redis中一份（key值setmeal_pic_resource），然后再上传到七牛云上
            jedisPool.getResource().sadd(RedisPicConstant.MENU_PIC_RESOURCES,fileName);
            // UUID的方式生成文件名，并返回
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
}
