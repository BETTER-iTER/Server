package com.example.betteriter.infra.s3;

import static com.example.betteriter.global.common.code.status.ErrorStatus._IMAGE_FILE_NAME_IS_NOT_EXIST;
import static com.example.betteriter.global.common.code.status.ErrorStatus._IMAGE_FILE_UPLOAD_FAILED;

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
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service implements ImageUploadService {

    private static final String FOLDER = "iter";

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public ReviewImage uploadImage(MultipartFile image, Review review, int orderNum) {
        String originalFilename = Optional.ofNullable(image.getOriginalFilename())
            .orElseThrow(() -> new ReviewHandler(_IMAGE_FILE_NAME_IS_NOT_EXIST));

        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString();
        String key = FOLDER + "/" + review.getId().toString() + "/" + fileName + fileExtension;

        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();

        try (InputStream inputStream = image.getInputStream()) {
            s3Client.putObject(request, RequestBody.fromInputStream(inputStream, image.getSize()));
        } catch (Exception e) {
            throw new ReviewHandler(_IMAGE_FILE_UPLOAD_FAILED);
        }

        return ReviewImage.builder()
            .review(review)
            .imgUrl(getImageUrl(key))
            .orderNum(orderNum)
            .build();
    }


    private String getImageUrl(String key) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }
}
