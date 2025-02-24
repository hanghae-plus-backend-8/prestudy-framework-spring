package hanghaeboard.api.service.board;

import hanghaeboard.api.controller.board.request.CreateBoardRequest;
import hanghaeboard.api.controller.board.request.DeleteBoardRequest;
import hanghaeboard.api.controller.board.request.UpdateBoardRequest;
import hanghaeboard.api.exception.exception.InvalidPasswordException;
import hanghaeboard.api.service.board.response.CreateBoardResponse;
import hanghaeboard.api.service.board.response.DeleteBoardResponse;
import hanghaeboard.api.service.board.response.FindBoardResponse;
import hanghaeboard.api.service.board.response.UpdateBoardResponse;
import hanghaeboard.domain.board.Board;
import hanghaeboard.domain.board.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final Clock clock;

    @Transactional
    public CreateBoardResponse createBoard(CreateBoardRequest request) {

        Board board = request.toEntity();
        Board savedBoard = boardRepository.save(board);

        return CreateBoardResponse.from(savedBoard);
    }

    public List<FindBoardResponse> findAllBoard(){
        return boardRepository.findAllBoard();
    }

    public FindBoardResponse findBoardById(Long id){
        return FindBoardResponse.from(boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("조회된 게시물이 없습니다.")));
    }

    @Transactional
    public UpdateBoardResponse updateBoard(Long id, UpdateBoardRequest request) {
        Board savedBoard = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("조회된 게시물이 없습니다."));

        if(!savedBoard.isCorrectPassword(request.getPassword())){
            throw new InvalidPasswordException("비밀번호가 올바르지 않습니다.");
        }

        savedBoard.changeBoard(request.getWriter(), request.getTitle(), request.getContent());

        return UpdateBoardResponse.from(savedBoard);
    }

    @Transactional
    public DeleteBoardResponse deleteBoard(Long id, DeleteBoardRequest request){
        Board savedBoard = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("조회된 게시물이 없습니다."));
        LocalDateTime deletedDatetime = LocalDateTime.now();

        if(!savedBoard.isCorrectPassword(request.getPassword())){
            throw new InvalidPasswordException("비밀번호가 올바르지 않습니다.");
        }

        if(!savedBoard.isDeleted()){
            savedBoard.delete(deletedDatetime);
        }

        return DeleteBoardResponse.from(savedBoard);
    }

}
