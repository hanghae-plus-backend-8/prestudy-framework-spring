package com.hhplus.precourse.user.controller;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static com.hhplus.precourse.common.ApiDocumentUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.hhplus.precourse.common.ControllerTestContext;
import com.hhplus.precourse.user.service.LoginService;

class LoginControllerTest extends ControllerTestContext {
    private static final String TAG = Tags.USER.tagName();
    private static final String DESCRIPTION = Tags.USER.descriptionWith("로그인");

    @MockBean
    private LoginService service;

    @Test
    void success() {
        var body = new LoginController.Request(
            "test1234",
            "password123"
        );

        BDDMockito.given(service.login(any()))
            .willReturn("jwtToken");

        given()
            .body(body)
            .when()
            .post("/users/login")
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
                    requestFields(
                        fieldWithPath("name").type(STRING).description("사용자 이름"),
                        fieldWithPath("password").type(STRING).description("비밀번호")
                    ),
                    responseFields(
                        fieldsWithBasic(
                            fieldWithPath("data").type(NULL).description("응답 데이터")
                        )
                    ),
                    responseHeaders(
                        headerWithName(HttpHeaders.AUTHORIZATION).description("JWT token")
                    )
                )
            )
            .status(HttpStatus.OK);
    }
}