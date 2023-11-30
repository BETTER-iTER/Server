package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.bo_domain.menufacturer.service.ManufacturerService;
import com.example.betteriter.bo_domain.spec.domain.Spec;
import com.example.betteriter.bo_domain.spec.domain.SpecData;
import com.example.betteriter.bo_domain.spec.service.SpecService;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto.CreateReviewImageRequestDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewSpecResponseDto;
import com.example.betteriter.fo_domain.review.repository.ReviewImageRepository;
import com.example.betteriter.fo_domain.review.repository.ReviewRepository;
import com.example.betteriter.fo_domain.review.repository.ReviewSpecDataRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.constant.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.example.betteriter.global.constant.Category.LAPTOP;
import static com.example.betteriter.global.constant.RoleType.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith({MockitoExtension.class})
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private UserService userService;

    @Mock
    private SpecService specService;

    @Mock
    private ManufacturerService manufacturerService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewImageRepository reviewImageRepository;

    @Mock
    private ReviewSpecDataRepository reviewSpecDataRepository;

    @Test
    @DisplayName("리뷰을 리뷰 이미지와 함께 정상적으로 등록한다.")
    void createReviewTest() {
        // given
        CreateReviewRequestDto requestDto =
                CreateReviewRequestDto.builder()
                        .category(LAPTOP)
                        .productName("맥북1")
                        .boughtAt(LocalDate.now())
                        .manufacturerId(1L)
                        .amount(100000)
                        .storeName(1)
                        .shortReview("한줄 평")
                        .starPoint(1)
                        .goodPoint("goodPoint")
                        .badPoint("badPoint")
                        .specData(List.of(1L, 2L))
                        .images(List.of(CreateReviewImageRequestDto.builder().imgUrl("imgUrl1").build(),
                                CreateReviewImageRequestDto.builder().imgUrl("imgUrl2").build())
                        ).build();

        given(this.manufacturerService.findManufacturerById(anyLong()))
                .willReturn(Manufacturer.createManufacturer("삼성")); // 삼성

        given(this.specService.findAllSpecDataByIds(anyList()))
                .willReturn(List.of(
                        SpecData.builder()
                                .spec(Spec.createSpec(LAPTOP, "title1"))
                                .build(),
                        SpecData.builder()
                                .spec(Spec.createSpec(LAPTOP, "title2"))
                                .build()
                )); // specData


        given(this.reviewRepository.save(any(Review.class)))
                .willReturn(Review.builder()
                        .writer(Users.builder()
                                .oauthId("oauthId")
                                .email("danaver12@daum.net")
                                .password("password")
                                .roleType(ROLE_USER)
                                .isExpert(true)
                                .build())
                        .manufacturer(Manufacturer.createManufacturer("삼성"))
                        .category(LAPTOP)
                        .productName("상품명1")
                        .amount(10000)
                        .storeName(1)
                        .boughtAt(LocalDate.now())
                        .starPoint(1)
                        .shortReview("ShortReview")
                        .goodPoint("goodPoint")
                        .badPoint("badPoint")
                        .build());

        given(this.reviewSpecDataRepository.saveAll(anyList()))
                .willReturn(null);

        // when
        Long result = this.reviewService.createReview(requestDto);

        // then
        assertThat(result).isNotNull();
        verify(manufacturerService, times(1)).findManufacturerById(anyLong());
        verify(reviewRepository, times(1)).save(any());
        verify(reviewSpecDataRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("리뷰 등록시 카테고리에 해당하는 리뷰 스펙 데이터를 조회한다.")
    void getReviewSpecDataWhenCreateReview() {
        // given
        Category category = Category.PC;
        Spec spec01 = Spec.builder().category(category).title("title1").build();
        Spec spec02 = Spec.builder().category(category).title("title2").build();
        List<Spec> specs = List.of(spec01, spec02);

        given(this.specService.findAllSpecDataByCategory(any(Category.class)))
                .willReturn(specs);
        // when
        GetReviewSpecResponseDto reviewSpecResponseDto = this.reviewService.getReviewSpecDataResponse(category);
        // then
        assertThat(reviewSpecResponseDto.getSpecs()).isNotNull();
        assertThat(reviewSpecResponseDto.getSpecs()).hasSize(2);
    }
}
