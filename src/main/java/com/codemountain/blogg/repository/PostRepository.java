package com.codemountain.blogg.repository;

import com.codemountain.blogg.model.Post;
import com.codemountain.blogg.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Long countByCreatedBy(Long id);
}
