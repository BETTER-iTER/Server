package com.example.betteriter.example.controller;

import com.example.betteriter.example.dto.TempResponse;
import com.example.betteriter.example.service.TempQueryServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class TempControllerTest {

    private TempController tempController;

    @Mock
    private TempQueryServiceImpl tempQueryService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
    }

    @Test
    public void api_call_test() throws Exception {
        // given
        final TempResponse.TempTestDto dto = buildTestDto();

        // when
        final ResultActions actions = requestGetTest();

        // then
        actions
                .andExpect(status().isOk());
    }


    @Test
    public void api_exception_test() throws Exception {
        // given
        final Integer flag = 3;
        final TempResponse.TempExceptionDto dto = buildTestExceptionDto(3);

        // when
        final ResultActions actions = requestGetException(flag);

        // then
        actions
                .andExpect(status().is4xxClientError());
    }

    private ResultActions requestGetException(Integer flag) throws Exception {
        return mockMvc.perform(get("/temp/exception")
                        .param("flag", flag.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private TempResponse.TempExceptionDto buildTestExceptionDto(Integer flag) {
        return TempResponse.TempExceptionDto.builder()
                .flag(flag)
                .build();
    }


    private ResultActions requestGetTest() throws Exception {
        return mockMvc.perform(get("/temp/test")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private static TempResponse.TempTestDto buildTestDto() {
        return TempResponse.TempTestDto.builder()
                .testString("test")
                .build();
    }
}