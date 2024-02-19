package downloadFile.Util;

import downloadFile.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.util.List;

public class ExcelGenerator {

        private List<User> listRecords;
        private XSSFWorkbook workbook;
        private XSSFSheet sheet;

        public ExcelGenerator(List<User> listRecords) {
            this.listRecords = listRecords;
            workbook = new XSSFWorkbook();
        }

        private void writeHeader() {
            sheet = workbook.createSheet("Exam Records");

            Row row = sheet.createRow(0);

            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            font.setFontHeight(16);
            style.setFont(font);

            createCell(row, 0, "ID", style);
            createCell(row, 1, "FirstName", style);
            createCell(row, 2, "LastName", style);
            createCell(row, 3, "Email", style);
            createCell(row, 3, "Gender", style);

        }

        private void createCell(Row row, int columnCount, Object value, CellStyle style) {
            sheet.autoSizeColumn(columnCount);
            Cell cell = row.createCell(columnCount);
            if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            }
            else if (value instanceof Long) {
                cell.setCellValue((Long) value);
            } else if (value instanceof Boolean) {
                cell.setCellValue((Boolean) value);
            } else {
                cell.setCellValue((String) value);
            }
            cell.setCellStyle(style);
        }

        private void write() {
            int rowCount = 1;

            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(14);
            style.setFont(font);

            for (User record : listRecords) {
                Row row = sheet.createRow(rowCount++);
                int columnCount = 0;

                createCell(row, columnCount++, record.getId(), style);
                createCell(row, columnCount++, record.getFirstName(), style);
                createCell(row, columnCount++, record.getLastName(), style);
                createCell(row, columnCount++, record.getEmail(), style);
                createCell(row, columnCount++, record.getGender(), style);

            }
        }

        public void generate(HttpServletResponse response) throws IOException {
            writeHeader();
            write();
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        }
    }
