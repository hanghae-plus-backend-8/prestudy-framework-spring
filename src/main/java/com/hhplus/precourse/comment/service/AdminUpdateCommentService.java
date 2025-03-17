package com.hhplus.precourse.comment.service;

import com.hhplus.precourse.comment.repository.CommentRepository;
import com.hhplus.precourse.comment.vo.CommentVo;
import com.hhplus.precourse.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hhplus.precourse.common.support.ApplicationStatus.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminUpdateCommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public CommentVo update(Command command) {
        var comment = commentRepository.findById(command.id())
            .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));

        comment.update(command.content());

        return CommentVo.from(comment);
    }

    public record Command(
        long id,
        String content
    ) {
    }
} 