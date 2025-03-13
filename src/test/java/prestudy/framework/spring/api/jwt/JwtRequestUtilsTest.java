package prestudy.framework.spring.api.jwt;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@ActiveProfiles("test")
@SpringBootTest
class JwtRequestUtilsTest {

    @Autowired
    private JwtRequestUtils jwtRequestUtils;

    @DisplayName("Request가 올바르지 않으면 유효하지 않은 토큰 값이다.")
    @Test
    void getUserIdWithInvalidRequest() {
        try (MockedStatic<RequestContextHolder> mockRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(null);

            // when & then
            assertThatThrownBy(() -> jwtRequestUtils.getUserId())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("토큰이 유효하지 않습니다.");
        }
    }

    @DisplayName("Request attribute가 올바르지 않으면 유효하지 않은 토큰 값이다.")
    @Test
    void getUserIdWithoutAttribute() {
        try (MockedStatic<RequestContextHolder> mockRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
            HttpServletRequest request = mock(HttpServletRequest.class);

            mockRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(attributes);

            given(attributes.getRequest()).willReturn(request);
            given(request.getAttribute("userId")).willReturn(null);

            // when & then
            assertThatThrownBy(() -> jwtRequestUtils.getUserId())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("토큰이 유효하지 않습니다.");
        }
    }

    @DisplayName("사용자 ID를 가져온다.")
    @Test
    void getUserId() {
        try (MockedStatic<RequestContextHolder> mockRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
            HttpServletRequest request = mock(HttpServletRequest.class);

            mockRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(attributes);

            given(attributes.getRequest()).willReturn(request);
            given(request.getAttribute("userId")).willReturn(1L);

            // when
            Long userId = jwtRequestUtils.getUserId();

            //then
            assertThat(userId).isEqualTo(1L);
        }
    }

}