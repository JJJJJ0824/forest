package com.dw.forest.controller;

import com.dw.forest.dto.PointConvertRequestDTO;
import com.dw.forest.dto.PointConvertResponseDTO;
import com.dw.forest.dto.PointDTO;
import com.dw.forest.dto.PointEventDTO;
import com.dw.forest.model.Point;
import com.dw.forest.service.PointService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/point")
public class PointController {
    @Autowired
    PointService pointService;

    @GetMapping("/all")
    public ResponseEntity<List<PointEventDTO>> getAllPoints(HttpServletRequest request) {
        return new ResponseEntity<>(pointService.getAllPoints(request), HttpStatus.OK);
    }

    @PostMapping("/add")
    public PointEventDTO addPoints(HttpServletRequest request, @RequestBody PointDTO pointDTO) {
        return pointService.addPointsToTraveler(request, pointDTO.getPoints(), pointDTO.getActionType());
    }

    @PostMapping("/use")
    public PointEventDTO usePoints(HttpServletRequest request, @RequestBody PointDTO pointDTO) {
        return pointService.usePointsFromTraveler(request, pointDTO.getPoints(), pointDTO.getActionType());
    }

    @PostMapping("/event")
    public ResponseEntity<String> triggerEventPoints(@RequestBody PointEventDTO pointEventDTO) {
        return new ResponseEntity<>(pointService.giveDoublePointsOnEvent(pointEventDTO)+" 시작", HttpStatus.OK);
    }

    @GetMapping("/mypoint")
    public ResponseEntity<List<PointEventDTO>> getAllPointsOfTraveler(HttpServletRequest request) {
        return new ResponseEntity<>(pointService.getAllPointsOfTraveler(request), HttpStatus.OK);
    }

    @GetMapping("/get-charged")
    public ResponseEntity<List<PointEventDTO>> getChargedPointsOfTraveler(HttpServletRequest request) {
        return new ResponseEntity<>(pointService.getChargedPointsOfTraveler(request), HttpStatus.OK);
    }

    @GetMapping("/get-charged/all")
    public ResponseEntity<List<PointEventDTO>> getChargedPointsOfTravelers(HttpServletRequest request) {
        return new ResponseEntity<>(pointService.getChargedPointsOfTravelers(request), HttpStatus.OK);
    }

    @PostMapping("/convert")
    public ResponseEntity<PointConvertResponseDTO> convertPoints(@RequestBody PointConvertRequestDTO requestDTO) {
        try {
            PointConvertResponseDTO responseDTO = pointService.convertPointsToCoupon(requestDTO);
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new PointConvertResponseDTO(null, 0, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get-used")
    public ResponseEntity<List<PointEventDTO>> getUsedPointsOfTraveler(HttpServletRequest request) {
        return new ResponseEntity<>(pointService.getUsedPointsOfTraveler(request), HttpStatus.OK);
    }

    @GetMapping("/get-used/all")
    public ResponseEntity<List<PointEventDTO>> getUsedPointsOfTravelers(HttpServletRequest request) {
        return new ResponseEntity<>(pointService.getUsedPointsOfTravelers(request), HttpStatus.OK);
    }

    @PostMapping("/deduct")
    public ResponseEntity<String> deductPoints(HttpServletRequest request, @RequestBody PointDTO pointDTO) {
        String result = pointService.deductPoints(request, pointDTO.getPoints());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
