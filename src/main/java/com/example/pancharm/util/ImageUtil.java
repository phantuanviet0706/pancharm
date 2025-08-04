package com.example.pancharm.util;

import com.example.pancharm.common.contract.ImageAttachable;
import com.example.pancharm.common.contract.ImageEntity;
import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.product.ProductImageRequest;
import com.example.pancharm.entity.ProductImages;
import com.example.pancharm.entity.Products;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.service.base.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ImageUtil {
	private final S3Service s3Service;

	public <E extends ImageEntity<T>, T extends ImageAttachable<E>> void attachImages(
			Set<MultipartFile> files,
			T parent,
			String folder,
			Function<String, E> imageBuilder,
			Consumer<Set<E>> saveAllFunc
	) {
		Set<E> images = files.stream()
				.map(file -> {
					String url = s3Service.uploadFile(file, folder + "/" + parent.getId());
					E image = imageBuilder.apply(url);
					image.setPath(url);
					image.setParent(parent);
					return image;
				}).collect(Collectors.toSet());

		saveAllFunc.accept(images);
		parent.setImages(images);
	}

	public void deletePaths(String path) {
		try {
			s3Service.deleteFile(path);
		} catch (Exception e) {
			throw new AppException(ErrorCode.AWS_S3_DELETE_ERROR);
		}
	}
}
