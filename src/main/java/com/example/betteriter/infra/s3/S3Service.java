package com.example.betteriter.infra.s3;

import static com.example.betteriter.global.common.code.status.ErrorStatus.*;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import com.example.betteriter.fo_domain.user.domain.Users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service implements ImageUploadService {

	private static final String FOLDER = "iter";

	private final AmazonS3 s3Client;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucketName;

	@Override
	public ReviewImage uploadImage(MultipartFile image, Review review, int orderNum) {
		String key = getReviewImageKey(image, review.getId());
		String imageUrl = getImageUrl(image, key);

		return ReviewImage.builder()
				.review(review)
				.imgUrl(imageUrl)
				.orderNum(orderNum)
				.build();
	}

	@Override
	public void updateImage(MultipartFile multipartFile, ReviewImage reviewImage) {
		validateImageFileExists(multipartFile);

		String key = this.getReviewImageKey(multipartFile, reviewImage.getReview().getId());
		String imageUrl = getImageUrl(multipartFile, key);

		reviewImage.updateImgUrl(imageUrl);
	}

	@Override
	public String uploadImage(MultipartFile image, Users user) {
		String key = getUserProfileImageKey(image, user);
		return getImageUrl(image, key);
	}

	@Override
	public String uploadTemporaryImage(MultipartFile image, Review review) {
		String key = getReviewImageKey(image, review.getId());
		return getImageUrl(image, key);
	}

	@Override
	public List<String> getReviewImageUrlsInS3(Review review) {
		log.info("1");
		ListObjectsV2Request request = new ListObjectsV2Request()
				.withBucketName(bucketName)
				.withPrefix(FOLDER + "/" + review.getId().toString() + "/");

		log.info("2");
		ListObjectsV2Result result = s3Client.listObjectsV2(request);

		log.info("3");
		List<S3ObjectSummary> objects = result.getObjectSummaries();

		log.info("4");
		return objects.stream()
				.map(S3ObjectSummary::getKey)
				.map(this::getImageUrl)
				.collect(Collectors.toList());
	}

	private static String getUserProfileImageKey(MultipartFile image, Users user) {
		String originalFilename = getFilename(image);

		String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String fileName = UUID.randomUUID().toString();

		return FOLDER + "/" + user.getEmail() + "/" + user.getId().toString() + "/" + fileName + fileExtension;
	}

	private String getReviewImageKey(MultipartFile image, Long reviewId) {
		String originalFilename = getFilename(image);

		String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String fileName = UUID.randomUUID().toString();

		return FOLDER + "/" + reviewId.toString() + "/" + fileName + fileExtension;
	}

	private static String getFilename(MultipartFile image) {
		return Optional.ofNullable(image.getOriginalFilename())
				.orElseThrow(() -> new ReviewHandler(_IMAGE_FILE_NAME_IS_NOT_EXIST));
	}

	@Override
	public void deleteImages(String imageUrl) {
		try {
			String key = imageUrl.substring(imageUrl.lastIndexOf(FOLDER));
			boolean isObjectExists = s3Client.doesObjectExist(bucketName, key);
			if (isObjectExists) {
				s3Client.deleteObject(bucketName, key);
			}
		} catch (SdkClientException e) {
			throw new ReviewHandler(_IMAGE_FILE_DELETE_FAILED);
		}
	}

	private String getImageUrl(String key) {
		return s3Client.getUrl(bucketName, key).toString();
	}

	private String getImageUrl(MultipartFile image, String key) {
		ObjectMetadata objectMetaData = new ObjectMetadata();
		objectMetaData.setContentType(image.getContentType());

		try (InputStream inputStream = image.getInputStream()) {
			s3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetaData));
		} catch (Exception e) {
			throw new ReviewHandler(_IMAGE_FILE_UPLOAD_FAILED);
		}

		return getImageUrl(key);
	}

	private void validateImageFileExists(MultipartFile multipartFile) {
		if (multipartFile.isEmpty()) {
			throw new ReviewHandler(_IMAGE_FILE_IS_NOT_EXIST);
		}
	}
}
