package prestudy.framework.spring.api.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prestudy.framework.spring.api.controller.common.response.ApiResponse;
import prestudy.framework.spring.api.controller.user.request.UserCreateRequest;
import prestudy.framework.spring.api.service.user.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/v1/users")
    public ApiResponse<Void> createUser(@Valid @RequestBody UserCreateRequest request) {
        userService.createUser(request.toCommand());
        return ApiResponse.success();
    }
}
