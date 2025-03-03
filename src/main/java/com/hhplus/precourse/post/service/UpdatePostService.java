package com.hhplus.precourse.post.service;

import com.hhplus.precourse.common.exception.NotFoundException;
import com.hhplus.precourse.post.repository.PostRepository;
import com.hhplus.precourse.post.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hhplus.precourse.common.support.ApplicationStatus.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UpdatePostService {
    private final PostRepository postRepository;

    @Transactional
    public PostVo update(Command command) {
        var post = postRepository.findById(command.id())
            .orElseThrow(() ->  new NotFoundException(POST_NOT_FOUND));

        post.update(
            command.author(),
            command.title(),
            command.content(),
            command.password()
        );

        return PostVo.from(post);
    }


    public record Command(
        long id,
        String author,
        String title,
        String content,
        String password
    ) {
    }
}
