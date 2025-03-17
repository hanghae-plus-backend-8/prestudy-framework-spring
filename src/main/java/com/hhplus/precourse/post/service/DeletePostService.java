package com.hhplus.precourse.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhplus.precourse.common.exception.BadRequestException;
import com.hhplus.precourse.common.exception.NotFoundException;
import static com.hhplus.precourse.common.support.ApplicationStatus.POST_NOT_FOUND;
import static com.hhplus.precourse.common.support.ApplicationStatus.UNAUTHORIZED;
import com.hhplus.precourse.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeletePostService {
    private final PostRepository postRepository;

    @Transactional
    public void delete(long id, long userId) {
        var post = postRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));

        if (!post.isAuthor(userId)) {
            throw new BadRequestException(UNAUTHORIZED, "작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }
}