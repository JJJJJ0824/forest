package com.dw.forest.service;

import com.dw.forest.dto.CartDTO;
import com.dw.forest.dto.DiscountDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.Cart;
import com.dw.forest.model.Course;
import com.dw.forest.model.Traveler;
import com.dw.forest.repository.CartRepository;
import com.dw.forest.repository.CourseRepository;
import com.dw.forest.repository.TravelerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    TravelerRepository travelerRepository;

    public List<CartDTO> getAllCarts() {
        return cartRepository.findAll().stream().map(Cart::toDTO).toList();
    }

    public CartDTO addCourseToCart(CartDTO cartDTO) {
        if (cartDTO.getCourseId() == null || cartDTO.getTravelerName() == null) {
            throw new ResourceNotFoundException("카트를 추가할 수 없습니다. 올바른 트래블러 이름과 강의 ID를 확인하세요");
        }

        Course course = courseRepository.findById(cartDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다."));

        Traveler traveler = travelerRepository.findById(cartDTO.getTravelerName())
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을 수 없습니다."));

        Cart cart = new Cart();
        cart.setTraveler(traveler);
        cart.setCourse(course);
        cart.setPurchaseStatus(cartDTO.isPurchaseStatus());
        cartRepository.save(cart);

        return cart.toDTO();
    }

    public List<CartDTO> getCartByTravelerName(String travelerName) {
        List<Cart> cartItems = cartRepository.findByTraveler_TravelerName(travelerName);

        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("장바구니를 찾을 수 없습니다. 올바른 여행자명을 입력하세요.");
        }

        return cartItems.stream().map(Cart::toDTO).toList();
    }


    public String removeCourseFromCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("해당 장바구니 항목을 찾을 수 없습니다."));

        if (cart.equals(new Cart())) {
            throw new ResourceNotFoundException("해당 장바구니 항목을 찾을 수 없습니다.");
        }

        if (cart.isPurchaseStatus()) {
            throw new ResourceNotFoundException("구매 완료된 강의는 삭제할 수 없습니다.");
        }

        cartRepository.delete(cart);

        return "강의가 장바구니에서 삭제되었습니다.";
    }

    public String clearCartOfTraveler(String travelerName) {
        List<Cart> carts = cartRepository.findByTraveler_TravelerName(travelerName);

        if (carts.isEmpty()) {
            throw new ResourceNotFoundException("해당 사용자의 장바구니가 비어있습니다");
        }

        cartRepository.deleteAll(carts);

        return "사용자의 장바구니가 모두 삭제되었습니다.";
    }

    public double calculateTotalPrice(String travelerName) {
        List<Cart> carts = cartRepository.findByTraveler_TravelerName(travelerName);

        if (carts.isEmpty()) {
            throw new ResourceNotFoundException("해당 여행자의 장바구니가 비어있습니다");
        }

        double totalPrice = 0;

        for (Cart cart : carts) {
            totalPrice += cart.getCourse().getPrice();
        }
        return totalPrice;
    }

    public boolean isCourseInCart(String travelerName, Long courseId) {
        travelerRepository.findById(travelerName).orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을 수 없습니다."));

        courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다"));

        Cart cart = cartRepository.findByTraveler_TravelerNameAndCourse_CourseId(travelerName, courseId)
                .orElseThrow(()->new ResourceNotFoundException("카트를 찾을 수 없습니다."));

        if (cart.equals(new Cart())){
            throw new ResourceNotFoundException("해당 강의는 장바구니에 없습니다");
        }
        return true;
    }

    public List<CartDTO> addMultipleCoursesToCart(String travelerName, List<Long> courseIds) {
        List<Cart> carts = new ArrayList<>();

        for (Long courseId : courseIds) {
            Cart cart = new Cart();
            cart.setTraveler(travelerRepository.findById(travelerName).orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다.")));
            cart.setCourse(courseRepository.findById(courseId).orElseThrow(()->new ResourceNotFoundException("해당 강좌가 없습니다.")));
            cart.setPurchaseStatus(false);
            cart.addPoint("강좌 담기", -courseRepository.findById(courseId).orElseThrow().getPrice());
            carts.add(cart);
        }
        cartRepository.saveAll(carts);

        return carts.stream().map(Cart::toDTO).toList();
    }

    public DiscountDTO applyDiscountToCart(String travelerName, String discountCode) {
        if (discountCode==null) {
            throw new InvalidRequestException("유효하지 않은 할인 코드입니다.")  ;
        }
        List<Cart> carts = cartRepository.findByTraveler_TravelerName(travelerName);

        if (carts.isEmpty()) {
            throw new ResourceNotFoundException("해당 여행자의 장바구니가 비어있습니다");
        }

        // 총 금액 계산
        double originalTotal = calculateCartTotal(carts);

        // 할인율 계산
        double discountRate = getDiscountRate(discountCode.toUpperCase());
        if (discountRate == 0.0) {
            throw new InvalidRequestException("유효하지 않은 할인 코드입니다.");
        }

        // 할인 적용
        double discountedTotal = originalTotal * (1 - discountRate);

        // 결과 생성
        return new DiscountDTO(discountCode, originalTotal, discountedTotal, discountRate * 100);
    }

    private double calculateCartTotal(List<Cart> carts) {
        // 카트의 Course 가격 총합 계산
        return carts.stream()
                .mapToDouble(cart -> cart.getCourse().getPrice())
                .sum();
    }

    private double getDiscountRate(String discountCode) {
        // 할인율 로직을 Service 내부에서 처리
        return switch (discountCode) {
            case "SUMMER" -> 0.15;
            case "WINTER" -> 0.20;
            case "SPRING" -> 0.10;
            default -> 0.0;
        };
    }
}
