package com.dw.forest.controller;

import com.dw.forest.dto.TravelerDTO;
import com.dw.forest.model.Traveler;
import com.dw.forest.service.TravelerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TravelerController {
    @Autowired
    TravelerService travelerService;

//    @PostMapping("/register")
//    public ResponseEntity<TravelerDTO> register(@RequestBody TravelerDTO travelerDTO) {
//        return new ResponseEntity<>(
//                travelerService.register(travelerDTO),
//                HttpStatus.CREATED);
//    }

    @GetMapping("/traveler/all")
    public ResponseEntity<List<Traveler>> getAllTraveler() {
        return new ResponseEntity<>(travelerService.getAllTravelers(), HttpStatus.OK);
    }
}
