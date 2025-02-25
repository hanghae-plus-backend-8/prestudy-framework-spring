package hanghaeboard.api.controller.member;

import hanghaeboard.domain.member.Member;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateMemberRequest {

    @Min(value = 4 , message = "아이디는 4글자 이상이어야 합니다.")
    @Max(value = 10 , message = "아이디는 10글자 이하여야 합니다.")
    @Pattern(regexp = "[a-z0-9]+", message = "아이디는 소문자 영문과 숫자로만 이루어져야 합니다.")
    private String username;

    @Min(value = 8, message = "비밀번호는 4글자 이상이어야 합니다.")
    @Max(value = 15, message = "비밀번호는 15글자 이하여야 합니다.")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "비밀번호는 소문자 혹은 대문자 영문과 숫자로만 이루어져야 합니다.")
    private String password;

    @Builder
    private CreateMemberRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Member toEntity(){
        return Member.builder().username(this.username).password(this.password).build();
    }
}
