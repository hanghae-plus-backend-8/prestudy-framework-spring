package com.hhplus.precourse.post.service;

import com.hhplus.precourse.comment.domain.Comment;
import com.hhplus.precourse.comment.repository.CommentRepository;
import com.hhplus.precourse.post.domain.Post;
import com.hhplus.precourse.post.repository.PostRepository;
import com.hhplus.precourse.post.vo.PostSummary;
import com.hhplus.precourse.post.vo.PostVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
public class GetPostListService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<PostSummary> get() {
        var posts = postRepository.findAll(Sort.by(Order.desc("createdAt")));
        var postIds = posts.stream().map(Post::id).toList();
        var comments = commentRepository.findAllByPostIdIn(postIds, Sort.by(Order.desc("createdAt")))
            .stream()
            .collect(groupingBy(Comment::postId));

        return posts.stream()
            .map(post -> PostSummary.of(post, comments.get(post.id())))
            .toList();
    }
}
