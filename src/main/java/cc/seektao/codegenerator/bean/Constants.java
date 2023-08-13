package cc.seektao.codegenerator.bean;

import cc.seektao.codegenerator.utils.PropertiesUtils;

public class Constants {
    public static Boolean IGNORE_TABLE_PREFIX ;

    public static String SUFFIX_BEAN_PARAM;

    static {
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));
        SUFFIX_BEAN_PARAM = PropertiesUtils.getString("suffix.bean.param");
    }

    public static final String[] SQL_DATE_TIME_TYPE = new String[]{"datetime", "timestamp"};
    public static final String[] SQL_DATE_TYPES = new String[]{"date"};
    public static final String[] SQL_DECIMAL_TYPE = new String[]{"decimal","double","float"};
    public static final String[] SQL_STRING_TYPE = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
    public static final String[] SQL_INTEGER_TYPE = new String[]{"int","tinyint"};
    public static final String[] SQL_LONG_TYPE = new String[]{"bigint"};
}
