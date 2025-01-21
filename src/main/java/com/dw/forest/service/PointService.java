package com.dw.forest.service;

import com.dw.forest.dto.PointConvertRequestDTO;
import com.dw.forest.dto.PointConvertResponseDTO;
import com.dw.forest.dto.PointEventDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.exception.UnauthorizedTravelerException;
import com.dw.forest.model.Point;
import com.dw.forest.model.Traveler;
import com.dw.forest.repository.PointRepository;
import com.dw.forest.repository.TravelerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PointService {
    @Autowired
    PointRepository pointRepository;

    @Autowired
    TravelerRepository travelerRepository;

    public List<PointEventDTO> getAllPoints(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        if (!travelerName.equals("admin")) {
            throw new UnauthorizedTravelerException("권한이 없습니다");
        }
        return pointRepository.findAll().stream().map(Point::toEvent).toList();
    }

    public PointEventDTO addPointsToTraveler(HttpServletRequest request, double points, String actionType) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        Traveler traveler = travelerRepository.findById(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을 수 없습니다."));

        Point point = new Point();
        point.setTraveler(traveler);
        point.setPoints(points);
        point.setActionType(actionType);
        point.setEventDate(LocalDate.now());

        return pointRepository.save(point).toEvent();
    }

    public PointEventDTO usePointsFromTraveler(HttpServletRequest request, double points, String actionType){
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }

        String travelerName = (String) session.getAttribute("travelerName");
        Traveler traveler = travelerRepository.findById(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을수 없습니다"));

        double currentPoints = pointRepository.findByTraveler(traveler).stream()
                .mapToDouble(Point::getPoints)
                .sum();

        if (currentPoints - points < 0 ) {
            throw new InvalidRequestException("포인트가 부족합니다");
        }

        Point point = new Point();
        point.setTraveler(traveler);
        point.setPoints(-points);
        point.setActionType(actionType);
        point.setEventDate(LocalDate.now());

        return pointRepository.save(point).toEvent();
    }

    public String giveDoublePointsOnEvent(PointEventDTO pointEventDTO) {
        try {
            LocalDate today = LocalDate.now();
            if (!today.equals(pointEventDTO.getEventDate())) {
                throw new InvalidRequestException("이벤트 날짜와 현재 날짜가 일치하지 않습니다.");
            }

            List<Traveler> travelers = travelerRepository.findAll();
            for (Traveler traveler : travelers) {

                double doublePoints = pointEventDTO.getPoints() * 2;
                Point point = new Point();
                point.setTraveler(traveler);
                point.setActionType(pointEventDTO.getActionType());
                point.setPoints(doublePoints);
                point.setEventDate(today);

                pointRepository.save(point);
            }
        } catch (DateTimeException e) {
            throw new InvalidRequestException("이벤트 날짜와 현재 날짜가 일치하지 않습니다.");
        }
        return "포인트 2배 이벤트 적용";
    }

    public PointConvertResponseDTO convertPointsToCoupon(PointConvertRequestDTO requestDTO) {
        Traveler traveler = travelerRepository.findById(requestDTO.getTravelerName())
                .orElseThrow(() -> new ResourceNotFoundException("유저를 찾을수 없습니다."));

        List<Point> points = pointRepository.findByTraveler(traveler);

        double totalPoints = points.stream().mapToDouble(Point::getPoints).sum();

        if (totalPoints < requestDTO.getPointsToConvert()) {
            throw new InvalidRequestException("전환할 포인트가 부족합니다.");
        }
        String couponCode = generateCouponCode();

        double pointUsed = requestDTO.getPointsToConvert();
        for (Point point : points) {
            if (pointUsed < 0) {
                throw new InvalidRequestException("전환할 포인트가 부족합니다.");
            };

            if (point.getPoints() >= pointUsed) {
                point.setPoints(point.getPoints() - pointUsed);
                pointUsed = 0;
            } else {
                pointUsed -= point.getPoints();
                point.setPoints(0);
            }
        }

        pointRepository.saveAll(points);

        PointConvertResponseDTO responseDTO = new PointConvertResponseDTO();
        responseDTO.setCouponCode(couponCode);
        responseDTO.setPointsUsed(requestDTO.getPointsToConvert());
        responseDTO.setMessage("포인트가 성공적으로 쿠폰전환 완료");

        return responseDTO;
    }

    private String generateCouponCode() {
        // 쿠폰 코드 생성
        return UUID.randomUUID().toString();
    }

    public List<PointEventDTO> getAllPointsOfTraveler(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        List<PointEventDTO> pointDTOList = pointRepository.findByTravelerTravelerName(travelerName).stream()
                .map(Point::toEvent).toList();

        if (pointDTOList.isEmpty()) {
            throw new ResourceNotFoundException("포인트 충전, 사용 내역이 없습니다.");
        }

        return pointDTOList;
    }

    public List<PointEventDTO> getChargedPointsOfTraveler(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        List<PointEventDTO> pointDTOList = pointRepository.findByTravelerTravelerName(travelerName).stream()
                .filter(point -> point.getPoints() > 0).map(Point::toEvent).toList();

        if (pointDTOList.isEmpty()) {
            throw new ResourceNotFoundException("포인트 충전 내역이 없습니다.");
        }

        return pointDTOList;
    }

    public List<PointEventDTO> getChargedPointsOfTravelers(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        if (!travelerName.equals("admin")) {
            throw new UnauthorizedTravelerException("권한이 없습니다.");
        }
        List<PointEventDTO> pointDTOList = pointRepository.findAll().stream()
                .filter(point -> point.getPoints() > 0).map(Point::toEvent).toList();

        if (pointDTOList.isEmpty()) {
            throw new ResourceNotFoundException("포인트 충전 내역이 없습니다.");
        }

        return pointDTOList;
    }

    public List<PointEventDTO> getUsedPointsOfTraveler(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        List<PointEventDTO> pointDTOList = pointRepository.findByTravelerTravelerName(travelerName).stream()
                .filter(point -> point.getPoints() < 0).map(Point::toEvent).toList();

        if (pointDTOList.isEmpty()) {
            throw new ResourceNotFoundException("포인트 사용 내역이 없습니다.");
        }

        return pointDTOList;
    }

    public List<PointEventDTO> getUsedPointsOfTravelers(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        if (!travelerName.equals("admin")) {
            throw new UnauthorizedTravelerException("권한이 없습니다");
        }
        List<PointEventDTO> pointDTOList = pointRepository.findAll().stream()
                .filter(point -> point.getPoints() < 0).map(Point::toEvent).toList();

        if (pointDTOList.isEmpty()) {
            throw new ResourceNotFoundException("포인트 사용 내역이 없습니다.");
        }

        return pointDTOList;
    }

    public String deductPoints(String travelerName, double pointsToDeduct) {
        Traveler traveler = travelerRepository.findByTravelerName(travelerName)
                .orElseThrow(() -> new ResourceNotFoundException("해당 유저를 찾을 수 없습니다."));

        Double useAblePoints = pointRepository.getUseAblePoints(travelerName);

        if (useAblePoints == null || useAblePoints < pointsToDeduct) {
            throw new InvalidRequestException("차감할 포인트가 부족합니다.");
        }

        Point point = new Point();
        point.setTraveler(traveler);
        point.setActionType("POINT_DEDUCTION");
        point.setPoints(-pointsToDeduct);
        point.setEventDate(LocalDate.now());
        pointRepository.save(point);

        return "포인트 차감이 완료되었습니다. 차감된 금액: " + pointsToDeduct + "원.";
    }
}