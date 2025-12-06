// util/ImageUtil.java
package com.example.pancharm.util;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.example.pancharm.constant.FileConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.pancharm.common.contract.ImageAttachable;
import com.example.pancharm.common.contract.ImageEntity;
import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.service.base.MinioService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImageUtil {

    // Đổi sang MinioService
    private final MinioService minioService;

    /**
     * Attach nhiều ảnh vào entity cha.
     * Giữ nguyên logic cũ, chỉ thay upload bằng MinIO.
     */
    public <E extends ImageEntity<T>, T extends ImageAttachable<E>> void attachImages(
            Set<MultipartFile> files,
            T parent,
            String folder,
            Function<String, E> imageBuilder,
            Consumer<Set<E>> saveAllFunc) {

        if (files == null || files.isEmpty()) {
            return;
        }

        Set<E> images = files.stream()
                .map(file -> {
                    String url = minioService.uploadFile(file, folder + "/" + parent.getId(), "");
                    E image = imageBuilder.apply(url);
                    image.setPath(url);
                    image.setParent(parent);
                    return image;
                })
                .collect(Collectors.toSet());

        saveAllFunc.accept(images);
        parent.setImages(images);
    }

    public <E extends ImageEntity<T>, T extends ImageAttachable<E>> void attachImages(
            Set<MultipartFile> files,
            T parent,
            String folder,
            Function<String, E> imageBuilder,
            Consumer<Set<E>> saveAllFunc,
            String allowExtension) {

        if (files == null || files.isEmpty()) {
            return;
        }

        Set<E> images = files.stream()
                .map(file -> {
                    String url = minioService.uploadFile(file, folder + "/" + parent.getId(), "");
                    if (!isValidFilePath(url, allowExtension)) {
                        throw new AppException(ErrorCode.FILE_PATH_NOT_ALLOWED);
                    }
                    E image = imageBuilder.apply(url);
                    image.setPath(url);
                    image.setParent(parent);
                    return image;
                })
                .collect(Collectors.toSet());

        saveAllFunc.accept(images);
        parent.setImages(images);
    }

    /**
     * Xoá file theo path (URL public).
     */
    public void deletePaths(String path) {
        try {
            minioService.deleteFile(path);
        } catch (Exception e) {
            // Giữ nguyên ErrorCode để không phải sửa nơi khác
            throw new AppException(ErrorCode.MINIO_DELETE_ERROR);
        }
    }

    /**
     * Upsert 1 file (ví dụ avatar): nếu có file mới -> replace (xoá cũ, upload mới).
     * Nếu không có file mới nhưng removeRequested=true -> xoá file hiện tại.
     *
     * @param file                 file mới (có thể null)
     * @param parent               entity cha (để build đường dẫn)
     * @param folderResolver       tạo folder upload từ parent (vd: c -> "companies/" + c.getId() + "/avatar")
     * @param currentPathSupplier  lấy path đang lưu trong DB (vd: company::getAvatarPath)
     * @param setPath              set path mới về DB (vd: company::setAvatarPath)
     * @param removeRequested      người dùng yêu cầu xoá (dù không upload file mới)
     * @return                     path sau cùng (có thể null)
     */
    public <T> String upsertSingleFile(
            MultipartFile file,
            T parent,
            Function<T, String> folderResolver,
            Supplier<String> currentPathSupplier,
            Consumer<String> setPath,
            boolean removeRequested) {

        final String currentPath = currentPathSupplier.get();

        if (file != null && !file.isEmpty()) {
            if (StringUtils.hasText(currentPath)) {
                deletePaths(currentPath);
            }
            final String folder = folderResolver.apply(parent);
            final String url = minioService.uploadFile(file, folder, "");
            setPath.accept(url);
            return url;
        }

        if (removeRequested) {
            if (StringUtils.hasText(currentPath)) {
                deletePaths(currentPath);
            }
            setPath.accept(null);
            return null;
        }

        return currentPath;
    }

    public <T> String upsertSingleFile(
            MultipartFile file,
            T parent,
            Function<T, String> folderResolver,
            Supplier<String> currentPathSupplier,
            Consumer<String> setPath,
            boolean removeRequested,
            String allowExtension) {

        final String currentPath = currentPathSupplier.get();

        if (file != null && !file.isEmpty()) {
            if (StringUtils.hasText(currentPath)) {
                deletePaths(currentPath);
            }
            final String folder = folderResolver.apply(parent);
            final String url = minioService.uploadFile(file, folder, "");
            if (!isValidFilePath(url, allowExtension)) {
                throw new AppException(ErrorCode.FILE_PATH_NOT_ALLOWED);
            }
            setPath.accept(url);
            return url;
        }

        if (removeRequested) {
            if (StringUtils.hasText(currentPath)) {
                deletePaths(currentPath);
            }
            setPath.accept(null);
            return null;
        }

        return currentPath;
    }

    /**
     * Overload nếu muốn truyền prefix + idGetter (vd: "companies/{id}/avatar").
     */
    public <T, ID> String upsertSingleFile(
            MultipartFile file,
            T parent,
            String folderPrefix,
            Function<T, ID> idGetter,
            String leafFolder, // ví dụ "avatar" hoặc "bank"
            Supplier<String> currentPathSupplier,
            Consumer<String> setPath,
            boolean removeRequested) {

        return upsertSingleFile(
                file,
                parent,
                p -> folderPrefix + "/" + idGetter.apply(p) + "/" + (leafFolder == null ? "" : leafFolder),
                currentPathSupplier,
                setPath,
                removeRequested);
    }

    /**
     * Xoá 1 file đang có (nếu tồn tại) và set null vào DB.
     */
    public void removeSingleFile(Supplier<String> currentPathSupplier, Consumer<String> setPath) {
        final String currentPath = currentPathSupplier.get();
        if (StringUtils.hasText(currentPath)) {
            deletePaths(currentPath);
        }
        setPath.accept(null);
    }

    private boolean isValidFilePath(String path, String allowedExtensions) {
        if (path == null || path.isEmpty()) {
            return false;
        }

        String regex = ".*\\.(?:" + allowedExtensions + ")$";

        return path.toLowerCase().matches(regex);
    }
}
