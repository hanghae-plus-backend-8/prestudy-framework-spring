package com.hhplus.precourse.post.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.hhplus.precourse.common.ControllerTestContext;
import com.hhplus.precourse.post.service.DeletePostService;
import com.hhplus.precourse.post.service.GetPostService;
import com.hhplus.precourse.post.vo.PostVo;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.hhplus.precourse.common.ApiDocumentUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

class GetPostControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.POST.tagName();
    private static final String DESCRIPTION = Tags.POST.descriptionWith("조회");

    @MockBean
    private GetPostService service;

    @Test
    void success() {
        BDDMockito.given(service.get(anyLong()))
            .willReturn(new PostVo(
                1L,
                "작성자명",
                "제목",
                "내용",
                "비밀번호",
                LocalDateTime.now(),
                LocalDateTime.now()
            ));

        given()
            .when()
            .get("/posts/{id}", "1")
            .then()
            .log().all()
            .apply(
                document(
                    identifier(),
                    new ResourceSnippetParametersBuilder()
                        .tag(TAG)
                        .description(DESCRIPTION),
                    preprocessRequest(),
                    preprocessResponse(),
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                            fieldWithPath("data.id").type(NUMBER).description("게시글 ID"),
                            fieldWithPath("data.title").type(STRING).description("제목"),
                            fieldWithPath("data.author").type(STRING).description("작성자명"),
                            fieldWithPath("data.content").type(STRING).description("내용"),
                            fieldWithPath("data.createdAt").type(STRING).description("생성일시"),
                            fieldWithPath("data.updatedAt").type(STRING).description("수정일시")
                        )
                    )
                )
            )
            .status(HttpStatus.OK);
    }
}