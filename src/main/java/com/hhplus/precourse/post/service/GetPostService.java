package com.hhplus.precourse.post.service;

import com.hhplus.precourse.comment.repository.CommentRepository;
import com.hhplus.precourse.common.exception.NotFoundException;
import com.hhplus.precourse.post.repository.PostRepository;
import com.hhplus.precourse.post.vo.PostDetails;
import com.hhplus.precourse.post.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hhplus.precourse.common.support.ApplicationStatus.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GetPostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public PostDetails get(long id) {
        var post = postRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(POST_NOT_FOUND));
        var comments = commentRepository.findAllByPostId(
            id,
            Sort.by(Order.desc("createdAt"))
        );

        return PostDetails.of(post, comments);
    }
}
