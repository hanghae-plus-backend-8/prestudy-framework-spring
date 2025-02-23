package com.hhplus.precourse.post.vo;

import com.hhplus.precourse.post.domain.Post;

public record PostVo(
    long id,
    String author,
    String title,
    String content,
    String password
) {
    public static PostVo from(Post post) {
        return new PostVo(
            post.id(),
            post.title(),
            post.author(),
            post.content(),
            post.password()
        );
    }
}
