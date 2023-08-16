package cc.seektao.builder;

import cc.seektao.bean.Constants;
import cc.seektao.utils.ConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 对一些基础类的生成
 */
public class BuildBase {
    private static Logger logger = LoggerFactory.getLogger(BuildBase.class);

    public static void execute() {
        List<String> headerInfoList = new ArrayList<>();

        // 生成DateTimePatternEnum枚举类
        headerInfoList.add("package " + Constants.PACKAGE_ENUMS + ";");
        build(headerInfoList,"DateTimePatternEnum", Constants.PATH_ENUMS);

        headerInfoList.clear();

        // 生成DateUtils类
        headerInfoList.add("package " + Constants.PACKAGE_UTILS + ";");
        build(headerInfoList,"DateUtils", Constants.PATH_UTILS);
    }

    public static void build(List<String> headerInfoList, String fileName, String outPutPath) {
        File folder = new File(outPutPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File javaFile = new File(outPutPath, fileName + ".java");

        // 创建流
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        try {
            String templatePath = BuildBase.class.getClassLoader().getResource("template/" + fileName + ".txt").getPath();
            is = new FileInputStream(templatePath);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            os = new FileOutputStream(javaFile);
            osw = new OutputStreamWriter(os, "utf-8");
            bw = new BufferedWriter(osw);

            // 导包
            for (String header : headerInfoList){
                bw.write(header);
                bw.newLine();
                bw.newLine();
            }

            String lineInfo = null;
            while ((lineInfo=br.readLine()) != null){
                bw.write(lineInfo);
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {
            logger.info("生成基础类:{},失败:", fileName, e);
        } finally {
            ConnectionUtils.close(bw, osw, os, br, isr, is);
        }
    }
}
