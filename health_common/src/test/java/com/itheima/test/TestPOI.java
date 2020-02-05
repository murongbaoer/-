package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TestPOI {
    // 2.2.1. 从Excel文件读取数据
    /**
     * POI操作Excel报表的API介绍
     * WorkBook（工作簿）->XSSFWorkBook
     * Sheet（工作表）->XSSFSheet
     * Row（行）->XSSFRow
     * Cell（单元格）->XSSFCell
     */
    @Test
    public void readExcel() throws IOException {
//        1：创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("D:/hello.xlsx");
//        2：获得工作表对象（0表示第1个工作表）
        XSSFSheet sheet = workbook.getSheetAt(0);
//        3：遍历工作表对象 获得行对象
        for (Row row : sheet) {
            //   4：遍历行对象 获得单元格（列）对象
            for (Cell cell : row) {
                //        5：获得数据
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
//        6：关闭
        workbook.close();
    }
    // 2.2.1. 从Excel文件读取数据
    /**
     * POI操作Excel报表的API介绍
     * WorkBook（工作簿）->XSSFWorkBook
     * Sheet（工作表）->XSSFSheet
     * Row（行）->XSSFRow
     * Cell（单元格）->XSSFCell
     */
    @Test
    public void readExcel2() throws IOException {
//        1：创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook("D:/hello.xlsx");
//        2：获得工作表对象（0表示第1个工作表）
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 3：获取最后一个行号，从第1行开始遍历，到最后一个行号
        int lastRowNum = sheet.getLastRowNum();
        //System.out.println(lastRowNum);
        for(int i=0;i<=lastRowNum;i++){
            XSSFRow row = sheet.getRow(i);
            // 4：获取最后一个单元格号，从第1个单元格开始遍历，到最后一个单元格
            short cellNum = row.getLastCellNum();
            //System.out.println(cellNum);
            for(int j=0;j<cellNum;j++){
                XSSFCell cell = row.getCell(j);
                //  5：获得数据
                String value = cell.getStringCellValue();
                System.out.println(value);
            }
        }
//        6：关闭
        workbook.close();
    }
    // 2.2.2. 向Excel文件写入数据（从数据库查询数据，把查询的数据放到excel中导出）
    @Test
    public void writeExcel() throws IOException {
//        1：创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 2：创建工作表对象
        XSSFSheet sheet = workbook.createSheet("用户管理");
        // 3：创建行对象（0表示第1行）
        XSSFRow row1 = sheet.createRow(0);
        // 4：创建单元格对象（0表示第1个单元格）
        row1.createCell(0).setCellValue("姓名");
        row1.createCell(1).setCellValue("年龄");
        row1.createCell(2).setCellValue("地址");

        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue("冯小刚");
        row2.createCell(1).setCellValue("50");
        row2.createCell(2).setCellValue("北京");

        XSSFRow row3 = sheet.createRow(2);
        row3.createCell(0).setCellValue("张艺谋");
        row3.createCell(1).setCellValue("60");
        row3.createCell(2).setCellValue("上海");

        XSSFRow row4 = sheet.createRow(3);
        row4.createCell(0).setCellValue("周星驰");
        row4.createCell(1).setCellValue("50");
        row4.createCell(2).setCellValue("深圳");

        // 5：将excel数据写到D盘下excel83.xlsx
        OutputStream out = new FileOutputStream("D:/excel83.xlsx");
        workbook.write(out);
        out.flush();
        out.close();
//        6：关闭
        workbook.close();
    }
}
