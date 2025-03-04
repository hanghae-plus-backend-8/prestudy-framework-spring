package prestudy.framework.spring.api.authenticate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import prestudy.framework.spring.support.IntegrationTestSupport;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class AuthenticationHandlerTest extends IntegrationTestSupport {

    @Autowired
    private AuthenticationHandler authenticationHandler;

    @DisplayName("HandlerMethod가 아니면 무시한다.")
    @Test
    void isIgnoreAuthenticationWithNotHandlerMethod() {
        // given
        Object handler = new Object();

        // when
        boolean ignoreAuthentication = authenticationHandler.isIgnoreAuthentication(handler);

        // then
        assertThat(ignoreAuthentication).isTrue();
    }

    @DisplayName("@Authentication 어노테이션이 없으면 무시한다.")
    @Test
    void isIgnoreAuthenticationWithoutAuthenticationAnnotation() {
        // given
        HandlerMethod handlerMethod = mock(HandlerMethod.class);

        given(handlerMethod.getMethod()).willReturn(mock(Method.class));
        given(handlerMethod.getMethod().isAnnotationPresent(Authentication.class))
            .willReturn(false);

        // when
        boolean ignoreAuthentication = authenticationHandler.isIgnoreAuthentication(handlerMethod);

        // then
        assertThat(ignoreAuthentication).isTrue();
    }
}