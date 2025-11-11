// service/base/MinioService.java
package com.example.pancharm.service.base;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.exception.AppException;

import io.minio.*;
import io.minio.errors.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MinioService {
    MinioClient minio;

    @NonFinal
    @Value("${appInfo.minio.bucket-name}")
    String bucket;

    @NonFinal
    @Value("${appInfo.minio.public-base-url}")
    String publicBaseUrl;

    public String uploadFile(MultipartFile file, String folder, String objectName) {
        try {
            ensureBucket();

            if (objectName.isEmpty()) {
                objectName = (folder == null || folder.isBlank() ? "" : folder + "/") + file.getOriginalFilename();
            }

            try (InputStream is = file.getInputStream()) {
                PutObjectArgs args = PutObjectArgs.builder().bucket(bucket).object(objectName).stream(
                                is, file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build();

                minio.putObject(args);
            }

            // Trả về URL public (tùy cách bạn expose – có thể là Nginx/CDN).
            String base = publicBaseUrl.endsWith("/")
                    ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1)
                    : publicBaseUrl;
            return base + "/" + bucket + "/" + objectName;

        } catch (Exception e) {
            throw new AppException(ErrorCode.MINIO_UPLOAD_ERROR); // có thể định nghĩa MINIO_UPLOAD_ERROR riêng
        }
    }

    public void deleteFile(String fileURL) {
        try {
            String base = (publicBaseUrl.endsWith("/")
                            ? publicBaseUrl.substring(0, publicBaseUrl.length() - 1)
                            : publicBaseUrl)
                    + "/" + bucket + "/";
            if (!fileURL.startsWith(base)) {
                // Nếu URL khác domain, tự bóc objectKey theo quy ước của bạn
                throw new AppException(ErrorCode.MINIO_INVALID_URL);
            }
            String objectKey = fileURL.substring(base.length());

            RemoveObjectArgs args =
                    RemoveObjectArgs.builder().bucket(bucket).object(objectKey).build();
            minio.removeObject(args);
        } catch (Exception e) {
            throw new AppException(ErrorCode.MINIO_DELETE_ERROR); // có thể đổi tên mã lỗi cho đúng ngữ cảnh
        }
    }

    private void ensureBucket() throws Exception {
        boolean exists =
                minio.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!exists) {
            minio.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }

        String policy =
                """
		{
		"Version":"2012-10-17",
		"Statement":[
			{
			"Effect":"Allow",
			"Principal":"*",
			"Action":["s3:GetObject"],
			"Resource":["arn:aws:s3:::%s/*"]
			}
		]
		}
		"""
                        .formatted(bucket);

        minio.setBucketPolicy(
                SetBucketPolicyArgs.builder().bucket(bucket).config(policy).build());
    }
}
