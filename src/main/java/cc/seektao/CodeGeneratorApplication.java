package cc.seektao;

import cc.seektao.builder.BuildBase;
import cc.seektao.builder.BuildPo;
import cc.seektao.bean.TableInfo;
import cc.seektao.builder.BuildTable;

import java.util.List;

public class CodeGeneratorApplication {
    public static void main(String[] args) {
//        BuildTable.getTable();

        List<TableInfo> tableInfoList = BuildTable.getTable();

        BuildBase.execute();

        for (TableInfo tableInfo : tableInfoList){
            BuildPo.execute(tableInfo);
        }
    }
}
