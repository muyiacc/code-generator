package cc.seektao.builder;

import cc.seektao.bean.Constants;
import cc.seektao.utils.DateUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 增加注释
 */
public class BuildComment {
    public static void createClassComment(BufferedWriter bw, String classComment) throws IOException {
        bw.write("/**");
        bw.newLine();
        bw.write(" * @Describtion : " + classComment);
        bw.newLine();
        bw.write(" * @Author : " + Constants.AUTHOR_COMMENT);
        bw.newLine();
        bw.write(" * @Date : " + DateUtils.format(new Date(), DateUtils._YYYY_MM_DD));
        bw.newLine();
        bw.write(" */");
        bw.newLine();
    }
    public static void createFieldComment(BufferedWriter bw, String fieldComment) throws IOException {
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + (fieldComment==null ? "": fieldComment));
        bw.newLine();
        bw.write("\t */");
        bw.newLine();
    }
    public static void createMethodComment(BufferedWriter bw){

    }

}
