package com.hhplus.precourse.comment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hhplus.precourse.comment.domain.Comment;
import com.hhplus.precourse.user.domain.User;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(long id, Sort sort);

    List<Comment> findAllByPostIdIn(List<Long> ids, Sort sort);
}
