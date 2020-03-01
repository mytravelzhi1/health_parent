package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName ReportContoller
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/13 16:04
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/report")
public class ReportContoller {

    @Reference// 订阅 dubbo注解
            MemberService memberService;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;


    // 统计报表（会员数量折线图统计）
    @RequestMapping(value = "/getMemberReport")
    public Result getMemberReport() {
        try {
            // 组织结果集
            /**
             * 返回Map<String,Object>
             map集合的key：                       map集合的value：
             months                               List<String>   -->['2019-06','2019-07']
             memberCount                          List<Integer> -->[10,35]
             */
            // 使用日历的工具类，统计过去12个月的时间
            Map<String, Object> map = new HashMap<>();
            // 存放到List<String> 对应key：months
            List<String> months = new ArrayList<>();
            // 获取Calendar对象
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -12); // 获取过去12个月，2019-2
            for (int i = 0; i < 12; i++) {
                // 过去的12个月，输出
                calendar.add(Calendar.MONTH, 1); //2019-3
                Date date = calendar.getTime();
                String sDate = new SimpleDateFormat("yyyy-MM").format(date);
                months.add(sDate);
            }

            // 组织每个月查询的会员数量（sql：SELECT COUNT(*) FROM t_member WHERE  regTime<= '2019-04-31' ）
            List<Integer> memberCount = memberService.findCountByBeforeRegTime(months);

            // [2019-03, 2019-04, 2019-05, 2019-06, 2019-07, 2019-08, 2019-09, 2019-10, 2019-11, 2019-12, 2020-01, 2020-02]
            map.put("months", months);
            // [3, 4, 5, 7, ...]
            map.put("memberCount", memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    // 统计报表（套餐预约占比饼形图统计）
    @RequestMapping(value = "/getSetmealReport")
    public Result getSetmealReport() {
        try {
            // 组织结果集
            /**
             * 返回Map<String,Object>
             map集合的key：                       map集合的value：
             setmealNames                          List<String>   -->['入职体检套餐','妇女节套餐']
             setmealCount                          List<Map<String,Object>> -->[
             {value: 335, name: '入职体检套餐'},
             {value: 310, name: '妇女节套餐'}
             ]
             */
            Map<String, Object> map = new HashMap<>();
            // 组织List<Map>
            List<Map> setmealCount = setmealService.findOrderCountBySetmealName();
            // 组织List<String>
            List<String> setmealNames = new ArrayList<>();
            // 遍历setmealCount
            if (setmealCount != null && setmealCount.size() > 0) {
                for (Map setmealMap : setmealCount) {
                    String name = (String) setmealMap.get("name");
                    setmealNames.add(name);
                }
            }
            map.put("setmealNames", setmealNames);
            map.put("setmealCount", setmealCount);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * #################################################################################
     * #对应SQL
     * # 一：会员相关
     * # reportDate:null, 时间（当前时间）
     * # todayNewMember :0,：今天新增会员数
     * SELECT COUNT(id) FROM t_member WHERE regTime = '2020-02-27'
     * # totalMember :0,：总会员数
     * SELECT COUNT(id) FROM t_member
     * # thisWeekNewMember :0, :本周新增会员数（计算本周的周一）
     * SELECT COUNT(id) FROM t_member WHERE regTime >= '2020-02-24'
     * # thisMonthNewMember :0, ：本月新增会员数（计算本月的1号）
     * SELECT COUNT(id) FROM t_member WHERE regTime >= '2020-02-01'
     * # 二：预约订单相关
     * # todayOrderNumber :0,：今天预约人数
     * SELECT COUNT(id) FROM t_order WHERE orderDate = '2020-02-27'
     * # todayVisitsNumber :0,：今天到诊人数
     * SELECT COUNT(id) FROM t_order WHERE orderDate = '2020-02-27' AND orderStatus = '已到诊'
     * # thisWeekOrderNumber :0, ：本周预约人数（计算本周的周一，计算本周的周日）
     * SELECT COUNT(id) FROM t_order WHERE orderDate BETWEEN '2020-02-24' AND '2020-03-01'
     * # thisWeekVisitsNumber :0, ：本周到诊人数（计算本周的周一，计算本周的周日）
     * SELECT COUNT(id) FROM t_order WHERE orderDate BETWEEN '2020-02-24' AND '2020-03-01' AND orderStatus = '已到诊'
     * # thisMonthOrderNumber :0, ：本月预约人数（计算本月的1号，本月的最后1天）
     * SELECT COUNT(id) FROM t_order WHERE orderDate BETWEEN '2020-02-01' AND '2020-02-29'
     * # thisMonthVisitsNumber :0, ：本月到诊人数（计算本月的1号，本月的最后1天）
     * SELECT COUNT(id) FROM t_order WHERE orderDate BETWEEN '2020-02-01' AND '2020-02-29' AND orderStatus = '已到诊'
     * # 三：套餐相关
     * #hotSetmeal :[   # 热门套餐（预约最多的放置到最前面，显示最热门的4个）
     * #{name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222},
     * #{name:'阳光爸妈升级肿瘤12项筛查体检套餐',setmeal_count:200,proportion:0.222}
     * #]
     * SELECT s.name,COUNT(*) setmeal_count,COUNT(*)/(SELECT COUNT(*) FROM t_order) proportion FROM t_order o,t_setmeal s
     * WHERE o.setmeal_id = s.id
     * GROUP BY s.name
     * ORDER BY setmeal_count DESC
     * LIMIT 0,4
     */

    // 统计报表，运营数据统计
    @RequestMapping(value = "/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            Map<String, Object> map = reportService.findBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    // 统计报表，运营数据统计（导出excel报表）
    @RequestMapping(value = "/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 1：读取放置到excel中的内容数据
            Map<String, Object> map = reportService.findBusinessReportData();
            String reportDate = (String) map.get("reportDate");//String  ：存放当前时间
            Integer todayNewMember = (Integer) map.get("todayNewMember");//Integer
            Integer totalMember = (Integer) map.get("totalMember");//Integer
            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");//Integer
            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");//Integer
            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");//Integer
            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");//Integer
            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");//Integer
            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");//Integer
            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");//Integer
            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");//Integer
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");//List<Map>
            // 2：加载模板文件（位置：/webapp/template/report_template.xlsx）
            // String path = request.getSession().getServletContext().getRealPath("template/report_template.xlsx");
            String path = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            // 3：使用POI，读取工作簿，读取工作表，读取行，将数据填充到单元格
            XSSFWorkbook workbook = new XSSFWorkbook(new File(path));
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(2); // 从0开始
            // 日期
            row.getCell(5).setCellValue(reportDate);
            // 会员相关
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);
            row.getCell(7).setCellValue(thisMonthNewMember);
            // 预约订单相关
            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);
            row.getCell(7).setCellValue(todayVisitsNumber);

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            row.getCell(7).setCellValue(thisWeekVisitsNumber);

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            row.getCell(7).setCellValue(thisMonthVisitsNumber);

            // 从12开始读取
            int rownum = 12;
            if (hotSetmeal != null && hotSetmeal.size() > 0) {
                for (Map map1 : hotSetmeal) {
                    String name = (String) map1.get("name");
                    Long setmeal_count = (Long) map1.get("setmeal_count");
                    BigDecimal proportion = (BigDecimal) map1.get("proportion");
                    row = sheet.getRow(rownum++); // ++放置到后面，先12，根据循环累加
                    row.getCell(4).setCellValue(name);
                    row.getCell(5).setCellValue(setmeal_count);
                    row.getCell(6).setCellValue(String.valueOf(proportion));
                }
            }
            // 4：将excel文件以IO的形式导出（设置类型和下载方式）
            ServletOutputStream out = response.getOutputStream();
            // 设置类型
            response.setContentType("application/vnd.ms-excel"); // 不指定。默认是以文本的形式输出
            // 设置下载方式（"attachment;filename="+filename：表示附件的方式下载；默认inline：表示内连，在浏览器上直接查看）
            String filename = "businessReport85.xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            workbook.write(out);
            // 刷新和关闭
            out.flush();
            out.close();
            workbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
