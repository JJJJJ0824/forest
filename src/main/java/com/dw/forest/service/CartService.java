package com.dw.forest.service;

import com.dw.forest.model.Cart;
import com.dw.forest.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }
}
