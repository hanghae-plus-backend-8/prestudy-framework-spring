package prestudy.framework.spring.api.authenticate;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

@Component
public class AuthenticationHandler {

    public boolean isIgnoreAuthentication(Object handler) {
        if (isNotHandlerMethod(handler)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        return isNotPresentAuthentication(method);
    }

    private boolean isNotPresentAuthentication(Method method) {
        return !method.isAnnotationPresent(Authentication.class);
    }

    private boolean isNotHandlerMethod(Object handler) {
        return !(handler instanceof HandlerMethod);
    }
}
