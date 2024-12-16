package com.example.coffee_shop_chain_management.service;
import com.example.coffee_shop_chain_management.entity.Timesheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelExporter {

    public static File exportTimesheetsToExcel(List<Timesheet> timesheets, String filePath) throws IOException {
        // Tạo workbook và sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Timesheets");

        // Tạo dòng tiêu đề
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Timesheet ID", "Employee ID", "Date", "Shift", "Branch ID"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Ghi dữ liệu từ danh sách timesheets
        int rowNum = 1;
        for (Timesheet timesheet : timesheets) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(timesheet.getTimesheetID());
            row.createCell(1).setCellValue(timesheet.getEmployee().getEmployeeID());
            row.createCell(2).setCellValue(timesheet.getDate().toString());
            row.createCell(3).setCellValue(timesheet.getShift());
            row.createCell(4).setCellValue(timesheet.getEmployee().getBranch().getBranchID());
        }

        // Ghi dữ liệu ra file
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }

        // Đóng workbook
        workbook.close();

        return file;
    }
}
