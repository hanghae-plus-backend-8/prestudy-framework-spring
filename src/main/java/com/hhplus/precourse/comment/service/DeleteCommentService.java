package com.hhplus.precourse.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhplus.precourse.comment.repository.CommentRepository;
import com.hhplus.precourse.common.exception.BadRequestException;
import com.hhplus.precourse.common.exception.NotFoundException;
import static com.hhplus.precourse.common.support.ApplicationStatus.COMMENT_NOT_FOUND;
import static com.hhplus.precourse.common.support.ApplicationStatus.UNAUTHORIZED;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteCommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public void delete(long id, long userId) {
        var comment = commentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));

        if (comment.isNotAuthor(userId)) {
            throw new BadRequestException(UNAUTHORIZED, "작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
} 