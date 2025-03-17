package com.hhplus.precourse.comment.service;

import com.hhplus.precourse.comment.repository.CommentRepository;
import com.hhplus.precourse.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hhplus.precourse.common.support.ApplicationStatus.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminDeleteCommentService {
    private final CommentRepository commentRepository;

    @Transactional
    public void delete(long id) {
        var comment = commentRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND));

        commentRepository.delete(comment);
    }
} 