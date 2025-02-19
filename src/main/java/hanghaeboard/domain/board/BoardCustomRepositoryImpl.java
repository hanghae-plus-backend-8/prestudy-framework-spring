package hanghaeboard.domain.board;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hanghaeboard.api.service.board.response.FindBoardResponse;
import hanghaeboard.api.service.member.response.FindMember;
import hanghaeboard.domain.member.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FindBoardResponse> findAllBoard() {
        QBoard board = QBoard.board;
        QMember member = QMember.member;

        return queryFactory.select(
                Projections.constructor(FindBoardResponse.class, board.id
                        , Projections.constructor(FindMember.class, member.id, member.username)
                        , board.title, board.content, board.createdDatetime, board.lastModifiedDatetime))
                .from(board)
                .join(board.member, member)
                .orderBy(board.createdDatetime.desc()).fetch();
    }
}
