package com.sfz.rest_api.service;

import com.sfz.rest_api.model.Comment;
import java.util.List;

public interface CommentService {
    Comment createComment(Comment comment);
    Comment getCommentById(Long id);
    List<Comment> getCommentsByPostId(Long postId);
    List<Comment> getCommentsByUserId(String userId);
    Comment updateComment(Long id, Comment comment);
    void deleteComment(Long id);
}

