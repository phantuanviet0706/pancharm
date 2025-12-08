package com.example.pancharm.controller;

import com.example.pancharm.dto.response.auth.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@RestController
@RequestMapping("uploadFile")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> debugUpload(
            @RequestParam("files") MultipartFile[] files
    ) {
        System.out.println("Received " + files.length + " files");
        long totalSize = Arrays.stream(files).mapToLong(MultipartFile::getSize).sum();
        System.out.println("Total size = " + totalSize);
        return ApiResponse.<String>builder().result("ok").build();
    }
}
