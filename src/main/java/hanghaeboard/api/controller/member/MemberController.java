package hanghaeboard.api.controller.member;

import hanghaeboard.api.ApiResponse;
import hanghaeboard.api.controller.member.request.CreateMemberRequest;
import hanghaeboard.api.service.member.MemberService;
import hanghaeboard.api.service.member.response.FindMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/api/v1/member/join")
    public ApiResponse<FindMember> join(@Valid @RequestBody CreateMemberRequest request) {
        return ApiResponse.ok(memberService.join(request));
    }

}
