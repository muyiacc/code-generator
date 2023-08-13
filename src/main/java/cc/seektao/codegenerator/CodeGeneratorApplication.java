package cc.seektao.codegenerator;

import cc.seektao.codegenerator.bean.TableInfo;
import cc.seektao.codegenerator.builder.BuildTable;

import java.util.List;

public class CodeGeneratorApplication {
    public static void main(String[] args) {
        List<TableInfo> tableInfoList = BuildTable.getTable();
    }
}
