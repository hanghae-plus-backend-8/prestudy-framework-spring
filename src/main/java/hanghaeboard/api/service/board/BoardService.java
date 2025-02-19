package hanghaeboard.api.service.board;

import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.service.board.response.CreateBoardResponse;
import hanghaeboard.api.service.board.response.FindBoardResponse;
import hanghaeboard.domain.board.Board;
import hanghaeboard.domain.board.BoardRepository;
import hanghaeboard.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CreateBoardResponse createBoard(CreateBoardRequest request) {

        Board board = request.toEntity();
        Board savedBoard = boardRepository.save(board);

        return CreateBoardResponse.from(savedBoard);
    }

    public List<FindBoardResponse> findAllBoard(){
        return boardRepository.findAllBoard();
    }

}
