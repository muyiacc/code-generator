package cc.seektao.builder;

import cc.seektao.bean.Constants;
import cc.seektao.bean.FieldInfo;
import cc.seektao.bean.TableInfo;
import cc.seektao.utils.ConnectionUtils;
import cc.seektao.utils.DateUtils;
import cc.seektao.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 读取数据库，生成po查询对象
 */
public class BuildPoQuery {
    private static final Logger logger = LoggerFactory.getLogger(BuildPoQuery.class);

    public static void execute(TableInfo tableInfo) {

        // 创建文件夹
        File folder = new File(Constants.PATH_QUERY);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + Constants.SUFFIX_BEAN_QUERY;

        File poFile = new File(folder, className + ".java");

        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            os = new FileOutputStream(poFile);
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);

            // 导入包
            bw.write("package " + Constants.PACKAGE_QUERY + ";");
            bw.newLine();
            bw.newLine();

            if (tableInfo.getHavaDateTime() || tableInfo.getHavaDate()) {
                bw.write("import java.util.Date;");
                bw.newLine();
            }

            if (tableInfo.getHavaBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            bw.newLine();

            // 添加类的注释
            BuildComment.createClassComment(bw, tableInfo.getComment() + "查询对象");

            bw.write("public class " + className + " {");
            bw.newLine();

            // 添加属性和及其注解
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();

                // 给String类型的字段加上 fuzzy 后缀
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY + ";");
                    bw.newLine();
                    bw.newLine();
                }

                // 给日期或时间类型的字段加上 start/end 后缀
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPE, fieldInfo.getSqlType()) ||
                        ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\tprivate String " + " " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START + ";");
                    bw.newLine();
                    bw.newLine();

                    bw.write("\tprivate String " + " " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END + ";");
                    bw.newLine();
                    bw.newLine();
                }
            }

            // 添加get/set方法
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                // 将字段名首字母变为大写
                String tmpField = StringUtils.upperCaseFieldFirstLetter(fieldInfo.getPropertyName());

                // 添加get方法
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + tmpField + "() {");
                    bw.newLine();
                    bw.write("\t\t" + "return " + fieldInfo.getPropertyName() + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();

                    bw.write("\tpublic String" + " get" + tmpField + Constants.SUFFIX_BEAN_QUERY_FUZZY + "() {");
                    bw.newLine();
                    bw.write("\t\t" + "return " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType()) ||
                        ArrayUtils.contains(Constants.SQL_DATE_TYPE, fieldInfo.getSqlType())) {
                    bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + tmpField + "() {");
                    bw.newLine();
                    bw.write("\t\t" + "return " + fieldInfo.getPropertyName() + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();

                    bw.write("\tpublic String" + " get" + tmpField + Constants.SUFFIX_BEAN_QUERY_TIME_START + "() {");
                    bw.newLine();
                    bw.write("\t\t" + "return " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();

                    bw.write("\tpublic String" + " get" + tmpField + Constants.SUFFIX_BEAN_QUERY_TIME_END + "() {");
                    bw.newLine();
                    bw.write("\t\t" + "return " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                } else {
                    bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + tmpField + "() {");
                    bw.newLine();
                    bw.write("\t\t" + "return " + fieldInfo.getPropertyName() + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                }

                // 添加set方法
                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\tpublic void " + "set" + tmpField + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {");
                    bw.newLine();
                    bw.write("\t\t" + "this." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();

                    bw.write("\tpublic void " + "set" + tmpField + Constants.SUFFIX_BEAN_QUERY_FUZZY + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY + ") {");
                    bw.newLine();
                    bw.write("\t\t" + "this." + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY  + " = " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_FUZZY + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES,fieldInfo.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TYPE, fieldInfo.getSqlType())) {
                    bw.write("\tpublic void " + "set" + tmpField + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {");
                    bw.newLine();
                    bw.write("\t\t" + "this." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();

                    bw.write("\tpublic void " + "set" + tmpField + Constants.SUFFIX_BEAN_QUERY_TIME_START + "( String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START + ") {");
                    bw.newLine();
                    bw.write("\t\t" + "this." + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START  + " = " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_START + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();

                    bw.write("\tpublic void " + "set" + tmpField + Constants.SUFFIX_BEAN_QUERY_TIME_END + "( String " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END + ") {");
                    bw.newLine();
                    bw.write("\t\t" + "this." + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END  + " = " + fieldInfo.getPropertyName() + Constants.SUFFIX_BEAN_QUERY_TIME_END + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                } else {
                    bw.write("\tpublic void " + "set" + tmpField + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {");
                    bw.newLine();
                    bw.write("\t\t" + "this." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";");
                    bw.newLine();
                    bw.write("\t}");
                    bw.newLine();
                }
            }

            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.info("创建po失败" + e);
        } finally {
            // 关闭流
            ConnectionUtils.close(bw, osw, os);
        }
    }
}
