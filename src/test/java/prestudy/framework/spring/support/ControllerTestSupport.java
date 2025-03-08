package prestudy.framework.spring.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import prestudy.framework.spring.api.controller.board.BoardController;
import prestudy.framework.spring.api.controller.user.UserController;
import prestudy.framework.spring.api.service.board.BoardService;
import prestudy.framework.spring.api.service.user.UserService;

@WebMvcTest(controllers = {
    BoardController.class,
    UserController.class
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
}
