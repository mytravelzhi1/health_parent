package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.DateUtils;

import java.util.Date;

public class ClearOrderSettingJob {

    @Reference
    OrderSettingService orderSettingService;

    public void clearOrderSetting(){
        try {
            System.out.println("执行清理预约设置任务了喔-------------");
            String clearTime = DateUtils.parseDate2String(new Date());
            orderSettingService.clearOrderSetting(clearTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
