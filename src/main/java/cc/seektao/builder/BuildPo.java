package cc.seektao.builder;

import cc.seektao.bean.Constants;
import cc.seektao.bean.FieldInfo;
import cc.seektao.bean.TableInfo;
import cc.seektao.utils.DateUtils;
import cc.seektao.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 读取数据库，生成po对象
 */
public class BuildPo {
    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File poFile = new File(folder, tableInfo.getBeanName() + ".java");

        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            os = new FileOutputStream(poFile);
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);

            bw.write("package " + Constants.PACKAGE_PO + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import java.io.Serializable;");
            bw.newLine();

            if (tableInfo.getHavaDateTime() || tableInfo.getHavaDate()) {
                bw.write("import java.util.Date;");
                bw.newLine();

                bw.write("import " + Constants.PACKAGE_UTILS + ".*;");
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_ENUMS + ".*;");
                bw.newLine();

                bw.write("import " + Constants.BEAN_DATE_SERIALIZABLE_CLASS + ";");
                bw.newLine();
                bw.write("import " + Constants.BEAN_DATE_DESERIALIZABLE_CLASS + ";");
                bw.newLine();

            }

            if (!Constants.IGNORE_BEAN_TOJSON_FIELD.isEmpty()) {
                bw.write("import " + Constants.IGNORE_BEAN_TOJSON_CLASS + ";");
                bw.newLine();
            }

            if (tableInfo.getHavaBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo.getComment());
            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bw.newLine();

            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                // 添加日期序列化
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\t" + String.format(Constants.BEAN_DATE_SERIALIZABLE_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                    bw.write("\t" + String.format(Constants.BEAN_DATE_DESERIALIZABLE_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                }
                // 添加日期反序列化
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPE, fieldInfo.getSqlType())) {
                    bw.write("\t" + String.format(Constants.BEAN_DATE_SERIALIZABLE_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                    bw.write("\t" + String.format(Constants.BEAN_DATE_DESERIALIZABLE_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                }

                // 是否要忽略属性
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FIELD.split(","), fieldInfo.getFieldName())) {
                    bw.write("\t" + Constants.IGNORE_BEAN_TOJSON_EXPRESSION);
                    bw.newLine();
                }

                bw.write("\tprivate " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();
            }

            // 添加get/set方法
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                // 将字段名首字母变为大写
                String tmpField = StringUtils.upperCaseFieldFirstLetter(fieldInfo.getPropertyName());

                // 添加get方法
                bw.write("\tpublic " + fieldInfo.getJavaType() + " get" + tmpField + "() {");
                bw.newLine();
                bw.write("\t\t" + "return " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();

                // 添加set方法
                bw.write("\tpublic void " + "set" + tmpField + "(" + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ") {");
                bw.newLine();
                bw.write("\t\t" + "this." + fieldInfo.getPropertyName() + " = " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                bw.write("\t}");
                bw.newLine();
            }

            // 添加toString方法
            StringBuilder toString = new StringBuilder();
            for (int i = 0; i < tableInfo.getFieldList().size(); i++) {
                FieldInfo fieldInfo = tableInfo.getFieldList().get(i);
                if (i == 0) {       // 开头的最前面不加逗号
                    toString.append(String.format("\"%s:\" + (%s == null ? \"空\" : %s) + \n\t\t\t\t",
                            fieldInfo.getComment(), fieldInfo.getPropertyName(), fieldInfo.getPropertyName()));
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, tableInfo.getFieldList().get(i).getSqlType())) {      //  创建时间调用枚举类
                    toString.append(String.format("\", %s:\" + (%s == null ? \"空\" : DateUtils.format(%s, DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS.getPattern())) + \n\t\t\t\t",
                            fieldInfo.getComment(), fieldInfo.getPropertyName(), fieldInfo.getPropertyName()));
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPE, tableInfo.getFieldList().get(i).getSqlType())) {        //  创建日期调用枚举类
                    toString.append(String.format("\", %s:\" + (%s == null ? \"空\" : DateUtils.format(%s, DateTimePatternEnum.YYYY_MM_DD.getPattern())) + \n\t\t\t\t",
                            fieldInfo.getComment(), fieldInfo.getPropertyName(), fieldInfo.getPropertyName()));
                } else if (i == tableInfo.getFieldList().size() - 1) {          // 最后属性没有 + 号
                    toString.append(String.format("\", %s:\" + (%s == null ? \"空\" : %s)",
                            fieldInfo.getComment(), fieldInfo.getPropertyName(), fieldInfo.getPropertyName()));
                } else {        // 其余属性的toString书写方式
                    toString.append(String.format("\", %s:\" + (%s == null ? \"空\" : %s) + \n\t\t\t\t",
                            fieldInfo.getComment(), fieldInfo.getPropertyName(), fieldInfo.getPropertyName()));
                }
            }
            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic String toString() {");
            bw.newLine();
            bw.write("\t\treturn " + toString + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.info("创建po失败" + e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }


    }
}
