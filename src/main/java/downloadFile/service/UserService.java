package downloadFile.service;

import downloadFile.entity.User;
import downloadFile.entity.UserSearch;
import downloadFile.repository.UserRepoistory;
import downloadFile.repository.UserSpec;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    @Autowired
    private UserRepoistory userRepoistory;

    public void generateExcel(HttpServletResponse response) throws IOException {

        List<User> users = userRepoistory.findAll();
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Users Info");
        HSSFRow row = sheet.createRow(0);


        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("FirstName");
        row.createCell(2).setCellValue("LastName");
        row.createCell(3).setCellValue("Email");
        row.createCell(4).setCellValue("Gender");

        int dataRowIndex = 1;
        for (User user : users) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(user.getId());
            dataRow.createCell(1).setCellValue(user.getFirstName());
            dataRow.createCell(2).setCellValue(user.getLastName());
            dataRow.createCell(3).setCellValue(user.getEmail());
            dataRow.createCell(4).setCellValue(user.getGender());
            dataRowIndex++;

        }
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();

    }

    @Async(value = "taskExecutor")
    public CompletableFuture<Page<User>> findByUserSpecAndPage(HttpServletResponse response, int pageNum, int pageSize, UserSearch search) throws IOException {
//        List<User> users = userRepoistory.findAll(new UserSpec(search));
//        UserSpec spec = new UserSpec(search);

//        List<User> users = userRepoistory.findAll(spec);   // Create a new Excel workbook and sheet

        Pageable page = PageRequest.of(pageNum, pageSize);
        UserSpec spec = new UserSpec(search);
        Page <User> users =  userRepoistory.findAll(spec, page);

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Users Info");
        HSSFRow row = sheet.createRow(0);

        // Add column headers to the Excel sheet
        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("FirstName");
        row.createCell(2).setCellValue("LastName");
        row.createCell(3).setCellValue("Email");
        row.createCell(4).setCellValue("Gender");

        int dataRowIndex = 1;
        for (User user : users) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            dataRow.createCell(0).setCellValue(user.getId());
            dataRow.createCell(1).setCellValue(user.getFirstName());
            dataRow.createCell(2).setCellValue(user.getLastName());
            dataRow.createCell(3).setCellValue(user.getEmail());
            dataRow.createCell(4).setCellValue(user.getGender());
            dataRowIndex++;
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
        return CompletableFuture.completedFuture(users);
    }

//
//    public void findByUserSpec(HttpServletResponse response,UserSearch search) throws IOException {
//        UserSpec spec = new UserSpec(search);
//
//        List<User> users = userRepoistory.findAll(spec);
//        HSSFWorkbook workbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Users Info");
//        HSSFRow row = sheet.createRow(0);
//
//
//        row.createCell(0).setCellValue("ID");
//        row.createCell(1).setCellValue("FirstName");
//        row.createCell(2).setCellValue("LastName");
//        row.createCell(3).setCellValue("Email");
//        row.createCell(4).setCellValue("Gender");
//
//        int dataRowIndex = 1;
//        for (User user : users) {
//            HSSFRow dataRow = sheet.createRow(dataRowIndex);
//            dataRow.createCell(0).setCellValue(user.getId());
//            dataRow.createCell(1).setCellValue(user.getFirstName());
//            dataRow.createCell(2).setCellValue(user.getLastName());
//            dataRow.createCell(3).setCellValue(user.getEmail());
//            dataRow.createCell(4).setCellValue(user.getGender());
//            dataRowIndex++;
//
//        }
//        ServletOutputStream ops = response.getOutputStream();
//        workbook.write(ops);
//        workbook.close();
//        ops.close();
//
//    }


    //    public List<User>findByUserSpec(UserSearch search){
//        UserSpec spec = new UserSpec(search);
//        return userRepoistory.findAll(spec);
//    }

    @Async(value = "taskExecutor")
    public CompletableFuture<Page<User>> findByUserSpec(int pageNum, int pageSize, UserSearch search) {
        Pageable page = PageRequest.of(pageNum, pageSize);
        UserSpec spec = new UserSpec(search);
        return CompletableFuture.completedFuture(userRepoistory.findAll(spec, page));

    }
    public List<User> getUsersByCatygory(Long catygoryId) {
        Specification<User> spec = UserSpec.hasAuthor(catygoryId);
        return userRepoistory.findAll(spec);
    }
}