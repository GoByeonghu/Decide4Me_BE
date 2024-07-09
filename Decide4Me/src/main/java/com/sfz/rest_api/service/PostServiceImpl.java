package com.sfz.rest_api.service;

import com.sfz.rest_api.model.Comment;
import com.sfz.rest_api.model.Post;
import com.sfz.rest_api.model.User;
import com.sfz.rest_api.model.VoteOption;
import com.sfz.rest_api.repository.CommentRepository;
import com.sfz.rest_api.repository.PostRepository;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    //private final CommentRepository

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
        //return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found with id " + id));

        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found with id " + id));

//        List<Comment> comments = commentRepository.findByPostId(postId);
//        post.setComments(comments);
//
//        List<VoteOption> voteOptions = voteOptionRepository.findByPostId(postId);
//        post.setVoteOptions(voteOptions);

        return post;
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

        if (existingPost != null) {

            if (post.getTitle() != null) {
                existingPost.setTitle(post.getTitle());
            }

            if (post.getImage() != null) {
                existingPost.setImage(post.getImage());
            }

            if (post.getContent() != null) {
                existingPost.setContent(post.getContent());
            }

            if (post.getUpdatedAt() != null) {
                existingPost.setUpdatedAt(LocalDateTime.now());
            }

            if (post.getDeadline() != null) {
                existingPost.setDeadline(post.getDeadline());
            }

            if (post.getCreatedAt() != null) {
                existingPost.setCreatedAt(post.getCreatedAt());
            }

            return postRepository.save(existingPost);
        }

        return null;
    }

    @Override
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found with id " + id);
        }
        postRepository.deleteById(id);
    }
}
