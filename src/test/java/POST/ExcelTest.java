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
    //declaring user credentials as static to use further
    public static String name;
    public static String gender;
    public static String email;
    public static String status;
    @Test(priority = 1)
    //reading data from excel file
    public ArrayList readUser() throws IOException {
        String excelFilePath = "src\\main\\resources\\POSTDATA.xlsx";
        //reading data from the Excel using Apache POI method
        FileInputStream input = new FileInputStream(excelFilePath);
        XSSFWorkbook wb = new XSSFWorkbook(input);
        XSSFSheet sheet = wb.getSheetAt(0);
        //taking data-formatter :formatting the value stored in cell
        DataFormatter dataFormatter = new DataFormatter();
        int rows = sheet.getLastRowNum() + 1;
        for (int i = 1; i < rows; i++) {
            XSSFRow row = sheet.getRow(i);
            //reading credentials using their respective cells of Excel file
            name = row.getCell(0).getStringCellValue();
            gender = row.getCell(1).getStringCellValue();
            email = dataFormatter.formatCellValue(sheet.getRow(i).getCell(2));
            status= row.getCell(3).getStringCellValue();
        }
        //taking all the credentials into arraylist:using Collections
        ArrayList al = new ArrayList();
        al.add(name);
        al.add(gender);
        al.add(email);
        al.add(status);
        return al;
    }
}


