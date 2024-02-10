package com.example.betteriter.infra.s3;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

    ReviewImage uploadImage(MultipartFile image, Review review, int orderNum);

    void updateImage(MultipartFile multipartFile, ReviewImage reviewImage);
}
