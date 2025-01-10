package com.dw.forest.controller;

import com.dw.forest.model.Cart;
import com.dw.forest.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/carts/all")
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }
}
