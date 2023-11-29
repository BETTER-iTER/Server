package com.example.betteriter.global;

import com.example.betteriter.global.constant.Category;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeserializerTest {
    @Test
    @DisplayName("역직렬화 테스트")
    void test() throws JsonProcessingException {
        // given
        Category category = Category.SPEAKER;
        String json = "{\"category\":\"MOUSE\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Object o = objectMapper.readerFor(Category.class).readValue(json);
        System.out.println(o);
        // when

        // then
    }
}
