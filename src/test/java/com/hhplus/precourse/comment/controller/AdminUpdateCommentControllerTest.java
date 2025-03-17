package com.hhplus.precourse.comment.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.hhplus.precourse.comment.CommentVoFixture;
import com.hhplus.precourse.comment.service.AdminUpdateCommentService;
import com.hhplus.precourse.common.ControllerTestContext;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.hhplus.precourse.common.ApiDocumentUtils.*;
import static com.hhplus.precourse.common.TestUserDetailsConfig.ADMIN_DETAILS_BEAN_NAME;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

class AdminUpdateCommentControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.ADMIN_COMMENT.tagName();
    private static final String DESCRIPTION = Tags.ADMIN_COMMENT.descriptionWith("수정");

    @MockBean
    private AdminUpdateCommentService service;

    @WithUserDetails(userDetailsServiceBeanName = ADMIN_DETAILS_BEAN_NAME)
    @Test
    void success() {
        BDDMockito.given(service.update(BDDMockito.any()))
            .willReturn(new CommentVoFixture().build());

        var body = new AdminUpdateCommentController.Request(
            "수정할 댓글 내용"
        );

        given()
            .header(authorizationHeader())
            .body(body)
            .when()
            .put("/admin/comments/{id}", "1")
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
                    requestHeaderWithAuthorization(),
                    requestFields(
                        fieldWithPath("content").type(STRING).description("수정할 댓글 내용")
                    ),
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(OBJECT).description("응답 데이터"),
                            fieldWithPath("data.id").type(NUMBER).description("댓글 ID"),
                            fieldWithPath("data.userId").type(NUMBER).description("사용자 ID"),
                            fieldWithPath("data.postId").type(NUMBER).description("게시글 ID"),
                            fieldWithPath("data.content").type(STRING).description("댓글 내용"),
                            fieldWithPath("data.createdAt").type(STRING).description("생성일시"),
                            fieldWithPath("data.updatedAt").type(STRING).description("수정일시")
                        )
                    )
                )
            )
            .status(HttpStatus.OK);
    }
} 