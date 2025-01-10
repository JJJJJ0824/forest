package com.dw.forest.controller;

import com.dw.forest.model.Point;
import com.dw.forest.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PointController {
    @Autowired
    PointService pointService;

    @GetMapping("/points/all")
    public List<Point> getAllPoints() {
        return pointService.getAllPoints();
    }

}
