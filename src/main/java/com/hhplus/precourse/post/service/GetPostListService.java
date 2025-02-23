package com.hhplus.precourse.post.service;

import com.hhplus.precourse.post.repository.PostRepository;
import com.hhplus.precourse.post.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPostListService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<PostVo> get() {
        var sort= Sort.by(Sort.Order.desc("createdAt"));
        return postRepository.findAll(sort)
            .stream()
            .map(PostVo::from)
            .toList();
    }
}
