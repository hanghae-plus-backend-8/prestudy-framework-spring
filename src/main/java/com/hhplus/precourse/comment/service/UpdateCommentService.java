package com.hhplus.precourse.comment.service;

import com.hhplus.precourse.comment.domain.CommentValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhplus.precourse.comment.repository.CommentRepository;
import com.hhplus.precourse.comment.vo.CommentVo;
import com.hhplus.precourse.common.exception.BadRequestException;
import com.hhplus.precourse.common.exception.NotFoundException;
import static com.hhplus.precourse.common.support.ApplicationStatus.COMMENT_NOT_FOUND;
import static com.hhplus.precourse.common.support.ApplicationStatus.UNAUTHORIZED;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateCommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public CommentVo update(Command command) {
        var comment = commentRepository.findById(command.id())
            .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));

        if (!comment.isAuthor(command.userId())) {
            throw new BadRequestException(UNAUTHORIZED, "작성자만 수정할 수 있습니다.");
        }

        comment.update(command.content());

        return CommentVo.from(comment);
    }

    public record Command(
        long id,
        long userId,
        String content
    ) {
    }
} 