package com.example.betteriter.global;

import com.example.betteriter.fo_domain.review.dto.CreateReviewRequestDto;
import com.example.betteriter.global.constant.Category;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.example.betteriter.global.constant.Category.LAPTOP;

public class DeserializerTest {
    @Test
    @DisplayName("역직렬화 테스트")
    void test() throws JsonProcessingException {
//        // given
//        String json = "{\"category\":\"노트북\"}";
//        // when
//
//        com.example.betteriter.fo_domain.review.dto.Test category = new ObjectMapper().readValue(json, com.example.betteriter.fo_domain.review.dto.Test.class);
//        // then
//        System.out.println("category = " + category);
//
//        Category[] values = Category.values();
//        for (Category value : values) {
//            System.out.println("value.getName() = " + value.getName());
//        }
    }

    @Test
    @DisplayName("직렬화 테스트 01")
    void serializerTest01() throws JsonProcessingException {
        // given
        List<Long> list = List.of(1L, 2L);
        // when
        String json = new ObjectMapper().writeValueAsString(list);
        // then
        System.out.println(json);

        System.out.println(new ObjectMapper().writeValueAsString(Category.ETC));
        System.out.println(new ObjectMapper().readValue(
                new ObjectMapper().writeValueAsString(Category.ETC), Category.class));
    }

    @Test
    @DisplayName("CreateReviewRequestDto Deserializer Test")
    void CreateReviewRequestDto_역직렬화_테스트() throws JsonProcessingException {
        // given
        CreateReviewRequestDto requestDto =
                CreateReviewRequestDto.builder()
                        .category(LAPTOP)
                        .productName("맥북1")
                        .boughtAt(LocalDate.now())
                        .manufacturerId(1L)
                        .amount(10000)
                        .storeName(1)
                        .shortReview("한줄 평")
                        .starPoint(1)
                        .goodPoint("goodPoint")
                        .badPoint("badPoint")
                        .specData(List.of(1L, 2L))
                        .images(List.of(CreateReviewRequestDto.CreateReviewImageRequestDto.builder().imgUrl("imgUrl1").build(),
                                CreateReviewRequestDto.CreateReviewImageRequestDto.builder().imgUrl("imgUrl2").build())
                        ).build();

        String json = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(requestDto);
        System.out.println("json = " + json);
        // when

        CreateReviewRequestDto requestDto1 = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, CreateReviewRequestDto.class);
        System.out.println("requestDto1 = " + requestDto1);

        // then
    }

    @Test
    @DisplayName("CreateReviewRequestDto Deserializer Test02")
    void CreateReviewRequestDto_역직렬화_테스트02() throws JsonProcessingException {
        // given
        String json
                = "{\"category\":\"기타\",\"productName\":\"productName\",\"boughtAt\":\"2023-11-20\"" +
                ",\"manufacturerId\":1,\"amount\":100000,\"storeName\":1,\"shortReview\":\"great\",\"starPoint\":5" +
                ",\"goodPoint\":\"good\",\"badPoint\":\"bad\",\"specData\":[1,2],\"images\":[{\"imgUrl\":\"http\"}]}";

        System.out.println(json);
        CreateReviewRequestDto requestDto = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, CreateReviewRequestDto.class);
        System.out.println(requestDto);
        // when

        // then
    }
}
