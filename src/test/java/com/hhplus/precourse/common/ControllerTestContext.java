package com.hhplus.precourse.common;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(RestDocumentationExtension.class)
public class ControllerTestContext {
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;



    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(print())
            .build();
    }

    protected MockMvcRequestSpecification given() {
        return RestAssuredMockMvc.given().mockMvc(mockMvc)
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON.withCharset(StandardCharsets.UTF_8));
    }

    protected String identifier() {
        return getClass().getSimpleName();
    }

    protected String identifier(String affix) {
        return "%s-%s".formatted(identifier(), affix);
    }

    protected HeaderDescriptor authorizationHeader() {
        return headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer 토큰");
    }

    protected static File imageFile() throws IOException {
        var multipartFile = new MockMultipartFile(
            "image", // 파일의 파라미터 이름
            "test-image.jpg", // 파일의 원본 이름
            "image/jpeg", // 파일의 MIME 타입
            "mock file content".getBytes() // 파일의 내용을 담은 바이트 배열
        );

        var file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }

    protected <T extends Enum<?>> ParameterDescriptor enumDescription(ParameterDescriptor descriptor,
                                                                      Class<T> enumClass) {
        return descriptor.description(
            "%s : %s".formatted(
                descriptor.getDescription(),
                Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "))
            )
        );
    }

    protected <T extends Enum<?>> FieldDescriptor enumDescription(FieldDescriptor descriptor,
                                                                  Class<T> enumClass) {
        return descriptor.description(
            "%s : %s".formatted(
                descriptor.getDescription(),
                Arrays.stream(enumClass.getEnumConstants())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "))
            )
        );
    }

    protected enum Tags {
        POST("게시글"),
        USER("사용자")
        ;

        private final String tagName;

        Tags(String tagName) {
            this.tagName = tagName;
        }

        public String tagName() {
            return tagName;
        }

        public String descriptionWith(String affix) {
            return "%s : %s".formatted(tagName, affix);
        }
    }
}
