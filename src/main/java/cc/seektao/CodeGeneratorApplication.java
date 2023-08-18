package cc.seektao;

import cc.seektao.builder.*;
import cc.seektao.bean.TableInfo;

import java.util.List;

public class CodeGeneratorApplication {
    public static void main(String[] args) {
//        BuildTable.getTable();

        List<TableInfo> tableInfoList = BuildTable.getTable();

        BuildBase.execute();

        for (TableInfo tableInfo : tableInfoList){
            BuildPo.execute(tableInfo);

            BuildPoQuery.execute(tableInfo);

            BuildMapper.execute(tableInfo);
        }
    }
}
