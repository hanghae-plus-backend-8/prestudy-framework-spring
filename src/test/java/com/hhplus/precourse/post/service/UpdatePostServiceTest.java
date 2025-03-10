package com.hhplus.precourse.post.service;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.hhplus.precourse.common.exception.BadRequestException;
import com.hhplus.precourse.common.exception.NotFoundException;
import static com.hhplus.precourse.common.support.ApplicationStatus.POST_NOT_FOUND;
import com.hhplus.precourse.post.domain.PostFixture;
import com.hhplus.precourse.post.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class UpdatePostServiceTest {
    @InjectMocks
    private UpdatePostService service;
    @Mock
    private PostRepository postRepository;

    @Test
    void success() {
        // given
        var post = new PostFixture().build();
        var command = new UpdatePostService.Command(
            post.id(),
            1L,
            "수정된 작성자명",
            "수정된 제목",
            "수정된 내용"
        );
        given(postRepository.findById(command.id()))
            .willReturn(Optional.of(post));

        // when
        var result = service.update(command);

        // then
        assertThat(result.id()).isEqualTo(post.id());
        assertThat(result.author()).isEqualTo(command.author());
        assertThat(result.title()).isEqualTo(command.title());
        assertThat(result.content()).isEqualTo(command.content());
    }

    @Test
    void notFound() {
        // given
        var post = new PostFixture().build();
        var command = new UpdatePostService.Command(
            post.id() + 1,
            post.userId(),
            "수정된 작성자명",
            "수정된 제목",
            "수정된 내용"
        );
        given(postRepository.findById(command.id()))
            .willReturn(java.util.Optional.empty());

        // when
        var throwable = catchThrowable(() -> service.update(command));

        // then
        assertThat(throwable).isInstanceOf(NotFoundException.class)
            .hasMessage(POST_NOT_FOUND.message());
    }

    @Test
    void unauthorized() {
        // given
        var post = new PostFixture().build();
        var command = new UpdatePostService.Command(
            post.id(),
            post.userId() + 1,
            "수정된 작성자명",
            "수정된 제목",
            "수정된 내용"
        );
        given(postRepository.findById(command.id()))
            .willReturn(Optional.of(post));

        // when
        var throwable = catchThrowable(() -> service.update(command));

        // then
        assertThat(throwable).isInstanceOf(BadRequestException.class)
            .hasMessage("작성자만 수정할 수 있습니다.");
    }
    
}