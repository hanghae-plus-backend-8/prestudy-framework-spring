package hanghaeboard.api.service.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {

    private String jwtToken;

    @Builder
    private LoginResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
