package cc.seektao.utils;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String _YYYY_MM_DD = "yyyy/MM/dd";
    public static final String YYYYMMDD = "yyyyMMdd";

    public static String format(Date date, String patten){
        return new SimpleDateFormat(patten).format(date);
    }

    public static Date parse(String date, String patten){
        try {
            return new SimpleDateFormat(patten).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args) {
//        System.out.println(DateUtils.format(new Date(), DateUtils.YYYY_MM_DD));
//    }
}
