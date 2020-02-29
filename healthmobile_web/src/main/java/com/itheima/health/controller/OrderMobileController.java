package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.OrderService;
import com.itheima.health.service.SetmealService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

/**
 * @ClassName OrderMobileController
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/20 14:20
 * @Version V1.0
 */
@RestController
@RequestMapping(value = "/order")
public class OrderMobileController {

    @Reference
    private OrderService orderService;

    @Reference
    private SetmealService setmealService;

    @Autowired
    JedisPool jedisPool;

    // 体检预约保存
    @RequestMapping(value = "/submit")
    public Result submit(@RequestBody Map map){
        // 1：获取手机号和验证码
        String telepone = (String)map.get("telephone");
        String validateCode = (String)map.get("validateCode");
        // 2：使用手机号，从Redis中获取验证码
        String redisValidateCode = jedisPool.getResource().get(telepone+ RedisMessageConstant.SENDTYPE_ORDER);
        // 3：比对验证码，如果验证码没有匹配，提示“验证码输入有误”
        if(redisValidateCode == null || !redisValidateCode.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 4：指定预约类型，预约类型是微信预约（map存储值）
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        Result result = null;
        try {
            result = orderService.orderSubmit(map);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }

    // 使用订单id，查询订单的详情信息
    @RequestMapping(value = "/findById")
    public Result findById(Integer id){
        Map map = orderService.findById(id);
        if(map!=null && map.size()>0){
            return new Result(true,MessageConstant.ORDER_SUCCESS,map);
        }else{
            return new Result(false,MessageConstant.ORDER_FAIL);
        }
    }

    // 在预约成功页面，导出PDF文档
    @RequestMapping(value = "/exportSetmealInfoPdf")
    public Result exportSetmealInfoPdf(Integer id, HttpServletResponse response){ // 订单id
        try {
            /**第一步：查询预约订单数据*/
            Map map = orderService.findById(id);
            // 获取套餐id
            Integer setmealId = (Integer)map.get("setmealId");
            // 获取套餐详情
            Setmeal setmeal = setmealService.findById(setmealId);

            /**第二步：生成PDF报表*/
            // 生成PDF报表
            // 1：创建一个文档对象
            Document document = new Document();
            // 2：获取1个PdfWriter对象实例
            // 设置导出类型（1：指定PDF类型；2：下载方式）
            response.setContentType("application/pdf");
            String filename = "mobileSetmealOrder85.pdf";
            response.setHeader("Content-Disposition","attachment;filename="+filename);
            PdfWriter.getInstance(document, response.getOutputStream());
            // 解决中文问题
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font = new Font(bfChinese,12, Font.NORMAL, Color.BLUE);
            // 3：打开文档（方便添加数据）
            document.open();
            // 4：添加数据
            document.add(new Paragraph("体检人："+map.get("member"),font));
            document.add(new Paragraph("体检套餐："+map.get("setmeal"),font));
            document.add(new Paragraph("体检日期："+map.get("orderDate"),font));
            document.add(new Paragraph("预约类型："+map.get("orderType"),font));

            //设置表格的形式   
            Table table = new Table(3);//设置列数为3个表格
            table.setWidth(80); // 宽度
            table.setBorder(1); // 边框
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); //水平对齐方式
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
            /*设置表格属性*/
            table.setBorderColor(new Color(0, 0, 255)); //将边框的颜色设置为蓝色
            table.setPadding(5);//设置表格与字体间的间距
            //table.setSpacing(5);//设置表格上下的间距
            table.setAlignment(Element.ALIGN_CENTER);//设置字体显示居中样式

            table.addCell(buildCell("项目名称",font));//设置表头的名称
            table.addCell(buildCell("项目内容",font));//设置表头的名称
            table.addCell(buildCell("项目解读",font));//设置表头的名称
            // 遍历表格的数据
            if(setmeal!=null){
                if(setmeal.getCheckGroups()!=null && setmeal.getCheckGroups().size()>0){
                    for (CheckGroup checkGroup : setmeal.getCheckGroups()) {
                        // 检查组的名称
                        table.addCell(buildCell(checkGroup.getName(),font));//设置数据内容
                        StringBuffer stringBuffer = new StringBuffer();
                        if(checkGroup.getCheckItems()!=null && checkGroup.getCheckItems().size()>0){
                            for (CheckItem checkItem : checkGroup.getCheckItems()) {
                                stringBuffer.append(checkItem.getName()+" ");
                            }
                        }
                        table.addCell(buildCell(stringBuffer.toString(),font));//设置数据内容
                        // 检查组的备注
                        table.addCell(buildCell(checkGroup.getRemark(),font));//设置数据内容
                    }
                }
            }
            document.add(table);
            // 5：关闭文档
            document.close();
            return null; // 使用IO的形式完成导出。
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Cell buildCell(String content, Font font) throws BadElementException {
        return new Cell(new Phrase(content,font));
    }

}
