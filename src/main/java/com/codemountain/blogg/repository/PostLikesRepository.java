package com.codemountain.blogg.repository;

import com.codemountain.blogg.model.PostLikes;
import com.codemountain.blogg.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikesRepository extends JpaRepository<PostLikes, Long> {
}
