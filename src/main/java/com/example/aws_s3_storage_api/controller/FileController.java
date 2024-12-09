package com.example.aws_s3_storage_api.controller;

import com.example.aws_s3_storage_api.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storage")
public class FileController {

    @Autowired
    private FileService fileService;

    // Search API
    @GetMapping("/search")
    public ResponseEntity<List<String>> searchFiles(
            @RequestParam String userName,
            @RequestParam String searchTerm) {
        List<String> results = fileService.searchFiles(userName, searchTerm);
        return ResponseEntity.ok(results);
    }

    // Download API
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(
            @RequestParam String userName,
            @RequestParam String fileName) {
        byte[] fileData = fileService.downloadFile(userName, fileName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }
}
