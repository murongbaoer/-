package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberServcie;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/report")
public class ReportController {

    @Reference
    MemberServcie memberServcie;

    @Reference
    SetmealService setmealService;

    @Reference
    ReportService reportService;

    @RequestMapping(value = "/getMemberReport")
    public Result getMemberReport(){
        try {
            //1构造数据,过去1年的月份数据
            List<String> months = new ArrayList<>();
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.MONTH,-12);
            for (int i = 0; i < 12; i++) {
                instance.add(Calendar.MONTH,1);
                months.add(new SimpleDateFormat("yyyy-MM").format(instance.getTime()));
            }
            // 2：构造数据，过去1年的每个月的会员注册数量
            List<Integer> memberCount = memberServcie.findMemberCountByMoth(months);
            Map<String,Object> map = new HashMap<>();
            map.put("months",months);
            map.put("memberCount",memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
    @RequestMapping(value = "/getSetmealReport")
    public Result getSetmealReport(){
        try {
            // 查询当前套餐对应的预约人数的数量

            List<Map> setmealCount = setmealService.getSetmealReport();
            List<String> setmealNames = new ArrayList<>();
            // 遍历setmealCount
            if (setmealCount !=null && setmealCount.size()>0){
                for (Map map : setmealCount) {
                  String name= (String) map.get("name");
                  setmealNames.add(name);
                }
            }
            Map<String,Object> map =new HashMap<>();
            map.put("setmealNames",setmealNames);
            map.put("setmealCount",setmealCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }
    // 运营数据统计报表
    @RequestMapping(value = "/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> map = reportService.getBusinessReportData();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
    // 运营数据统计报表（导出excel）
    @RequestMapping(value = "/exportBusinessReport")
    public Result getBusinessReportData(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String,Object> map = reportService.getBusinessReportData();
                    String reportDate = (String)map.get("reportDate");
                    Integer todayNewMember = (Integer) map.get("todayNewMember");
                    Integer totalMember = (Integer) map.get("totalMember");
                    Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
                    Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
                    Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
                    Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
                    Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
                    Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
                    Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
                    Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
                    List<Map> hotSetmeal = (List<Map>)map.get("hotSetmeal");
            // 2：读取Excel文件(项目路径/template/report_template.xlsx)，将数据存放到Excel文件中指定的单元格中（Cell）
            String path = request.getSession().getServletContext().getRealPath("/template")+ File.separator+"report_template.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(path)));
            //获取第一个Sheet对象
            XSSFSheet sheet = workbook.getSheetAt(0);
            // 获取日期
            // 获取行
            XSSFRow row = sheet.getRow(2);
            // 获取单元格，并赋值
            row.getCell(5).setCellValue(reportDate);
            // 获取新增会员数，总会员数
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            // 热门套餐
            int rowCount =12;
            for (Map map1 : hotSetmeal) {
                // 套餐名称
                String name = (String) map1.get("name");
                Long setmeal_count = (Long) map1.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map1.get("proportion");
                // 获取行
                row = sheet.getRow(rowCount++);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmeal_count);
                row.getCell(6).setCellValue(proportion.toString());
            }
            // 3：将excel文件（workbook）写到输出流中,默认是文本（.txt)
            ServletOutputStream out = response.getOutputStream();
            // 设置下载文件的类型
            response.setContentType("application/vnd.ms-excel");
            // 设置响应头（处理下载附件的形式,1:attachment(表示附件),2:inline(表示内连)）
            response.setHeader("Content-Disposition","attachment;filename=export83.xls");
            workbook.write(out);
            out.flush();
            out.close();
            workbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    /**
     * 获取指定时间内的会员数量统计, 折线图
     * @DateTimeFormat
     * @return
     */
    @PostMapping("/getMemberDateReport")
    public Result getMemberDateReport(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        Map<String,List<Object>> map = memberServcie.findMemberCountByMonthDate2(startDate,endDate);
        if (map == null){
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }
    /**
     * 获取指定时间内的会员数量统计, 折线图
     * 查询的是每月新增的会员
     * @DateTimeFormat
     * @return
     */
    @PostMapping("/getMemberDateReport2")
    public Result getMemberDateReport2(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate){
        Map<String,List<Object>> map = memberServcie.findMemberCountByMonthDate(startDate,endDate);
        if (map == null){
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }
    /**
     * 会员数量统计,男女会员占比,饼形图
     * @return
     */
    @GetMapping("/getMemberGenderReport")
    public Result getMemberGenderReport(){
        Map<String,Object> map = memberServcie.findMemberGender();
        if (map == null) {
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }
    /**
     * 根据年龄段获取会员数量统计, 饼形图
     * 按照会员的年龄段（可以指定几个年龄段，例如0-18、18-30、30-45、45以上）来展示各个年龄段的占比
     * @return
     */
    @PostMapping("/getMemberAgeReport")
    public Result getMemberAgeReport(){
        Map<String,Object> map = memberServcie.findMemberAge();
        if (map == null) {
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }
}
