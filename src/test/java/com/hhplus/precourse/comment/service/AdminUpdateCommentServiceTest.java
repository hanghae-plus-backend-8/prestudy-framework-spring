package com.hhplus.precourse.comment.service;

import com.hhplus.precourse.comment.domain.CommentFixture;
import com.hhplus.precourse.comment.repository.CommentRepository;
import com.hhplus.precourse.common.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.hhplus.precourse.common.support.ApplicationStatus.COMMENT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminUpdateCommentServiceTest {
    @InjectMocks
    private AdminUpdateCommentService service;
    @Mock
    private CommentRepository commentRepository;

    @Test
    void success() {
        // given
        var comment = new CommentFixture().build();
        var command = new AdminUpdateCommentService.Command(
            comment.id(),
            "수정된 댓글 내용"
        );
        given(commentRepository.findById(command.id()))
            .willReturn(Optional.of(comment));

        // when
        var result = service.update(command);

        // then
        assertThat(result.id()).isEqualTo(comment.id());
        assertThat(result.content()).isEqualTo(command.content());
    }

    @Test
    void notFound() {
        // given
        var command = new AdminUpdateCommentService.Command(
            1L,
            "수정된 댓글 내용"
        );
        given(commentRepository.findById(command.id()))
            .willReturn(Optional.empty());

        // when
        var throwable = catchThrowable(() -> service.update(command));

        // then
        assertThat(throwable).isInstanceOf(NotFoundException.class)
            .hasMessage(COMMENT_NOT_FOUND.message());
    }
} 