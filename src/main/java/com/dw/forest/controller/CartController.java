package com.dw.forest.controller;

import com.dw.forest.dto.CartDTO;
import com.dw.forest.dto.DiscountResponseDTO;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.Cart;
import com.dw.forest.model.Course;
import com.dw.forest.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/all")
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    @PostMapping("/add")
    public ResponseEntity<CartDTO> addCourseToCart(@RequestBody CartDTO cartDTO) {

        return new ResponseEntity<>(cartService.addCourseToCart(cartDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{travelerName}")
    public ResponseEntity<List<CartDTO>> getCartByTravelerName(@PathVariable String travelerName) {
        List<CartDTO> cartDTOs = cartService.getCartByTravelerName(travelerName);
        if (cartDTOs.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> removeCourseFromCart(@PathVariable Long cartId) {
        String result = cartService.removeCourseFromCart(cartId);

        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{travelerName}/clear")
    public ResponseEntity<String> clearCartForTraveler(@PathVariable String travelerName){
        String result = cartService.clearCartForTraveler(travelerName);
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @GetMapping("{travelerName}/total-price")
    public ResponseEntity<Double> calculateTotalPrice(@PathVariable String travelerName) {
        double totalPrice = cartService.calculateTotalPrice(travelerName);
        return new ResponseEntity<>(totalPrice, HttpStatus.OK);
    }

    @GetMapping("/{travelerName}/exists/{courseId}")
    public ResponseEntity<String> isCourseInCart(@PathVariable String travelerName, @PathVariable Long courseId) {
        try {
            boolean isInCart = cartService.isCourseInCart(travelerName, courseId);

            if (isInCart) {
                return new ResponseEntity<>("해당 강의는 이미 장바구니 안에 있습니다.", HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException("해당 강의는 장바구니 안에 없습니다.");
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @PostMapping("/{travelerName}/add-multiple")
    public ResponseEntity<List<CartDTO>> addMultipleCoursesToCart(@PathVariable String travelerName, @RequestBody List<Long> courseIds) {
        return new ResponseEntity<>(
                cartService.addMultipleCoursesToCart(travelerName, courseIds, false), HttpStatus.OK);
    }

    @PutMapping("/{travelerName}/apply-discount")
    public ResponseEntity<DiscountResponseDTO> applyDiscount(@PathVariable String travelerName, @RequestBody String discountCode) {
        return new ResponseEntity<>(cartService.applyDiscountToCart(travelerName,discountCode), HttpStatus.OK);
    }
}