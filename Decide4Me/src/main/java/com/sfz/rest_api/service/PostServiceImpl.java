package com.sfz.rest_api.service;

import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found with id " + id));
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getPostsByUserId(String userId) {
        return postRepository.findByUserId(userId);
    }

    @Override
    public Post updatePost(Long id, Post post) {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found with id " + id));
        existingPost.setTitle(post.getTitle());
        existingPost.setImage(post.getImage());
        existingPost.setContent(post.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());
        existingPost.setDeadline(post.getDeadline());
        existingPost.setLikeCount(post.getLikeCount());
        existingPost.setDislikeCount(post.getDislikeCount());
        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found with id " + id);
        }
        postRepository.deleteById(id);
    }
}
