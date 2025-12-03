package com.example.pancharm.controller;

import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.service.common.EditorImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EditorImageController {
    EditorImageService editorImageService;

    @PostMapping
    public ApiResponse<String> upload(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("draftId") String draftId) {
        return ApiResponse.<String>builder()
                .result(editorImageService.uploadDraftImage(file, draftId))
                .build();
    }
}
