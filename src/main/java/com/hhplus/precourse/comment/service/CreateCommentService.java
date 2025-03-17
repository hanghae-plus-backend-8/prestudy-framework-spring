package com.hhplus.precourse.comment.service;

import com.hhplus.precourse.comment.domain.Comment;
import com.hhplus.precourse.comment.domain.CommentValidator;
import com.hhplus.precourse.comment.repository.CommentRepository;
import com.hhplus.precourse.comment.vo.CommentVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateCommentService {
    private final CommentValidator commentValidator;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentVo create(Command command) {
        var comment = new Comment(
            command.postId(),
            command.userId(),
            command.content(),
            commentValidator
        );

        var saved = commentRepository.save(comment);

        return CommentVo.from(saved);
    }

    public record Command(
        long postId,
        long userId,
        String content
    ) {
    }
}
