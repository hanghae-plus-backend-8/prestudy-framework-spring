package com.hhplus.precourse.comment.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.hhplus.precourse.comment.domain.CommentValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hhplus.precourse.comment.domain.Comment;
import com.hhplus.precourse.comment.domain.CommentFixture;
import com.hhplus.precourse.comment.repository.CommentRepository;

@ExtendWith(MockitoExtension.class)
class CreateCommentServiceTest {
    @InjectMocks
    private CreateCommentService service;
    @Mock
    private CommentValidator commentValidator;
    @Mock
    private CommentRepository commentRepository;

    @Test
    void success() {
        // given
        var command = new CreateCommentService.Command(1L, 1L, "test-content");
        var comment = new CommentFixture()
            .setPostId(command.postId())
            .setUserId(command.userId())
            .setContent(command.content())
            .build();
        given(commentRepository.save(any()))
            .willReturn(comment);

        // when
        var throwable = catchThrowable(() -> service.create(command));

        // then
        assertThat(throwable).isNull();
        var captor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(captor.capture());
        var captured = captor.getValue();
        assertThat(captured.id()).isNull();
        assertThat(captured.postId()).isEqualTo(command.postId());
        assertThat(captured.userId()).isEqualTo(command.userId());
        assertThat(captured.content()).isEqualTo(command.content());
    }

}