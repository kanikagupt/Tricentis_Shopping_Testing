package utilities;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {
    public static Object[][] getTestData(String sheetName) {
        try {
            FileInputStream file = new FileInputStream("src/test/resources/TestData.xlsx");
            Workbook book = new XSSFWorkbook(file);
            Sheet sheet = book.getSheet(sheetName);
            
            if (sheet == null) {
                book.close();
                return new Object[0][0];
            }

            int rows = sheet.getLastRowNum();
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                book.close();
                return new Object[0][0];
            }
            
            int cols = headerRow.getLastCellNum();
            DataFormatter formatter = new DataFormatter();
            List<Object[]> validRows = new ArrayList<>();

            for (int i = 1; i <= rows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                Cell firstCell = row.getCell(0);
                String tcId = (firstCell == null) ? "" : formatter.formatCellValue(firstCell).trim();
                
                // Only process rows that have a valid Test Case ID (e.g. TC_001)
                if (tcId.isEmpty()) continue;

                Object[] rowData = new Object[cols];
                for (int j = 0; j < cols; j++) {
                    Cell cell = row.getCell(j);
                    rowData[j] = (cell == null) ? "" : formatter.formatCellValue(cell).trim();
                }
                validRows.add(rowData);
            }
            
            book.close();
            file.close();
            
            Object[][] data = new Object[validRows.size()][cols];
            for (int k = 0; k < validRows.size(); k++) {
                data[k] = validRows.get(k);
            }
            return data;
            
        } catch (Exception e) { 
            e.printStackTrace(); 
            return new Object[0][0]; 
        }
    }
}