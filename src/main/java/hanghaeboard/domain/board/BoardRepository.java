package hanghaeboard.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {

    @Query("SELECT b FROM Board b JOIN FETCH b.member ORDER BY b.createdDatetime DESC")
    List<Board> findAllWithMember();
}
