package cc.seektao.bean;

import cc.seektao.utils.PropertiesUtils;

public class Constants {
    public static String AUTHOR_COMMENT ;
    public static Boolean IGNORE_TABLE_PREFIX ;
    public static String SUFFIX_BEAN_PARAM;

    // 需要忽略的属性
    public static String IGNORE_BEAN_TOJSON_FIELD;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;
    // 日期序列化，反序列化
    public static String BEAN_DATE_SERIALIZABLE_EXPRESSION;
    public static String BEAN_DATE_SERIALIZABLE_CLASS;
    public static String BEAN_DATE_DESERIALIZABLE_EXPRESSION;
    public static String BEAN_DATE_DESERIALIZABLE_CLASS;

    public static String PACKAGE_BASE;
    public static String PACKAGE_PO;
    public static String PACKAGE_UTILS;
    public static String PACKAGE_ENUMS;
    private static String PATH_JAVA = "java";

    public static String PATH_BASE;
    public static String PATH_PO;
    public static String PATH_UTILS;
    public static String PATH_ENUMS;



    static {
        AUTHOR_COMMENT = PropertiesUtils.getString("author.comment");

        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));
        SUFFIX_BEAN_PARAM = PropertiesUtils.getString("suffix.bean.param");

        IGNORE_BEAN_TOJSON_FIELD = PropertiesUtils.getString("ignore.bean.tojson.field");
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getString("ignore.bean.tojson.expression");
        IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getString("ignore.bean.tojson.class");

        BEAN_DATE_SERIALIZABLE_EXPRESSION = PropertiesUtils.getString("bean.date.serializable.expression");
        BEAN_DATE_SERIALIZABLE_CLASS = PropertiesUtils.getString("bean.date.serializable.class");
        BEAN_DATE_DESERIALIZABLE_EXPRESSION = PropertiesUtils.getString("bean.date.deserializable.expression");
        BEAN_DATE_DESERIALIZABLE_CLASS = PropertiesUtils.getString("bean.date.deserializable.class");

        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");
        PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils");
        PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums");

        PATH_BASE = PropertiesUtils.getString("path.base");
        PATH_BASE = PATH_BASE + "/" + PATH_JAVA;

        PATH_PO = PATH_BASE + "/" + PACKAGE_PO.replace(".", "/");
        PATH_UTILS = PATH_BASE + "/" + PACKAGE_UTILS.replace(".", "/");
        PATH_ENUMS = PATH_BASE + "/" + PACKAGE_ENUMS.replace(".", "/");


    }

    public static final String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
    public static final String[] SQL_DATE_TYPE = new String[]{"date"};
    public static final String[] SQL_DECIMAL_TYPES = new String[]{"decimal","double","float"};
    public static final String[] SQL_STRING_TYPES = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
    public static final String[] SQL_INTEGER_TYPES = new String[]{"int","tinyint"};
    public static final String[] SQL_LONG_TYPE = new String[]{"bigint"};

    public static void main(String[] args) {
        System.out.println(PACKAGE_BASE);
        System.out.println(PACKAGE_PO);

        System.out.println(PATH_PO);
        System.out.println(PATH_BASE);
        System.out.println(PATH_UTILS);

    }
}
