package com.tools.commonservice.controller;

//import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.ExcelWriter;
//import com.alibaba.excel.util.ListUtils;
//import com.alibaba.excel.write.metadata.WriteSheet;
import com.tools.commonservice.exception.ApiException;
import com.tools.commonservice.exception.ErrorCode;
import com.tools.commonservice.util.excelUtil.ExcelData;
import com.tools.commonservice.util.excelUtil.ExcelExportUtils;
import com.tools.commonservice.util.excelUtil.ExcelHeader;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("export")
public class ExcelExportController {
    @GetMapping("test")
    public void test(HttpServletResponse response) {

        try {
            String filename = "test.xlsx";
            filename = URLEncoder.encode(filename, "utf-8");
//            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename + "");
            testService(response.getOutputStream());
        } catch (Exception ex) {
            throw new ApiException(ErrorCode.paramError("导出失败"));
        }

    }
//
//    @GetMapping("test1")
//    public void test1(HttpServletResponse response) {
//
//        try {
//            response.setContentType("application/vnd.ms-excel");
//            response.setCharacterEncoding("utf-8");
//            String fileName = URLEncoder.encode("test", "UTF-8").replaceAll("\\+", "%20");
//            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
//            EasyExcel.write(response.getOutputStream(), DemoData.class).sheet("模板").doWrite(data());
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//
//            throw new ApiException(ErrorCode.paramError("导出失败"));
//        }
//
//    }




    private void testService(OutputStream stream) throws IOException {
        ExcelHeader header = new ExcelHeader(2);
        header.setTitles(new List[]{
                Arrays.asList(
                        new ExcelHeader.ExcelTitle("表头1-1", 1, 2),
                        new ExcelHeader.ExcelTitle("表头1-2", 2, 1)
                ),
                Arrays.asList(
                        new ExcelHeader.ExcelTitle("表头2-1", 1, 1),
                        new ExcelHeader.ExcelTitle("表头2-2", 1, 1)
                )});

        ExcelData excelData = new ExcelData();
        excelData.setHeader(header);
        excelData.setSheetName("excel表sheet名称");
        List<String[]> list = new ArrayList();
        list.add(new String[] {"hello1", "Line1", "AAA"});
        list.add(new String[] {"hello2", "Line2", "BBB"});
        list.add(new String[] {"hello3", "Line3", "CCC"});


        List<String[]> data = list.stream().map(info -> {
            String[] row = new String[3];
            row[0] = info[0];
            row[1] = info[1];
            row[2] = info[2];
            return row;
        }).collect(Collectors.toList());
        excelData.setData(data);

        ExcelExportUtils.export(excelData, stream);
    }

//
//    private List<DemoData> data() {
//        List<DemoData> list = ListUtils.newArrayList();
//        for (int i = 0; i < 10; i++) {
//            DemoData data = new DemoData();
//            data.setString("字符串" + i);
//            data.setDate(LocalDate.now().toString());
//            data.setDoubleData(0.56);
//            list.add(data);
//        }
//        return list;
//    }

    @Data
    public static class DemoData{
        String string;
        String date;
        Double doubleData;
    }
}
