package com.hhplus.precourse.post.controller;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.hhplus.precourse.common.ControllerTestContext;
import com.hhplus.precourse.post.service.AdminDeletePostService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.hhplus.precourse.common.ApiDocumentUtils.*;
import static com.hhplus.precourse.common.TestUserDetailsConfig.ADMIN_DETAILS_BEAN_NAME;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

class AdminDeletePostControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.ADMIN_POST.tagName();
    private static final String DESCRIPTION = Tags.ADMIN_POST.descriptionWith("삭제");

    @MockBean
    private AdminDeletePostService service;

    @WithUserDetails(userDetailsServiceBeanName = ADMIN_DETAILS_BEAN_NAME)
    @Test
    void success() {
        given()
            .header(authorizationHeader())
            .when()
            .delete("/admin/posts/{id}", "1")
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
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(NULL).description("응답 데이터")
                        )
                    )
                )
            )
            .status(HttpStatus.OK);
    }
} 