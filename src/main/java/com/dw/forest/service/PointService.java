package com.dw.forest.service;

import com.dw.forest.model.Point;
import com.dw.forest.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {
    @Autowired
    PointRepository pointRepository;

    public List<Point> getAllPoints() {
        return pointRepository.findAll();
    }
}
