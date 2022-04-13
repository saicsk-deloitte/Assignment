package POST;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
public class ExcelTest {
    public static String name;
    public static String gender;
    public static String email;
    public static String status;
    @Test(priority = 1)
    public ArrayList readCells() throws IOException {
        String excelFilePath = "C:\\Users\\Chamkumar\\Desktop\\POSTDATA.xlsx";
        //reading data from the Excel using Apache POI method
        FileInputStream input = new FileInputStream(excelFilePath);
        XSSFWorkbook wb = new XSSFWorkbook(input);
        XSSFSheet sheet = wb.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        int rows = sheet.getLastRowNum() + 1;
        for (int i = 1; i < rows; i++) {
            XSSFRow row = sheet.getRow(i);
            name = row.getCell(0).getStringCellValue();
            gender = row.getCell(1).getStringCellValue();
            email = dataFormatter.formatCellValue(sheet.getRow(i).getCell(2));
            status= row.getCell(3).getStringCellValue();
        }
        ArrayList al = new ArrayList();
        al.add(name);
        al.add(gender);
        al.add(email);
        al.add(status);
        return al;
    }
}


