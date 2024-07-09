package com.sfz.rest_api.controller;

import com.sfz.rest_api.config.ApplicationProperties;
import com.sfz.rest_api.dto.PostRequest;
import com.sfz.rest_api.dto.PostResponse;
import com.sfz.rest_api.dto.ResponseForm;
import com.sfz.rest_api.dto.UserResponse;
import com.sfz.rest_api.model.VoteOption;
import com.sfz.rest_api.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import com.sfz.rest_api.model.User;
import com.sfz.rest_api.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.ToDoubleBiFunction;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final VoteOptionService voteOptionService;
    private final CommentService commentService;
    private final ApplicationProperties applicationProperties;
    private final FileStorageService fileStorage;

    @Autowired
    public PostController(PostService postService,
                          UserService userService,
                          VoteOptionService voteOptionService,
                          CommentService commentService,
                          FileStorageService fileStorage,
                          ApplicationProperties applicationProperties) {
        this.postService = postService;
        this.userService = userService;
        this.voteOptionService=voteOptionService;
        this.commentService = commentService;
        this.fileStorage = fileStorage;
        this.applicationProperties = applicationProperties;
    }

    @GetMapping
    public ResponseEntity<ResponseForm> getPosts(
            @RequestParam(name = "order",required = false) String order,
            @RequestParam(name = "user",required = false) String user,
            @RequestParam(name = "count",required = false, defaultValue = "100") int count) {

        List<Post> posts;

        if (user != null) {
            posts = postService.getPostsByUserId(user);
        } else {
            posts = postService.getAllPosts();
        }

        // Sort the posts if order is provided
        if (order != null) {
            switch (order) {
                case "createdAt":
                    posts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));
                    break;
                case "updatedAt":
                    posts.sort((p1, p2) -> p2.getUpdatedAt().compareTo(p1.getUpdatedAt()));
                    break;
                case "deadline":
                    posts.sort((p1, p2) -> p2.getDeadline().compareTo(p1.getDeadline()));
                    break;
                // Add more cases as needed
            }
        }

        // Limit the number of posts returned based on the count parameter
        if (posts.size() > count) {
            posts = posts.subList(0, count);
        }

        List<PostResponse> postResponseList = new ArrayList<PostResponse>();
        for(Post post : posts){
            PostResponse postData = new PostResponse(post,
                    applicationProperties.getServerAddress() + applicationProperties.getStaticURL(),
                    voteOptionService.getVoteOptionByPostId(post.getId()),
                    commentService.getCommentsByPostId(post.getId()));
            postResponseList.add(postData);
        }

        ResponseForm response = new ResponseForm("ok", postResponseList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<ResponseForm> createPost(@RequestPart(value ="postData") PostRequest postRequest,
                                                   @RequestPart(value = "post_image", required = false) MultipartFile postImage) {

        try {
            User user = userService.getUserByNickname(postRequest.getUser_id());

            Post newPostData = new Post();
            newPostData.setUser(user);
            newPostData.setTitle(postRequest.getTitle());
            newPostData.setContent(postRequest.getContent());
            newPostData.setCreatedAt(postRequest.getCreated_at());
            newPostData.setUpdatedAt(postRequest.getUpdated_at());
            newPostData.setDeadline(postRequest.getDeadline());
            Post newPost = postService.createPost(newPostData);

            if (postImage != null && !postImage.isEmpty()) {
                String newProfilePath = fileStorage.storePostImage(postImage, newPost.getId());
                newPost.setImage(newProfilePath);
                newPost = postService.updatePost(newPost.getId(), newPost);
            }
            //옵션저장
            for (VoteOption voteOption : postRequest.getVoteOptions()) {
                voteOption.setPost(newPost);
                voteOptionService.createVoteOption(voteOption);
            }
            PostResponse postData = new PostResponse(newPost,
                    applicationProperties.getServerAddress() + applicationProperties.getStaticURL(),
                    voteOptionService.getVoteOptionByPostId(newPost.getId()),
                    null);
            ResponseForm response = new ResponseForm("ok", postData);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (java.io.IOException e) {
            ResponseForm response = new ResponseForm("error", "File upload failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseForm> getPostById(@PathVariable("id") Long id) {
        Post post = postService.getPostById(id);

        PostResponse postData = new PostResponse(post,
                applicationProperties.getServerAddress() + applicationProperties.getStaticURL(),
                voteOptionService.getVoteOptionByPostId(post.getId()),
                commentService.getCommentsByPostId(post.getId()));
        ResponseForm response = new ResponseForm("ok", postData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseForm> deletePost(@PathVariable("id") Long id) {
        try {
            Post post = postService.getPostById(id);

            if (post.getImage() != null) {
                fileStorage.deleteFile(applicationProperties.getFileServer() + post.getImage());
            }
            postService.deletePost(id);
            ResponseForm response = new ResponseForm("ok", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (java.io.IOException e) {
            ResponseForm response = new ResponseForm("error", "File delete failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<ResponseForm> updatePost(@PathVariable("id") Long id,
                                           @RequestPart(value ="postData", required = false) Post post,
                                           @RequestPart(value = "post_image", required = false) MultipartFile image) {
        try {
            Post legacyPost = postService.getPostById(id);

            if(post == null){
                post = legacyPost;
            }
            if (image != null && !image.isEmpty()) {
                //이미 이미지 있는 경우 해당 이미지 삭제
                if(legacyPost.getImage() != null) {
                    fileStorage.deleteFile(applicationProperties.getFileServer()+legacyPost.getImage());
                }
                String newProfilePath = fileStorage.storePostImage(image, legacyPost.getId());
                post.setImage(newProfilePath);
            }

            Post newPost = postService.updatePost(legacyPost.getId(), post);

            PostResponse postData = new PostResponse(newPost,
                    applicationProperties.getServerAddress() + applicationProperties.getStaticURL(),
                    voteOptionService.getVoteOptionByPostId(post.getId()),
                    commentService.getCommentsByPostId(post.getId()));
            ResponseForm response = new ResponseForm("ok", postData);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (java.io.IOException e) {
            ResponseForm response = new ResponseForm("error", "File upload failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
