package com.yw.blog.service;

import com.yw.blog.po.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlogId(long blogId);

    Page<Comment> listComment(Pageable pageable);

    Comment getComment(Long id);

    Comment updateComment(Long id, Comment comment);

    Comment saveComment(Comment comment);

    void deleteComment(Long id);
}
