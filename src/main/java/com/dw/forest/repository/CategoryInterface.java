package com.dw.forest.repository;

import com.dw.forest.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryInterface extends JpaRepository<Category, Long> {

}
