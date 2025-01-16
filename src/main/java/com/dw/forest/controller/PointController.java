package com.dw.forest.controller;

import com.dw.forest.model.Point;
import com.dw.forest.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointController {
    @Autowired
    PointService pointService;

    @GetMapping("/all")
    public ResponseEntity<List<Point>> getAllPoints() {
        return new ResponseEntity<>(pointService.getAllPoints(), HttpStatus.OK);
    }

    @GetMapping("/{traveler_name}/all")
    public ResponseEntity<List<Point>> getAllPointsOfTraveler(@PathVariable String traveler_name) {
        return new ResponseEntity<>(pointService.getAllPointsOfTraveler(traveler_name), HttpStatus.OK);
    }

    @GetMapping("/{traveler_name}/usage")
    public ResponseEntity<List<Point>> getUsedPointsOfTraveler(@PathVariable String traveler_name) {
        return new ResponseEntity<>(pointService.getUsedPointsOfTraveler(traveler_name), HttpStatus.OK);
    }
}
