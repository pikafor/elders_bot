package org.example.Controller;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ExelController {
    private void setRegionBorderWithMedium(CellRangeAddress region, Sheet sheet) {
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderLeft(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, region, sheet);
        RegionUtil.setBorderTop(BorderStyle.MEDIUM, region, sheet);
    }
    public void write(String fileName, ResultSetMetaData resultSetMetaData, ResultSet resultSet, ResultSetMetaData resultSetMetaData2, ResultSet resultSet2, int lastId) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Новый лист");
        XSSFRow row = sheet.createRow(0);
        XSSFRow row1 = sheet.createRow(1);
        XSSFRow row3 = sheet.createRow(2);
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 2));

        try {
            row.createCell(0).setCellValue("№");
            setRegionBorderWithMedium(CellRangeAddress.valueOf("A1:A3"), sheet);
            row.createCell(1).setCellValue("ФИО");
            setRegionBorderWithMedium(CellRangeAddress.valueOf("B1:C3"), sheet);
            int _row = 3;
            for (int i = 3; i < resultSetMetaData.getColumnCount(); i++) {
                row.createCell(_row).setCellValue(resultSetMetaData.getColumnName(i + 1));
                sheet.addMergedRegion(new CellRangeAddress(0, 0, _row, _row + 1));
                setRegionBorderWithMedium(new CellRangeAddress(0, 0, _row, _row + 1), sheet);
                resultSet2.next();
                row1.createCell(_row).setCellValue(resultSet2.getString(3));
                sheet.addMergedRegion(new CellRangeAddress(1, 1, _row, _row + 1));
                setRegionBorderWithMedium(new CellRangeAddress(1, 1, _row, _row + 1), sheet);
                for (int j = 0; j < 2; j++) {
                    row3.createCell(_row + j).setCellValue((j % 2 == 0) ? "1 час" : "2 час");
                    setRegionBorderWithMedium(new CellRangeAddress(2, 2, _row, _row + j), sheet);
                }

                _row += 2;
            }

            int count = 2;
            // i = 4;
            while (resultSet.next()) {
                count++;
                XSSFRow row2 = sheet.createRow(count);
                row2.createCell(0).setCellValue(resultSet.getString(1));
                setRegionBorderWithMedium(new CellRangeAddress(count, count, 0, 1), sheet);
                row2.createCell(1).setCellValue(resultSet.getString(2) + " " + resultSet.getString(3));
                sheet.addMergedRegion(new CellRangeAddress(count, count, 1, 2));
                setRegionBorderWithMedium(new CellRangeAddress(count, count, 1, 2), sheet);
                int columnCount = 3;
                for (int i = 3; i < resultSetMetaData.getColumnCount(); i++) {
                    for (int j = 0; j < 2; j++) {
                        row2.createCell(columnCount + j).setCellValue(resultSet.getString(i + 1));
                        setRegionBorderWithMedium(new CellRangeAddress(count, count, columnCount, columnCount + j), sheet);
                    }
                    columnCount += 2;
                }
            }
            setRegionBorderWithMedium(new CellRangeAddress(0, 5, 0, resultSetMetaData.getColumnCount() + 1), sheet);
            FileOutputStream fileOutput = new FileOutputStream("C:\\Users\\Иван\\Desktop\\write.xlsx");
            workbook.write(fileOutput);
            fileOutput.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
