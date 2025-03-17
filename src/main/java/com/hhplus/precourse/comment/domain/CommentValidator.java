package com.hhplus.precourse.comment.domain;

public interface CommentValidator {
    void validateCreation(Comment comment);

    void validateModification(Comment comment);
}
