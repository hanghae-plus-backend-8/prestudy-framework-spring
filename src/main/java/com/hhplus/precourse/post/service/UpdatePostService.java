package com.hhplus.precourse.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhplus.precourse.common.exception.BadRequestException;
import com.hhplus.precourse.common.exception.NotFoundException;
import static com.hhplus.precourse.common.support.ApplicationStatus.POST_NOT_FOUND;
import static com.hhplus.precourse.common.support.ApplicationStatus.UNAUTHORIZED;
import com.hhplus.precourse.post.repository.PostRepository;
import com.hhplus.precourse.post.vo.PostVo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdatePostService {
    private final PostRepository postRepository;

    @Transactional
    public PostVo update(Command command) {
        var post = postRepository.findById(command.id())
            .orElseThrow(() ->  new NotFoundException(POST_NOT_FOUND));

        if (!post.isAuthor(command.userId())) {
            throw new BadRequestException(UNAUTHORIZED, "작성자만 수정할 수 있습니다.");
        }

        post.update(
            command.author(),
            command.title(),
            command.content()
        );

        return PostVo.from(post);
    }


    public record Command(
        long id,
        long userId,
        String author,
        String title,
        String content
    ) {
    }
}
