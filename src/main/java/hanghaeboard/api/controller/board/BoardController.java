package hanghaeboard.api.controller.board;

import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.service.board.BoardService;
import hanghaeboard.api.service.board.request.CreateBoardServiceRequest;
import hanghaeboard.api.service.board.response.CreateBoardResponse;
import hanghaeboard.api.service.member.MemberService;
import hanghaeboard.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    @PostMapping("/api/v1/board/create")
    public CreateBoardResponse createBoard(CreateBoardRequest request){

        Member member = memberService.findMemberById(request.getMemberId());
        CreateBoardServiceRequest serviceRequest = request.to(member);

        return boardService.createBoard(serviceRequest);
    }

}
