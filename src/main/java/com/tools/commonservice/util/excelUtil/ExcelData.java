package com.tools.commonservice.util.excelUtil;

import lombok.Data;

import java.util.Collection;

/**
 * 导出数据
 */
@Data
public class ExcelData {
    /**
     * 导出页签名字
     */
    private String sheetName;
    /**
     * 导出的表头
     */
    private ExcelHeader header;
    /**
     * 导出的数据
     */
    private Collection<String[]> data;

}
