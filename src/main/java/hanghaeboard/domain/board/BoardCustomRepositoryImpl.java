package hanghaeboard.domain.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghaeboard.api.service.board.response.FindBoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FindBoardResponse> findAllBoard() {
        return null;
//        QBoard board = QBoard.board;
//        QMember member = QMember.member;
//
//        return queryFactory.select(new FindBoardResponse(board.id, member.id, board.title, board.content, board.createdDatetime, board.lastModifiedDatetime))
//                .from(board)
//                .join(member)
//                .orderBy(board.createdDatetime.desc()).fetch();
    }
}
