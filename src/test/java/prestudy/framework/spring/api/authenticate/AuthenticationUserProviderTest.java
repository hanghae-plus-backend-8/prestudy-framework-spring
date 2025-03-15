package prestudy.framework.spring.api.authenticate;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import prestudy.framework.spring.api.exception.AuthenticationException;
import prestudy.framework.spring.domain.user.User;
import prestudy.framework.spring.domain.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class AuthenticationUserProviderTest {

    @Autowired
    private AuthenticationUserProvider authenticationUserProvider;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("인증 사용자는 요청이 유효해야한다.")
    @Test
    void authenticatedUserWithInvalidRequest() {
        try (MockedStatic<RequestContextHolder> mockRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            // given
            mockRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(null);

            // when & then
            assertThatThrownBy(() -> authenticationUserProvider.authenticatedUser())
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("요청이 올바르지 않습니다.");
        }
    }

    @DisplayName("인증 사용자는 요청 속성에 사용자 ID가 존재해야한다.")
    @Test
    void authenticatedUserWithoutAttribute() {
        try (MockedStatic<RequestContextHolder> mockRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            // given
            ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
            HttpServletRequest request = mock(HttpServletRequest.class);

            mockRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(attributes);

            given(attributes.getRequest()).willReturn(request);
            given(request.getAttribute("userId")).willReturn(null);

            // when & then
            assertThatThrownBy(() -> authenticationUserProvider.authenticatedUser())
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("사용자 ID가 존재하지 않습니다.");
        }
    }

    @DisplayName("인증 사용자는 유효한 사용자이여야 한다.")
    @Test
    void authenticatedUserWithInvalidUser() {
        try (MockedStatic<RequestContextHolder> mockRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            // given
            ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
            HttpServletRequest request = mock(HttpServletRequest.class);

            mockRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(attributes);

            User user = User.ofUser("abcd1", "Password12!");
            userRepository.save(user);

            given(attributes.getRequest()).willReturn(request);
            given(request.getAttribute("userId")).willReturn(user.getId() + 1);

            // when & then
            assertThatThrownBy(() -> authenticationUserProvider.authenticatedUser())
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("사용자가 올바르지 않습니다.");
        }
    }

    @DisplayName("인증 사용자를 가져온다.")
    @Test
    void authenticatedUser() {
        try (MockedStatic<RequestContextHolder> mockRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            // given
            ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
            HttpServletRequest request = mock(HttpServletRequest.class);

            mockRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(attributes);

            User user = User.ofUser("abcd1", "Password12!");
            userRepository.save(user);

            given(attributes.getRequest()).willReturn(request);
            given(request.getAttribute("userId")).willReturn(user.getId());

            // when
            User authenticatedUser = authenticationUserProvider.authenticatedUser();

            // then
            assertThat(authenticatedUser).isEqualTo(user);
        }
    }

}