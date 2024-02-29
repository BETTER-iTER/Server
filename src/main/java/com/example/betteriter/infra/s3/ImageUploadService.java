package com.example.betteriter.infra.s3;

import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.domain.ReviewImage;
import com.example.betteriter.fo_domain.user.domain.Users;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

    ReviewImage uploadImage(MultipartFile image, Review review, int orderNum);

    String uploadImage(MultipartFile image, Users user);

    void updateImage(MultipartFile multipartFile, ReviewImage reviewImage);

    void deleteImages(String imageUrl);
}
