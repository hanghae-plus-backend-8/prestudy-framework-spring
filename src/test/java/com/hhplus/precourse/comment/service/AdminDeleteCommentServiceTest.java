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
class AdminDeleteCommentServiceTest {
    @InjectMocks
    private AdminDeleteCommentService service;
    @Mock
    private CommentRepository commentRepository;

    @Test
    void success() {
        // given
        var comment = new CommentFixture().build();
        given(commentRepository.findById(comment.id()))
            .willReturn(Optional.of(comment));

        // when
        var throwable = catchThrowable(() -> service.delete(comment.id()));

        // then
        assertThat(throwable).isNull();
    }

    @Test
    void notFound() {
        // given
        given(commentRepository.findById(1L))
            .willReturn(Optional.empty());

        // when
        var throwable = catchThrowable(() -> service.delete(1L));

        // then
        assertThat(throwable).isInstanceOf(NotFoundException.class)
            .hasMessage(COMMENT_NOT_FOUND.message());
    }
} 