package prestudy.framework.spring.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import prestudy.framework.spring.api.authenticate.AuthenticationUserProvider;

@ActiveProfiles("test")
@SpringBootTest
public abstract class IntegrationTestSupport {

    @MockitoBean
    protected AuthenticationUserProvider authenticationUserProvider;
}
