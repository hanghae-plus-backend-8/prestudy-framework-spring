package com.hhplus.precourse.post.service;

import com.hhplus.precourse.common.exception.NotFoundException;
import com.hhplus.precourse.post.repository.PostRepository;
import com.hhplus.precourse.post.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hhplus.precourse.common.support.ApplicationStatus.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GetPostService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public PostVo get(long id) {
        return postRepository.findById(id)
            .map(PostVo::from)
            .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));
    }
}
