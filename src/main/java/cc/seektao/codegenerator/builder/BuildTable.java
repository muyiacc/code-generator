package cc.seektao.codegenerator.builder;

import cc.seektao.codegenerator.bean.Constants;
import cc.seektao.codegenerator.bean.FieldInfo;
import cc.seektao.codegenerator.bean.TableInfo;
import cc.seektao.codegenerator.utils.JsonUtils;
import cc.seektao.codegenerator.utils.PropertiesUtils;
import cc.seektao.codegenerator.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BuildTable {
    private static Connection conn = null;
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    private static String SQL_SHOW_TABLE_STATUS = "show table status";
    private static String SQL_SHOW_TABLE_FIELDS = "show full fields from %s";
    private static String SQL_SHOW_TABLE_INDEX = "show index from %s";

    static {
        String driverName = PropertiesUtils.getString("db.driver.name");
        String url = PropertiesUtils.getString("db.url");
        String user = PropertiesUtils.getString("db.username");
        String password = PropertiesUtils.getString("db.password");

        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            logger.error("数据库连接失败" + e);
        }
    }

    public static List<TableInfo> getTable(){
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<TableInfo> tableInfoList = new ArrayList();
        try {
            ps = conn.prepareStatement(SQL_SHOW_TABLE_STATUS);
            rs = ps.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString("name");
                String comment = rs.getString("comment");
                //logger.info("tableName:{},comment:{}",tableName, comment);

                String beanName = tableName;
                if (Constants.IGNORE_TABLE_PREFIX) {
                    beanName = tableName.substring(tableName.indexOf("_")+1);
                }
                beanName = processField(beanName, true);
//                logger.info(beanName);

                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName + Constants.SUFFIX_BEAN_PARAM);

                readFieldInfo(tableInfo);

                getKeyIndexInfo(tableInfo);

                logger.info("tableInfo:{}", JsonUtils.convertObjectToJson(tableInfo));
            }
        } catch (Exception e) {
            logger.error("读取表失败", e);
        } finally {
            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return tableInfoList;
    }

    private static void readFieldInfo(TableInfo tableInfo){
        PreparedStatement ps = null;
        ResultSet fieldResult = null;

        List<FieldInfo> fieldInfoList = new ArrayList();
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_FIELDS,tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while (fieldResult.next()) {
                String field = fieldResult.getString("field");
                String type = fieldResult.getString("type");
                String extra = fieldResult.getString("extra");
                String comment = fieldResult.getString("comment");

                if (type.indexOf("(")>0){
                    type = type.substring(0, type.indexOf("("));
                }
                String propertyName = processField(field, false);

                FieldInfo fieldInfo = new FieldInfo();
                fieldInfoList.add(fieldInfo);

                fieldInfo.setFieldName(field);
                fieldInfo.setComment(comment);
                fieldInfo.setSqlType(type);
                fieldInfo.setAutoIncrement("".equalsIgnoreCase(extra) ? true : false);
                fieldInfo.setPropertyName(propertyName);
                fieldInfo.setJavaType(processJavaType(type));

                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPE, type)){
                    tableInfo.setHavaDateTime(true);
                }else {
                    tableInfo.setHavaDateTime(false);
                }
                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)){
                    tableInfo.setHavaDateTime(true);
                }else {
                    tableInfo.setHavaDateTime(false);
                }
                if (ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)){
                    tableInfo.setHavaBigDecimal(true);
                }else{
                    tableInfo.setHavaBigDecimal(false);
                }
            }
            tableInfo.setFieldList(fieldInfoList);
        } catch (Exception e) {
            logger.error("读取表失败", e);
        } finally {
            if (fieldResult != null){
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static List<FieldInfo> getKeyIndexInfo(TableInfo tableInfo){
        PreparedStatement ps = null;
        ResultSet fieldResult = null;

        List<FieldInfo> fieldInfoList = new ArrayList();
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_TABLE_INDEX,tableInfo.getTableName()));
            fieldResult = ps.executeQuery();
            while (fieldResult.next()) {
                String keyName = fieldResult.getString("key_name");
                Integer nonUnique = fieldResult.getInt("non_unique");
                String columnName = fieldResult.getString("column_name");

                if (nonUnique == 1){
                    continue;
                }
                List<FieldInfo> keyFieldList = tableInfo.getKeyIndexMap().get(keyName);
                if (null == keyFieldList){
                    keyFieldList = new ArrayList<>();
                    tableInfo.getKeyIndexMap().put(keyName, keyFieldList);
                }

                for (FieldInfo fieldInfo :
                        tableInfo.getFieldList()) {
                    if (fieldInfo.getFieldName().equals(columnName)) {
                        keyFieldList.add(fieldInfo);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("读取索引失败", e);
        } finally {
            if (fieldResult != null){
                try {
                    fieldResult.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return fieldInfoList;
    }

    private static String processField(String field,Boolean upperCaseFirstLetter){
        StringBuilder sb = new StringBuilder();
        String[] fields = field.split("_");
        sb.append(upperCaseFirstLetter ? StringUtils.upperCaseFieldFirstLetter(fields[0]) : fields[0]);
        for (int i = 1; i < fields.length; i++) {
            sb.append(StringUtils.upperCaseFieldFirstLetter(fields[i]));
        }
        return sb.toString();
    }

    private static String processJavaType(String type){
        if (ArrayUtils.contains(Constants.SQL_INTEGER_TYPE, type)){
            return "Integer";
        } else if(ArrayUtils.contains(Constants.SQL_LONG_TYPE, type)){
            return "Long";
        } else if(ArrayUtils.contains(Constants.SQL_STRING_TYPE, type)){
            return "String";
        } else if(ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPE, type) || ArrayUtils.contains(Constants.SQL_DATE_TYPES, type)){
            return "Date";
        } else if(ArrayUtils.contains(Constants.SQL_DECIMAL_TYPE, type)){
            return "BigDecimal";
        } else {
            throw new RuntimeException("无法识别的类型" + type);
        }
    }
}
