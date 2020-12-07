package com.codemountain.blogg.repository;

import com.codemountain.blogg.model.Category;
import com.codemountain.blogg.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
