package com.dw.forest.controller;

import com.dw.forest.dto.PointConvertRequestDTO;
import com.dw.forest.dto.PointConvertResponseDTO;
import com.dw.forest.dto.PointDTO;
import com.dw.forest.dto.PointEventDTO;
import com.dw.forest.model.Point;
import com.dw.forest.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointController {
    @Autowired
    PointService pointService;

    @GetMapping("/all")
    public ResponseEntity<List<PointEventDTO>> getAllPoints() {
        return new ResponseEntity<>(pointService.getAllPoints(), HttpStatus.OK);
    }

    @PostMapping("/{travelerName}/add")
    public Point addPoints(@PathVariable String travelerName, @RequestBody PointDTO pointDTO) {
        return pointService.addPointsToTraveler(travelerName,
                pointDTO.getPoints(), pointDTO.getActionType());
    }

    @PostMapping("/{travelerName}/use")
    public Point usePoints(@PathVariable String travelerName, @RequestBody PointDTO pointDTO) {
        return pointService.usePointsFromTraveler(travelerName, pointDTO.getPoints(), pointDTO.getActionType());
    }

    @PostMapping("/event")
    public ResponseEntity<String> triggerEventPoints(@RequestBody PointEventDTO pointEventDTO) {
        pointService.giveDoublePointsOnEvent(pointEventDTO);
        return ResponseEntity.ok("포인트 이벤트가 성공적으로 적용되었습니다.");
    }

    @GetMapping("/{traveler_name}/all")
    public ResponseEntity<List<PointEventDTO>> getAllPointsOfTraveler(@PathVariable String traveler_name) {
        return new ResponseEntity<>(pointService.getAllPointsOfTraveler(traveler_name), HttpStatus.OK);
    }

    @GetMapping("/{travelerName}/get-charged-all")
    public ResponseEntity<List<PointEventDTO>> getChargedPointsOfTraveler(@PathVariable String travelerName) {
        return new ResponseEntity<>(pointService.getChargedPointsOfTraveler(travelerName), HttpStatus.OK);
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

    @GetMapping("/{travelerName}/get-used-all")
    public ResponseEntity<List<PointEventDTO>> getUsedPointsOfTraveler(@PathVariable String travelerName) {
        return new ResponseEntity<>(pointService.getUsedPointsOfTraveler(travelerName), HttpStatus.OK);
    }
}
