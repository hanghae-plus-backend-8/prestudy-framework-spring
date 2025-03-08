package prestudy.framework.spring.api.jwt;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import prestudy.framework.spring.api.controller.common.response.ApiResponse;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class JwtResponseUtils {

    public static ResponseEntity<ApiResponse<Void>> response(String jwt) {
        return new ResponseEntity<>(
            ApiResponse.success(),
            setBearerAuthHeaders(jwt),
            HttpStatus.OK);
    }

    private static HttpHeaders setBearerAuthHeaders(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        return headers;
    }
}
