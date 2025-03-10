package com.hhplus.precourse.post.service;

import com.hhplus.precourse.common.exception.NotFoundException;
import com.hhplus.precourse.post.domain.PostFixture;
import com.hhplus.precourse.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.hhplus.precourse.common.support.ApplicationStatus.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AdminUpdatePostServiceTest {
    @InjectMocks
    private AdminUpdatePostService service;
    @Mock
    private PostRepository postRepository;

    @Test
    void success() {
        // given
        var post = new PostFixture().build();
        var command = new AdminUpdatePostService.Command(
            post.id(),
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
        var command = new AdminUpdatePostService.Command(
            1L,
            "수정된 작성자명",
            "수정된 제목",
            "수정된 내용"
        );
        given(postRepository.findById(command.id()))
            .willReturn(Optional.empty());

        // when
        var throwable = catchThrowable(() -> service.update(command));

        // then
        assertThat(throwable).isInstanceOf(NotFoundException.class)
            .hasMessage(POST_NOT_FOUND.message());
    }
} 