package com.example.betteriter.infra.s3;

import static com.example.betteriter.global.common.code.status.ErrorStatus._IMAGE_FILE_IS_NOT_EXIST;
import static com.example.betteriter.global.common.code.status.ErrorStatus._IMAGE_FILE_NAME_IS_NOT_EXIST;
import static com.example.betteriter.global.common.code.status.ErrorStatus._IMAGE_FILE_UPLOAD_FAILED;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.review.exception.ReviewHandler;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


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
        String originalFilename = Optional.ofNullable(image.getOriginalFilename())
            .orElseThrow(() -> new ReviewHandler(_IMAGE_FILE_NAME_IS_NOT_EXIST));

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString();
        String key = FOLDER + "/" + review.getId().toString() + "/" + fileName + fileExtension;

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(image.getContentType());

        try (InputStream inputStream = image.getInputStream()) {

            s3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetaData));

        } catch (Exception e) {
            throw new ReviewHandler(_IMAGE_FILE_UPLOAD_FAILED);
        }

        return ReviewImage.builder()
            .review(review)
            .imgUrl(getImageUrl(key))
            .orderNum(orderNum)
            .build();
    }

    @Override
    public void updateImage(MultipartFile multipartFile, ReviewImage reviewImage) {
        validateImageFileExists(multipartFile);

        String originalFilename = Optional.ofNullable(multipartFile.getOriginalFilename())
            .orElseThrow(() -> new ReviewHandler(_IMAGE_FILE_NAME_IS_NOT_EXIST));

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString();
        String key = FOLDER + "/" + reviewImage.getReview().getId().toString() + "/" + fileName + fileExtension;

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetaData));
        } catch (Exception e) {
            throw new ReviewHandler(_IMAGE_FILE_UPLOAD_FAILED);
        }

        reviewImage.updateImgUrl(getImageUrl(key));
    }

    private void validateImageFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new ReviewHandler(_IMAGE_FILE_IS_NOT_EXIST);
        }
    }


    private String getImageUrl(String key) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }
}
