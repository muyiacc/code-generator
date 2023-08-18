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
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * 读取数据库，生成po mapper
 */
public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildMapper.class);

    public static void execute(TableInfo tableInfo) {

        // 创建文件夹
        File folder = new File(Constants.PATH_MAPPERS);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // mapper类的名字
        String mapperName = tableInfo.getBeanName() + Constants.SUFFIX_MAPPER;

        File poFile = new File(folder, mapperName + ".java");

        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            os = new FileOutputStream(poFile);
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);

            // 导入包
            bw.write("package " + Constants.PACKAGE_MAPPERS + ";");
            bw.newLine();
            bw.newLine();

            bw.write("import org.apache.ibatis.annotations.Param;");
            bw.newLine();
            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo.getComment() + "mapper");
            bw.write("public interface " + mapperName + "<T, P> extends BaseMapper<T, P> {");
            bw.newLine();

            // 生成方法
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldInfoList = entry.getValue();

                Integer index = 0;
                // 方法名
                StringBuilder methodName = new StringBuilder();
                // 方法参数
                StringBuilder methodParams = new StringBuilder();

                for (FieldInfo fieldInfo : keyFieldInfoList) {
                    index++;

                    // 构建方法名
                    methodName.append(StringUtils.upperCaseFieldFirstLetter(fieldInfo.getPropertyName()));
                    if (index < keyFieldInfoList.size()) {
                        methodName.append("And");
                    }

                    // 构建方法参数
                    methodParams.append(String.format("@Param(\"%s\") %s %s", fieldInfo.getPropertyName(), fieldInfo.getJavaType(), fieldInfo.getPropertyName()));
                    if (index < keyFieldInfoList.size()) {
                        methodParams.append(", ");
                    }
                }
                // 查询
                BuildComment.createFieldComment(bw, "根据" + methodName + "查询");
                bw.write("\tT selectBy" + methodName + "(" + methodParams + ");");
                bw.newLine();
                bw.newLine();

                // 更新
                BuildComment.createFieldComment(bw, "根据" + methodName + "更新");
                bw.write("\tInteger updateBy" + methodName + "(@Param(\"bean\") T t, " + methodParams + ");");
                bw.newLine();
                bw.newLine();

                // 删除
                BuildComment.createFieldComment(bw, "根据" + methodName + "删除");
                bw.write("\tInteger deleteBy" + methodName + "(" + methodParams + ");");
                bw.newLine();
                bw.newLine();
            }

            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.info("创建mapper失败" + e);
        } finally {
            // 关闭流
            ConnectionUtils.close(bw, osw, os);
        }
    }
}
