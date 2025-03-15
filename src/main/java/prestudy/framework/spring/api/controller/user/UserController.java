package prestudy.framework.spring.api.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import prestudy.framework.spring.api.controller.common.response.ApiResponse;
import prestudy.framework.spring.api.controller.user.request.UserCreateRequest;
import prestudy.framework.spring.api.controller.user.request.UserLoginRequest;
import prestudy.framework.spring.api.controller.user.response.UserLoginResponse;
import prestudy.framework.spring.api.service.user.UserService;

@Tag(name = "회원 API")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입")
    @PostMapping("/api/v1/users")
    public ApiResponse<Void> createUser(@Valid @RequestBody UserCreateRequest request) {
        userService.createUser(request.toCommand());
        return ApiResponse.success();
    }

    @Operation(summary = "로그인")
    @PostMapping("/api/v1/users/login")
    public ResponseEntity<ApiResponse<Void>> loginUser(@Valid @RequestBody UserLoginRequest request) {
        String jwt = userService.loginUser(request.toCommand());
        return UserLoginResponse.response(jwt);
    }
}
