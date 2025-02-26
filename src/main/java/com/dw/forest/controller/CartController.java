package com.dw.forest.controller;

import com.dw.forest.dto.CartDTO;
import com.dw.forest.dto.CouponCodeDTO;
import com.dw.forest.dto.DiscountDTO;
import com.dw.forest.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/all")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        return new ResponseEntity<>(cartService.getAllCarts(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addCourseToCart(HttpServletRequest request,@RequestBody CartDTO cartDTO) {
        return new ResponseEntity<>(cartService.addCourseToCart(request, cartDTO), HttpStatus.CREATED);
    }

    @GetMapping("/mycart")
    public ResponseEntity<List<CartDTO>> getCartByTravelerName(HttpServletRequest request) {
        return new ResponseEntity<>(cartService.getCartByTravelerName(request), HttpStatus.OK);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkoutCart(HttpServletRequest request) {
        return new ResponseEntity<>(cartService.checkoutCart(request), HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> removeCourseFromCart(@PathVariable Long cartId) {
        return new ResponseEntity<>(cartService.removeCourseFromCart(cartId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCartOfTraveler(HttpServletRequest request){
        return new ResponseEntity<>(cartService.clearCartOfTraveler(request), HttpStatus.ACCEPTED);
    }

    @GetMapping("/total-price")
    public ResponseEntity<Double> calculateTotalPrice(HttpServletRequest request) {
        return new ResponseEntity<>(cartService.calculateTotalPrice(request), HttpStatus.OK);
    }

    @PostMapping("/add-multiple")
    public ResponseEntity<List<CartDTO>> addMultipleCoursesToCart(HttpServletRequest request, @RequestBody List<Long> courseIds) {
        return new ResponseEntity<>(
                cartService.addMultipleCoursesToCart(request, courseIds), HttpStatus.OK);
    }

    @PutMapping("/apply-discount")
    public ResponseEntity<DiscountDTO> applyDiscount(HttpServletRequest request, @RequestBody CouponCodeDTO couponCodeDTO) {
        String discountCode = couponCodeDTO.getDiscountCode();
//        System.out.println("전달 받은 할인 코드 : " + discountCode);
        return new ResponseEntity<>(cartService.applyDiscountToCart(request, discountCode), HttpStatus.OK);
    }

    @PutMapping("/checkout")
    public ResponseEntity<String> checkout(HttpServletRequest request) {
        String message = cartService.checkout(request);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}