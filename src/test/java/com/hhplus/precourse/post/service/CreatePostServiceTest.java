package com.hhplus.precourse.post.service;

import com.hhplus.precourse.post.domain.Post;
import com.hhplus.precourse.post.domain.PostFixture;
import com.hhplus.precourse.post.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreatePostServiceTest {
    @InjectMocks
    private CreatePostService service;
    @Mock
    private PostRepository postRepository;

    @Test
    void success() {
        // given
        var command = new CreatePostService.Command(
            "작성자명",
            "제목",
            "내용",
            "비밀번호"
        );
        given(postRepository.save(any()))
            .willReturn(new PostFixture().build());

        // when
        var throwable = catchThrowable(() -> service.create(command));

        // then
        assertThat(throwable).isNull();
        var postCaptor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(postCaptor.capture());
        var captured = postCaptor.getValue();
        assertThat(captured.author()).isEqualTo(command.author());
        assertThat(captured.title()).isEqualTo(command.title());
        assertThat(captured.content()).isEqualTo(command.content());
        assertThat(captured.password()).isEqualTo(command.password());
    }
}