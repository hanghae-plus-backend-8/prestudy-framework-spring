package prestudy.framework.spring.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import prestudy.framework.spring.api.controller.board.BoardController;
import prestudy.framework.spring.api.controller.comment.CommentController;
import prestudy.framework.spring.api.controller.user.UserController;
import prestudy.framework.spring.api.authenticate.jwt.JwtInterceptor;
import prestudy.framework.spring.api.service.board.BoardService;
import prestudy.framework.spring.api.service.comment.CommentService;
import prestudy.framework.spring.api.service.user.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@WebMvcTest(controllers = {
    BoardController.class,
    UserController.class,
    CommentController.class
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected BoardService boardService;

    @MockitoBean
    protected UserService userService;

    @MockitoBean
    protected CommentService commentService;

    @MockitoBean
    protected JwtInterceptor jwtInterceptor;

    @BeforeEach
    void setUp() throws Exception {
        given(jwtInterceptor.preHandle(any(), any(), any())).willReturn(true);
    }
}
