package prestudy.framework.spring.api.authenticate.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.method.HandlerMethod;
import prestudy.framework.spring.api.authenticate.AuthenticationHandler;
import prestudy.framework.spring.api.authenticate.jwt.JwtInterceptor;
import prestudy.framework.spring.api.authenticate.jwt.JwtProvider;
import prestudy.framework.spring.support.IntegrationTestSupport;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JwtInterceptorTest extends IntegrationTestSupport {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private JwtProvider jwtProvider;

    @MockitoBean
    private AuthenticationHandler authenticationHandler;

    @DisplayName("@Authentication 어노테이션이 있어야 검증을 할 수 있다.")
    @Test
    void preHandleRequiredAuthenticationAnnotation() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerMethod handlerMethod = mock(HandlerMethod.class);

        given(authenticationHandler.isIgnoreAuthentication(handlerMethod))
            .willReturn(true);

        // when
        boolean result = jwtInterceptor.preHandle(request, response, handlerMethod);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("Authorization 헤더가 없으면 JWT 검증에 실패한다.")
    @Test
    void preHandleWithoutAuthorizationHeader() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerMethod handlerMethod = mock(HandlerMethod.class);

        given(authenticationHandler.isIgnoreAuthentication(handlerMethod))
            .willReturn(false);

        // when & then
        assertThatThrownBy(() -> jwtInterceptor.preHandle(request, response, handlerMethod))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("토큰이 유효하지 않습니다.");
    }

    @DisplayName("JWT 토큰 값이 올바르지 않으면 검증에 실패한다.")
    @ParameterizedTest
    @ValueSource(strings = {"Bearer", "Invalid", "Bearer123", "Bearer 123", "Bearer invalid"})
    void preHandleWithInvalidJwtToken(String authorizationHeader) {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerMethod handlerMethod = mock(HandlerMethod.class);

        given(authenticationHandler.isIgnoreAuthentication(handlerMethod))
            .willReturn(false);

        given(request.getHeader("Authorization")).willReturn(authorizationHeader);

        // when & then
        assertThatThrownBy(() -> jwtInterceptor.preHandle(request, response, handlerMethod))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("토큰이 유효하지 않습니다.");
    }

    @DisplayName("JWT 토큰 값이 만료된 토큰값이면 검증에 실패한다.")
    @Test
    void preHandleWithExpiredJwtToken() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerMethod handlerMethod = mock(HandlerMethod.class);

        given(authenticationHandler.isIgnoreAuthentication(handlerMethod))
            .willReturn(false);

        String token = jwtProvider.generateToken(1L, LocalDateTime.now().minusMinutes(10));

        given(request.getHeader("Authorization")).willReturn("Bearer " + token);

        // when & then
        assertThatThrownBy(() -> jwtInterceptor.preHandle(request, response, handlerMethod))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("토큰이 유효하지 않습니다.");
    }

    @DisplayName("JWT 토큰 값이 정상적이면 Request attribute에 subject 정보를 저장한다.")
    @Test
    void preHandle() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerMethod handlerMethod = mock(HandlerMethod.class);

        given(authenticationHandler.isIgnoreAuthentication(handlerMethod))
            .willReturn(false);

        String token = jwtProvider.generateToken(1L, LocalDateTime.now().plusMinutes(10));

        given(request.getHeader("Authorization")).willReturn("Bearer " + token);

        // when
        boolean result = jwtInterceptor.preHandle(request, response, handlerMethod);

        // then
        assertThat(result).isTrue();
        verify(request).setAttribute("userId", 1L);
    }
}