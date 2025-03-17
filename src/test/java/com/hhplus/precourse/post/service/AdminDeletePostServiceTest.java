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
class AdminDeletePostServiceTest {
    @InjectMocks
    private AdminDeletePostService service;
    @Mock
    private PostRepository postRepository;

    @Test
    void success() {
        // given
        var post = new PostFixture().build();
        given(postRepository.findById(post.id()))
            .willReturn(Optional.of(post));

        // when
        var throwable = catchThrowable(() -> service.delete(post.id()));

        // then
        assertThat(throwable).isNull();
    }

    @Test
    void notFound() {
        // given
        given(postRepository.findById(1L))
            .willReturn(Optional.empty());

        // when
        var throwable = catchThrowable(() -> service.delete(1L));

        // then
        assertThat(throwable).isInstanceOf(NotFoundException.class)
            .hasMessage(POST_NOT_FOUND.message());
    }
} 