package com.example.socialnetworkproject.Service;

import com.example.socialnetworkproject.Entity.Post;
import com.example.socialnetworkproject.Repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> postList() {
        return postRepository.findAll();
    }

    public void postSave(Post post) {
        postRepository.save(post);
    }

    public List<Post> findPostByAuthorId(long id) {
        return postRepository.findByAuthorId(id);
    }

    public List<Post> findPostByAuthorIdCreatedDate(long id) {
        return postRepository.findAllByAuthorIdOrderByCreatedDateDesc(id);
    }

    public List<Post> findPostsByCreatedDate() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public void postsDeleteByAuthorId(long id) {
        postRepository.deleteAllByAuthorId(id);
    }

    public void postDeleteId(long id) {
        postRepository.deleteById(id);
    }


}

