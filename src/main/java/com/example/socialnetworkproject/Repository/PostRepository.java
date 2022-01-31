package com.example.socialnetworkproject.Repository;

import com.example.socialnetworkproject.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedDateDesc();

    List<Post> findAllByAuthorIdOrderByCreatedDateDesc(long id);

    List<Post> findByAuthorId(long id);

    @Transactional
    void deleteAllByAuthorId(long id);
}