/*package com.itheima.health.test;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.junit.Test;

import java.awt.*;
import java.io.FileOutputStream;

*//**
 * @ClassName TestItext
 * @Description TODO
 * @Author ly
 * @Company 深圳黑马程序员
 * @Date 2020/2/28 14:23
 * @Version V1.0
 *//*
public class TestItext {

    // 入门案例
    @Test
    public void createPdf() throws Exception {
        // 1：创建一个文档对象
        Document document = new Document();
        // 2：获取1个PdfWriter对象实例
        PdfWriter.getInstance(document, new FileOutputStream("D:/itext85.pdf"));
        // 解决中文问题
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese,12, Font.NORMAL, Color.BLUE);
        // 3：打开文档（方便添加数据）
        document.open();
        // 4：添加数据
        document.add(new Paragraph("Hello World! 你好! ^_^",font));
        // 5：关闭文档
        document.close();
    }

}*/
