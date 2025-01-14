package com.dw.forest.controller;

import com.dw.forest.model.Notice;
import com.dw.forest.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    @GetMapping("/all")
    public ResponseEntity<List<Notice>> getAllNotice() {
        return new ResponseEntity<>(noticeService.getAllNotice(), HttpStatus.OK);
    }
}
