package com.hhplus.precourse.comment.component;

import com.hhplus.precourse.comment.domain.Comment;
import com.hhplus.precourse.comment.domain.CommentValidator;
import com.hhplus.precourse.common.exception.NotFoundException;
import com.hhplus.precourse.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.hhplus.precourse.common.support.ApplicationStatus.POST_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class CommentValidatorImpl implements CommentValidator {
    private final PostRepository postRepository;


    @Override
    public void validateCreation(Comment comment) {
        comment.validateValues();

        if (!postRepository.existsById(comment.postId())) {
            throw new NotFoundException(POST_NOT_FOUND);
        }
    }
}
