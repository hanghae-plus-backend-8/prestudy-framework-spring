package com.hhplus.precourse.comment.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hhplus.precourse.comment.domain.CommentFixture;
import com.hhplus.precourse.comment.repository.CommentRepository;
import com.hhplus.precourse.common.exception.BadRequestException;
import com.hhplus.precourse.common.exception.NotFoundException;
import static com.hhplus.precourse.common.support.ApplicationStatus.COMMENT_NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class UpdateCommentServiceTest {
    @InjectMocks
    private UpdateCommentService service;
    @Mock
    private CommentRepository commentRepository;

    @Test
    void success() {
        // given
        var comment = new CommentFixture().build();
        var command = new UpdateCommentService.Command(
            comment.id(),
            comment.userId(),
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
        var comment = new CommentFixture().build();
        var command = new UpdateCommentService.Command(
            comment.id() + 1,
            comment.userId(),
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

    @Test
    void unauthorized() {
        // given
        var comment = new CommentFixture().build();
        var command = new UpdateCommentService.Command(
            comment.id(),
            comment.userId() + 1,
            "수정된 댓글 내용"
        );
        given(commentRepository.findById(command.id()))
            .willReturn(Optional.of(comment));

        // when
        var throwable = catchThrowable(() -> service.update(command));

        // then
        assertThat(throwable).isInstanceOf(BadRequestException.class)
            .hasMessage("작성자만 수정할 수 있습니다.");
    }
} 