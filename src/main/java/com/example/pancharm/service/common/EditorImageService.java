package com.example.pancharm.service.common;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.pancharm.service.base.MinioService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EditorImageService {

    private final MinioService minioService;

    // ================== UPLOAD VÀO DRAFT ==================

    public String uploadDraftImage(MultipartFile file, String draftId) {
        // folder: draft/{draftId}
        String folder = "draft/" + draftId;

        String cleanName = Objects.requireNonNull(file.getOriginalFilename())
                .trim()
                .replaceAll("\\s+", "_");

        String objectName = folder + "/" + System.currentTimeMillis() + "-" + cleanName;

        // Dùng MinioService upload
        return minioService.uploadFile(file, null, objectName);
        // lưu ý: vì objectName đã đầy đủ "draft/xxx/...", nên folder = null cũng được.
    }

    // ================== KHI SUBMIT FORM PRODUCT ==================

    /**
     * - Nhận HTML đang chứa URL draft
     * - Move file từ `draft/{draftId}/...` sang `products/{productId}/...`
     * - Replace URL trong HTML
     * - Trả về HTML final
     */
    public String moveDraftImagesAndRewriteHtml(String html, String draftId, Integer productId) {
        if (html == null || html.isBlank() || draftId == null || draftId.isBlank()) {
            return html;
        }

        // Regex bắt <img src="xxx">
        Pattern pattern = Pattern.compile("<img[^>]+src=[\"']([^\"']+)[\"'][^>]*>");
        Matcher matcher = pattern.matcher(html);

        Map<String, String> replaceMap = new LinkedHashMap<>();

        while (matcher.find()) {
            String srcUrl = matcher.group(1);

            // Chỉ xử lý ảnh thuộc draftId này
            String baseDraftPart = "/draft/" + draftId + "/";
            if (!srcUrl.contains(baseDraftPart)) continue;

            // Lấy objectKey cũ từ URL
            String oldKey = minioService.extractObjectKeyFromUrl(srcUrl);
            // oldKey ví dụ: draft/{draftId}/123456-filename.png

            // Lấy filename
            String filename = oldKey.substring(oldKey.lastIndexOf('/') + 1);

            // Key mới: products/{productId}/filename
            String newKey = "products/" + productId + "/" + filename;

            // Copy rồi trả về URL mới
            String newUrl = minioService.copyObject(oldKey, newKey);

            // Xoá bản draft
            minioService.deleteFile(srcUrl);

            // Map để replace trong HTML
            replaceMap.put(srcUrl, newUrl);
        }

        // Replace trong HTML
        String updatedHtml = html;
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            updatedHtml = updatedHtml.replace(entry.getKey(), entry.getValue());
        }

        return updatedHtml;
    }
}
