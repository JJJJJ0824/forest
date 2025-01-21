package com.dw.forest.controller;

import com.dw.forest.dto.CartDTO;
import com.dw.forest.dto.CouponCodeDTO;
import com.dw.forest.dto.DiscountDTO;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.NonUniqueResultException;
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
    public ResponseEntity<CartDTO> addCourseToCart(@RequestBody CartDTO cartDTO) {
        return new ResponseEntity<>(cartService.addCourseToCart(cartDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{travelerName}")
    public ResponseEntity<List<CartDTO>> getCartByTravelerName(@PathVariable String travelerName) {
        return new ResponseEntity<>(cartService.getCartByTravelerName(travelerName), HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> removeCourseFromCart(@PathVariable Long cartId) {
        return new ResponseEntity<>(cartService.removeCourseFromCart(cartId), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{travelerName}/clear")
    public ResponseEntity<String> clearCartOfTraveler(@PathVariable String travelerName){
        return new ResponseEntity<>(cartService.clearCartOfTraveler(travelerName), HttpStatus.ACCEPTED);
    }

    @GetMapping("{travelerName}/total-price")
    public ResponseEntity<Double> calculateTotalPrice(@PathVariable String travelerName) {
        return new ResponseEntity<>(cartService.calculateTotalPrice(travelerName), HttpStatus.OK);
    }

    @GetMapping("/{travelerName}/exists/{courseId}")
    public ResponseEntity<String> isCourseInCart(HttpServletRequest request, @PathVariable Long courseId) {
        try {
            boolean isInCart = cartService.isCourseInCart(request, courseId);

            if (isInCart) {
                return new ResponseEntity<>("해당 강의는 이미 장바구니 안에 있습니다.", HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException("해당 강의는 장바구니 안에 없습니다.");
            }
        } catch (NonUniqueResultException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @PostMapping("/{travelerName}/add-multiple")
    public ResponseEntity<List<CartDTO>> addMultipleCoursesToCart(@PathVariable String travelerName, @RequestBody List<Long> courseIds) {
        return new ResponseEntity<>(
                cartService.addMultipleCoursesToCart(travelerName, courseIds), HttpStatus.OK);
    }

    @PutMapping("/{travelerName}/apply-discount")
    public ResponseEntity<DiscountDTO> applyDiscount(@PathVariable String travelerName, @RequestBody CouponCodeDTO couponCodeDTO) {
        String discountCode = couponCodeDTO.getDiscountCode();
//        System.out.println("전달 받은 할인 코드 : " + discountCode);
        return new ResponseEntity<>(cartService.applyDiscountToCart(travelerName, discountCode), HttpStatus.OK);
    }
}