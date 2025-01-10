package com.dw.forest.controller;

import com.dw.forest.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PointController {
    @Autowired
    PointService pointService;
}
