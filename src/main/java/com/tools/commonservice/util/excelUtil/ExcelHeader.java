package com.tools.commonservice.util.excelUtil;

import lombok.Data;

import java.util.List;

/**
 *
 * |---------|-----------------|
 * |         |     表头1-2     |
 * | 表头1-1 |------------------|
 * |        | 表头2-1 | 表头2-2 |
 * |--------|------------------|
 * | hello1 |  LINE1 |   AAA   |
 * |--------|------------------|
 * | hello2 | LINE1  |   BBB   |
 * |--------|------------------|
 *
 * 复杂表头:
 * 第一行: 表头1-1(width=1, height=2), 表头1-2(width=2, height=1),
 * 第二行: 表头2-1(width=1, height=1), 表头2-2(width=1, height=1)
 *
 */
@Data
public class ExcelHeader {

    private List<ExcelTitle>[] titles;

    /**
     * 表头总共行数
     * @param rows
     */
    public ExcelHeader(int rows) {
        titles = new List[rows];
    }

    @Data
    public static class ExcelTitle {

        public ExcelTitle(String name) {
            this.name = name;
        }

        public ExcelTitle(String name, int width, int height) {
            this(name);
            this.width = width;
            this.height = height;
        }

        // 列名字
        private String name;
        // 宽度，占用的列数
        private int width = 1;
        // 高度，占用的行数
        private int height = 1;
    }
}
