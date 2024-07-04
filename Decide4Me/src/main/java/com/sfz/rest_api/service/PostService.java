package com.sfz.rest_api.service;

import com.sfz.rest_api.model.Post;
import java.util.List;

public interface PostService {
    Post createPost(Post post);
    Post getPostById(Long id);
    List<Post> getAllPosts();
    List<Post> getPostsByUserId(String userId);
    Post updatePost(Long id, Post post);
    void deletePost(Long id);
}

