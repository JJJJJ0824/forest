package com.dw.forest.service;

import com.dw.forest.dto.CartDTO;
import com.dw.forest.dto.DiscountDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.*;
import com.dw.forest.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    TravelerRepository travelerRepository;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    CompletionRepository completionRepository;

    public List<CartDTO> getAllCarts() {
        return cartRepository.findAll().stream().map(Cart::toDTO).toList();
    }

    public CartDTO addCourseToCart(HttpServletRequest request,CartDTO cartDTO) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        if (cartDTO.getCourseId() == null) {
            throw new ResourceNotFoundException("카트를 추가할 수 없습니다. 올바른 강의 ID를 입력하세요.");
        }

        Course course = courseRepository.findById(cartDTO.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("해당 강의를 찾을 수 없습니다."));

        Traveler traveler = travelerRepository.findById(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을 수 없습니다."));

        Cart cart = new Cart();
        cart.setTraveler(traveler);
        cart.setCourse(course);
        cart.setPurchaseStatus(cartDTO.isPurchaseStatus());
        cartRepository.save(cart);

        return cart.toDTO();
    }

    public List<CartDTO> getCartByTravelerName(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        List<Cart> cartItems = cartRepository.findByTraveler_TravelerName(travelerName);

        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("장바구니를 찾을 수 없습니다. 올바른 여행자명을 입력하세요.");
        }

        return cartItems.stream().map(Cart::toDTO).toList();
    }

    public String checkoutCart(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리

        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }

        String travelerName = (String) session.getAttribute("travelerName");

        List<Cart> cartList = cartRepository.findToPurchase(travelerName);

        if (cartList.isEmpty()) {
            return "장바구니에 담긴 강의가 없습니다.";
        }

        double totalAmount = 0.0;

        for (Cart cart : cartList) {
            totalAmount += cart.getCourse().getPrice();
        }

        // 사용 가능한 포인트 조회
        Double useAblePoints = pointRepository.getUseAblePoints(travelerName);

        // 포인트가 부족하면 결제 불가
        if (useAblePoints == null || useAblePoints < totalAmount) {
            return "포인트가 부족하여 결제할 수 없습니다.";
        }
        double remainingPoints = useAblePoints - totalAmount;

        pointRepository.usePoint(totalAmount, travelerName);

        cartRepository.updateAllPurchaseStatus(travelerName);

//        savePurchase(travelerName, totalAmount, cartList);

        return "결제가 완료되었습니다. 총 금액: " + totalAmount + ", 남은 포인트: " + remainingPoints;
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

    public String clearCartOfTraveler(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        List<Cart> carts = cartRepository.findByTraveler_TravelerName(travelerName);

        if (carts.isEmpty()) {
            throw new ResourceNotFoundException("해당 사용자의 장바구니가 비어있습니다");
        }

        cartRepository.deleteAll(carts);

        return "사용자의 장바구니가 모두 삭제되었습니다.";
    }

    public double calculateTotalPrice(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

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

    public List<CartDTO> addMultipleCoursesToCart(HttpServletRequest request, List<Long> courseIds) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

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

    public DiscountDTO applyDiscountToCart(HttpServletRequest request, String discountCode) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
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

    public String checkout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }

        String travelerName = (String) session.getAttribute("travelerName");
        Traveler traveler = travelerRepository.findByTravelerName(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("여행자를 찾을 수 없습니다."));

        List<Cart> carts = cartRepository.findByTravelerTravelerNameAndPurchaseStatus(travelerName, false);
        if (carts.isEmpty()) {
            throw new ResourceNotFoundException("장바구니에 결제할 강의가 없습니다.");
        }

        double totalAmount = calculateCartTotal(carts);

        Double useAblePoints = pointRepository.getUseAblePoints(travelerName);
        if (useAblePoints == null) {
            // null일 경우 기본값을 사용하거나, 예외 처리
            useAblePoints = 0.0; // 기본값 설정
        }
        if (useAblePoints < totalAmount) {
            throw new InvalidRequestException("잔액이 부족하여 결제를 진행할 수 없습니다.");
        }

        processPurchase(traveler, carts, totalAmount);

        return "결제가 완료되었습니다. 총 결제 금액: " +(long) totalAmount + ". 잔액: " + useAblePoints.doubleValue();
    }

    private void processPurchase(Traveler traveler, List<Cart> carts, double totalAmount) {
        for (Cart cartItem : carts) {
            Point point = new Point();
            point.setTraveler(traveler);
            point.setActionType("강의 구매");
            point.setPoints(-cartItem.getCourse().getPrice());
            point.setEventDate(LocalDate.now());
            point.setCart_fk(null);

            pointRepository.save(point);

            Completion completion = new Completion(null, traveler, cartItem.getCourse(), null, point);
            completionRepository.save(completion);
            cartItem.setPurchaseStatus(true);
            cartRepository.save(cartItem);
            cartRepository.deleteById(cartItem.getId());
        }

        pointRepository.usePoint(totalAmount, traveler.getTravelerName());
    }
}
