package com.example.betteriter.fo_domain.review.service;

import com.example.betteriter.bo_domain.menufacturer.domain.Manufacturer;
import com.example.betteriter.bo_domain.menufacturer.service.ManufacturerService;
import com.example.betteriter.bo_domain.spec.domain.Spec;
import com.example.betteriter.bo_domain.spec.domain.SpecData;
import com.example.betteriter.bo_domain.spec.service.SpecService;
import com.example.betteriter.fo_domain.review.domain.Review;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto;
import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto.CreateReviewImageRequestDto;
import com.example.betteriter.fo_domain.review.dto.GetCategoryReviewResponseDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewResponseDto;
import com.example.betteriter.fo_domain.review.dto.GetReviewSpecResponseDto;
import com.example.betteriter.fo_domain.review.repository.ReviewImageRepository;
import com.example.betteriter.fo_domain.review.repository.ReviewRepository;
import com.example.betteriter.fo_domain.review.repository.ReviewSpecDataRepository;
import com.example.betteriter.fo_domain.user.domain.Users;
import com.example.betteriter.fo_domain.user.domain.UsersDetail;
import com.example.betteriter.fo_domain.user.service.UserService;
import com.example.betteriter.global.constant.Category;
import com.example.betteriter.global.constant.Job;
import com.example.betteriter.global.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.time.LocalDate;
import java.util.List;

import static com.example.betteriter.global.constant.Category.LAPTOP;
import static com.example.betteriter.global.constant.Category.PC;
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

    @Test
    @DisplayName("카테고리에 해당하는 모든 리뷰를 조회한다.")
    void getReviewByCategory() {
        // given

        Users user01 = Users.builder()
                .oauthId("oauthId")
                .email("danaver12@daum.net")
                .password("1234")
                .roleType(RoleType.ROLE_USER)
                .isExpert(true)
                .usersDetail(UsersDetail.builder()
                        .nickName("nickname")
                        .job(Job.DEVELOPER).build())
                .build();


        Review review01 = Review.builder()
                .id(1L)
                .category(PC)
                .productName("productName01")
                .amount(100000)
                .writer(user01)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        Review review02 = Review.builder()
                .id(2L)
                .category(PC)
                .productName("productName02")
                .writer(user01)
                .amount(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        Review review03 = Review.builder()
                .id(3L)
                .category(PC)
                .productName("productName01")
                .writer(user01)
                .amount(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();

        Review review04 = Review.builder()
                .id(4L)
                .category(PC)
                .productName("productName01")
                .writer(user01)
                .amount(100000)
                .storeName(1)
                .boughtAt(LocalDate.now())
                .starPoint(0)
                .shortReview("shortReview")
                .goodPoint("goodPoint")
                .badPoint("badPoint")
                .build();


        given(reviewRepository.findReviewByCategory(any(Category.class), any(Pageable.class)))
                .willReturn(new SliceImpl<>(List.of(review01, review02, review03, review04)));

        // when
        GetCategoryReviewResponseDto result = this.reviewService.getReviewByCategory(PC);
        // then
        for (GetReviewResponseDto getReviewResponseDto : result.getGetReviewResponseDtoList()) {
            System.out.println("getReviewResponseDto = " + getReviewResponseDto);
        }
    }
}
