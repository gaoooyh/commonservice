package com.tools.commonservice.util.excelUtil;

import com.tools.commonservice.exception.ApiException;
import com.tools.commonservice.exception.ErrorCode;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ExcelExportUtils {

    private static void buildHeader(Workbook wb, Sheet sheet, ExcelHeader header) {

        CellStyle headerStyle = wb.createCellStyle();
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        headerStyle.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        headerStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        headerStyle.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        headerStyle.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        if (header.getTitles() == null || header.getTitles().length == 0) {
            return;
        }
        int maxWidth = 0;
        for (ExcelHeader.ExcelTitle title : header.getTitles()[0]) {
            maxWidth += title.getWidth();
        }
        boolean[][] filledCells = new boolean[header.getTitles().length][maxWidth];

        for (int i = 1; i <= maxWidth; i++) {
            sheet.setColumnWidth(i, 256*15+184);
        }
        List<CellRangeAddress> cellRangeAddresses = new ArrayList<>();
        for (int i = 0; i < header.getTitles().length; i++) {

            int start = 0;
            Row row = sheet.createRow(i);
            row.setHeight((short) 400);
            List<ExcelHeader.ExcelTitle> titleList = header.getTitles()[i];

            for (ExcelHeader.ExcelTitle title : titleList) {
                for (int j = start; j < filledCells[0].length; j++) {
                    if (filledCells[i][j]) {
                        start++;
                    } else {
                        break;
                    }
                }

                Cell cell = row.createCell(start, CellType.STRING);
                cell.setCellStyle(headerStyle);
                cell.setCellValue(title.getName());
                if (title.getWidth() > 1 || title.getHeight() > 1) {
                    for (int m = 0; m <  title.getWidth(); m++) {
                        for (int n = 0; n < title.getHeight(); n++) {
                            filledCells[i + n][start + m] = true;
                        }
                    }
                    CellRangeAddress cellAddresses = new CellRangeAddress(i, i + title.getHeight() - 1, start, start + title.getWidth() - 1);
                    cellRangeAddresses.add(cellAddresses);
                    sheet.addMergedRegion(cellAddresses);

                } else {
                    filledCells[i][start] = true;
                }
            }
        }

        for (CellRangeAddress cellAddress : cellRangeAddresses) {
            RegionUtil.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex(), cellAddress, sheet);
            RegionUtil.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex(), cellAddress, sheet);
            RegionUtil.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex(), cellAddress, sheet);
            RegionUtil.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex(), cellAddress, sheet);
            RegionUtil.setBorderBottom(BorderStyle.THIN, cellAddress, sheet);
            RegionUtil.setBorderTop(BorderStyle.THIN, cellAddress, sheet);
            RegionUtil.setBorderLeft(BorderStyle.THIN, cellAddress, sheet);
            RegionUtil.setBorderRight(BorderStyle.THIN, cellAddress, sheet);
        }
    }

    private static void writeData(Workbook wb, Sheet sheet, Collection<String[]> data, int startRow) {
        if (data == null || data.isEmpty()) {
            return;
        }
        CellStyle headerStyle = wb.createCellStyle();

        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setTopBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        headerStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        headerStyle.setRightBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        headerStyle.setLeftBorderColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());

        headerStyle.setAlignment(HorizontalAlignment.LEFT);
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setWrapText(true);

        for (String[] rowData : data) {
            Row row = sheet.createRow(startRow);
            int maxRowCnt = 1;
            for(int i = 0; i < rowData.length; i++) {
                int cnt = 1;
                Cell cell = row.createCell(i, CellType.STRING);
                cell.setCellStyle(headerStyle);
                String cellValue = rowData[i];
                if (cellValue == null) {
                    cellValue = "";
                } else {
                    for(char c : cellValue.toCharArray()) {
                        if(c == (char)10) {
                            cnt++;
                        }
                    }
                }
                cell.setCellValue(cellValue);
                maxRowCnt = Math.max(maxRowCnt, cnt);
            }
            row.setHeight((short) (400*maxRowCnt));
            startRow++;
        }
    }


    public static void export(ExcelData data, OutputStream outputStream) throws IOException {
        if (data == null) {
            throw new ApiException(ErrorCode.paramError("导出数据为空"));
        }
        if (data.getHeader() == null) {
            throw new ApiException(ErrorCode.paramError("导出表头为空，导出失败"));
        }
        if (data.getSheetName() == null) {
            throw new ApiException(ErrorCode.paramError("导出表名为空，导出失败"));
        }

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(data.getSheetName());
        buildHeader(wb, sheet, data.getHeader());
        writeData(wb, sheet, data.getData(), data.getHeader().getTitles().length);

        wb.write(outputStream);
    }


    public static void export(Collection<ExcelData> dataList, OutputStream outputStream) throws IOException {
        if (dataList.isEmpty()) {
            throw new ApiException(ErrorCode.paramError("导出数据为空"));
        }
        Workbook wb = new XSSFWorkbook();
        for (ExcelData data : dataList) {
            Sheet sheet = wb.createSheet(data.getSheetName());
            buildHeader(wb, sheet, data.getHeader());
            writeData(wb, sheet, data.getData(), data.getHeader().getTitles().length);
        }
        wb.write(outputStream);
    }

}
