package com.hhplus.precourse.post.domain;

import com.hhplus.precourse.common.FixtureReflectionUtils;
import com.hhplus.precourse.common.TestFixture;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class PostFixture implements TestFixture<Post> {
    private long id = 1L;
    private String author = "testAuthor";
    private String title = "testTitle";
    private String password = "testPassword";
    private String content = "testContent";

    @Override
    public Post build() {
        var entity = new Post();
        FixtureReflectionUtils.reflect(entity, this);
        return entity;
    }
}
