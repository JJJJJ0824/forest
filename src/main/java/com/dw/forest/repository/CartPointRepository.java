package com.dw.forest.repository;

import com.dw.forest.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartPointRepository extends JpaRepository<Cart, Long> {

}
