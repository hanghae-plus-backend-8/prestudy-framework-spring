package hanghaeboard.api.controller.user;

import hanghaeboard.api.ApiResponse;
import hanghaeboard.api.controller.user.request.CreateUserRequest;
import hanghaeboard.api.controller.user.request.LoginRequest;
import hanghaeboard.api.service.user.UserService;
import hanghaeboard.api.service.user.response.FindUser;
import hanghaeboard.api.service.user.response.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/api/v1/users")
    public ApiResponse<FindUser> join(@Valid @RequestBody CreateUserRequest request) {
        return ApiResponse.ok(userService.join(request));
    }

    @PostMapping("/api/v1/users/login")
    public ApiResponse<Void> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = userService.login(request);

        response.setHeader(HttpHeaders.AUTHORIZATION
                , "Bearer " + loginResponse.getJwtToken());

        return ApiResponse.ok();
    }

}
