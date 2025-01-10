package com.dw.forest.service;

import com.dw.forest.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    @Autowired
    PointRepository pointRepository;

}
