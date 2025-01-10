package com.dw.forest.service;

import com.dw.forest.repository.TravelerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TravelerService {
    @Autowired
    TravelerRepository travelerRepository;

}
