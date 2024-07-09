package com.sfz.rest_api.controller;

import com.sfz.rest_api.dto.CommentRequest;
import com.sfz.rest_api.dto.CommentResponse;
import com.sfz.rest_api.dto.ResponseForm;
import com.sfz.rest_api.model.Comment;
import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.User;
import com.sfz.rest_api.service.CommentService;
import com.sfz.rest_api.service.PostService;
import com.sfz.rest_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService,
                             PostService postService,
                             UserService userService) {
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ResponseForm> getCommentsByPostId(@RequestParam(name = "post_id") Long postId,
                                                            @RequestParam(name = "order_by", required = false, defaultValue = "created_at") String order_by) {

        List<Comment> comments = commentService.getCommentsByPostId(postId);
        // Sort comments based on order_by parameter
        if ("created_at".equalsIgnoreCase(order_by)) {
            comments.sort(Comparator.comparing(Comment::getCreatedAt));
        } else if ("updated_at".equalsIgnoreCase(order_by)) {
            comments.sort(Comparator.comparing(Comment::getUpdatedAt));
        } else {
            // Default sorting or throw an exception if needed
            comments.sort(Comparator.comparing(Comment::getCreatedAt));
        }

        List<CommentResponse> commentResponses = new ArrayList<CommentResponse>();
        for (Comment comment : comments) {
            CommentResponse commentResponse = new CommentResponse(comment);
            commentResponses.add(commentResponse);
        }
        ResponseForm response = new ResponseForm("ok", commentResponses);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseForm> createComment(@RequestBody CommentRequest commentRequest) {
        //Comment newComment = commentService.createComment(comment);
        User user = userService.getUserByNickname(commentRequest.getUser_id());
        Post post = postService.getPostById(commentRequest.getPost_id());

        Comment newCommentData = new Comment();
        newCommentData.setUser(user);
        newCommentData.setPost(post);
        newCommentData.setCreatedAt(commentRequest.getCreated_at());
        newCommentData.setUpdatedAt(commentRequest.getUpdated_at());
        newCommentData.setContent(commentRequest.getContent());

        Comment newComment = commentService.createComment(newCommentData);
        CommentResponse commentResponse = new CommentResponse(newComment);
        ResponseForm response = new ResponseForm("ok", commentResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseForm> getCommentById(@PathVariable("id") Long id) {
        Comment comment = commentService.getCommentById(id);
        CommentResponse commentResponse = new CommentResponse(comment);
        ResponseForm response = new ResponseForm("ok", commentResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseForm> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        ResponseForm response = new ResponseForm("ok", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseForm> updateComment(@PathVariable("id") Long id, @RequestBody Comment comment) {
        Comment updatedComment = commentService.updateComment(id, comment);
        CommentResponse commentResponse = new CommentResponse(updatedComment);
        ResponseForm response = new ResponseForm("ok", commentResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
