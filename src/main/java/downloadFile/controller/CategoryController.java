package downloadFile.controller;

import downloadFile.entity.Category;
import downloadFile.entity.User;
import downloadFile.entity.UserSearch;
import downloadFile.repository.UserRepoistory;
import downloadFile.service.CategoryService;
import downloadFile.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
public class CategoryController {


    @Autowired
    private UserService reportService;
    @Autowired
    private UserRepoistory userRepoistory;
    @Autowired
    private CategoryService categoryService;

    @PostMapping("/saveCategory")
    public ResponseEntity<Category>saveCategory(@RequestBody Category category){
        return new ResponseEntity<>(categoryService.saveCategory(category), HttpStatus.CREATED);
    }

//    @Async(value = "taskExecutor")
//    @PostMapping("/specdown")
//    public CompletableFuture<Page<User>> reportData(HttpServletResponse response, @RequestBody UserSearch search, @RequestParam int pageNum, @RequestParam int pageSize) throws IOException, IOException {
//
//        response.setContentType("application/octet-stream");
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment;filename=users.xls";
//        response.setHeader(headerKey,headerValue);
//      return .findByUserSpec(response, pageNum, pageSize, search);
//
//    }
}