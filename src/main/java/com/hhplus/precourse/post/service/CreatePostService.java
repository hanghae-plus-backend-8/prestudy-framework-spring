package com.hhplus.precourse.post.service;

import com.hhplus.precourse.post.domain.Post;
import com.hhplus.precourse.post.repository.PostRepository;
import com.hhplus.precourse.post.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreatePostService {
    private final PostRepository postRepository;

    @Transactional
    public PostVo create(Command command) {
        var post = createPost(command);
        var saved = postRepository.save(post);

        return PostVo.from(saved);
    }

    private Post createPost(Command command) {
        return new Post(
            command.author(),
            command.title(),
            command.content(),
            command.password()
        );
    }

    public record Command(
        String author,
        String title,
        String content,
        String password
    ) {
    }
}
