package com.itheima.health.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.*;
import java.util.Map;

public class FreemarkerUtils {
    public static void generateHtml(FreeMarkerConfigurer freeMarkerConfigurer,String output_path,String templateName, String htmlName, Map<String, Object> dataMap) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;

        try {
            //加载模板文件
            Template template = configuration.getTemplate(templateName);
            //生成数据
            File docFile = new File(output_path + "\\" + htmlName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
            //输出文件
            template.process(dataMap, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
