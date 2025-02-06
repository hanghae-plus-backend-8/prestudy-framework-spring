package hanghaeboard.api.controller.board;

import hanghaeboard.api.ApiResponse;
import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.service.board.BoardService;
import hanghaeboard.api.service.board.request.CreateBoardServiceRequest;
import hanghaeboard.api.service.board.response.CreateBoardResponse;
import hanghaeboard.api.service.member.MemberService;
import hanghaeboard.domain.member.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    @PostMapping("/api/v1/board/create")
    public ApiResponse<CreateBoardResponse> createBoard(@Valid @RequestBody CreateBoardRequest request) throws Exception{
        log.info("createBoard request: {}", request);

        Member member = memberService.findMemberById(request.getMemberId());
        CreateBoardServiceRequest serviceRequest = request.to(member);

        return ApiResponse.ok(boardService.createBoard(serviceRequest));
    }

}
