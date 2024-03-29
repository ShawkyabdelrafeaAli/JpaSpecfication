package downloadFile.controller;

import downloadFile.entity.User;
import downloadFile.entity.UserSearch;
import downloadFile.repository.UserRepoistory;
import downloadFile.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.awt.print.Book;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserService  userService;

    @Autowired
    private UserRepoistory userRepoistory;
    @PostMapping("/saveUser")
    public ResponseEntity<User>saveUser(@RequestBody User user){
        return new ResponseEntity<>(userRepoistory.save(user), HttpStatus.CREATED);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<User>> getUsersByCategoryJoin(@PathVariable Long categoryId) {
        List<User> users = userService.getUsersByCatygory(categoryId);
        return ResponseEntity.ok(users);
    }
        @GetMapping("/downloadExcel")
    public void  generateExcelReport(HttpServletResponse response ) throws IOException {

     response.setContentType("application/octet-stream");
     String headerKey = "Content-Disposition";
     String headerValue = "attachment;filename=users.xls";
     response.setHeader(headerKey,headerValue);
            userService.generateExcel(response);
    }


    @Async(value = "taskExecutor")
    @PostMapping("/export-users")
    public void exportUsersToExcel(HttpServletResponse response, @RequestParam int pageNum, @RequestParam int pageSize,@RequestBody UserSearch search) throws Exception {
        List<CompletableFuture<Page<User>>> futures = new ArrayList<>();
        // Submit multiple asynchronous tasks to fetch users
        for (int i = 0; i < 5; i++) {
            futures.add(userService.findByUserSpecAndPage(response, pageNum + i, pageSize, search));
        }

        // Wait for all tasks to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // Merge results
        List<User> allUsers = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(page -> page.getContent().stream())
                .collect(Collectors.toList());

        // Export merged users to Excel
        userService.exportUsersToExcel(response, allUsers);
    }






//
//     @Async(value = "taskExecutor")
//     @PostMapping (value = "/excel-exportWIthSpec" , produces = MediaType.APPLICATION_JSON_VALUE)
//      public CompletableFuture  <ResponseEntity> exportToExcel(HttpServletResponse response,@RequestBody  UserSearch search, @RequestParam int pageNum, @RequestParam int pageSize ) throws IOException {
////        response.setContentType("application/octet-stream");
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment;filename=users.xls";
//        response.setHeader(headerKey, headerValue);
//        return userService.findByUserSpecAndPage(response,pageNum, pageSize, search).thenApply(ResponseEntity::ok);
//    }

    @Async(value = "taskExecutor")
    @PostMapping("/specOnly")
    public CompletableFuture  <ResponseEntity>findByUserSpec(@RequestBody UserSearch search , @RequestParam int pageNum, @RequestParam int pageSize) {
        return userService.findByUserSpec(pageNum, pageSize, search).thenApply(ResponseEntity::ok);
    }


    }


