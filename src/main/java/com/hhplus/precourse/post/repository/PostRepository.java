package com.hhplus.precourse.post.repository;

import com.hhplus.precourse.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
