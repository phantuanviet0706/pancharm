package com.example.pancharm.service.base;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class S3Service {
    AmazonS3 amazonS3;

    @NonFinal
    @Value("${appInfo.aws.s3.bucket-name}")
    String bucketName;

    public String uploadFile(MultipartFile file, String folder) {
        String fileName = folder + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            amazonS3.putObject(bucketName, fileName, file.getInputStream(), objectMetadata);
            return amazonS3.getUrl(bucketName, fileName).toString();
        } catch (IOException e) {
            throw new AppException(ErrorCode.AWS_S3_UPLOAD_ERROR);
        }
    }

    public void deleteFile(String fileURL) {
        try {
            String objectKey = extractObjectKeyFromUrl(fileURL);
            amazonS3.deleteObject(bucketName, objectKey);
        } catch (Exception e) {
            throw new AppException(ErrorCode.AWS_S3_DELETE_ERROR);
        }
    }

    private String extractObjectKeyFromUrl(String fileUrl) {
        String bucketUrl = amazonS3.getUrl(bucketName, "").toString();
        if (!fileUrl.startsWith(bucketUrl)) {
            throw new AppException(ErrorCode.AWS_S3_INVALID_URL);
        }
        return fileUrl.substring(bucketUrl.length());
    }
}
