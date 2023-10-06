package com.example.betteriter.example.controller;

import com.example.betteriter.example.dto.TempResponse;
import com.example.betteriter.example.service.TempQueryServiceImpl;
import com.example.betteriter.global.filter.JwtAuthenticationEntryPoint;
import com.example.betteriter.global.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.example.betteriter.rest_docs.ApiDocumentUtils.getDocumentRequest;
import static com.example.betteriter.rest_docs.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = TempController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                        JwtAuthenticationFilter.class,
                        JwtAuthenticationEntryPoint.class,
                        CorsFilter.class
                })
        })
@WithMockUser(username = "test", password = "0000",roles = "USER")
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "betteritem.store", uriPort = 80)
class TempControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TempQueryServiceImpl tempQueryService;

    @Test
    public void api_call_test() throws Exception {
        // given
        final TempResponse.TempTestDto dto = buildTestDto();

        // when
        final ResultActions actions = requestGetTest();

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("temp-test",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(),
                        requestFields(),
                        responseFields(
                            fieldWithPath("testString").type(JsonFieldType.STRING).description("test string")
                        )
                ));
    }

    @Test
    public void api_exception_test() throws Exception {
        // given
        final Integer flag = 2;
        final TempResponse.TempExceptionDto dto = buildTestExceptionDto(flag);

        // when
        final ResultActions actions = requestGetException(flag);

        // then
        actions
                .andExpect(status().isOk())
                .andDo(document("temp-exception",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                // parameter 문서화
                                parameterWithName("flag").description("flag: 만약 3이면 예외 발생")
                        ),
                        requestFields(
                                // request 문서화
                                // filedWithPath("flag").type(JsonFieldType.NUMBER).description("flag")
                        ),
                        responseFields(
                                // response 문서화
                                fieldWithPath("flag").type(JsonFieldType.NUMBER).description("flag")
                        )
                ));
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
