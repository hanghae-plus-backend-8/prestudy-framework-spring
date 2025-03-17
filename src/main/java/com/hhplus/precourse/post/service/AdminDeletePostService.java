package com.hhplus.precourse.post.service;

import com.hhplus.precourse.common.exception.NotFoundException;
import com.hhplus.precourse.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hhplus.precourse.common.support.ApplicationStatus.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AdminDeletePostService {
    private final PostRepository postRepository;

    @Transactional
    public void delete(long id) {
        var post = postRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));

        postRepository.delete(post);
    }
} 