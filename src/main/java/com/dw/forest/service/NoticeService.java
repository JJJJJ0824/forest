package com.dw.forest.service;

import com.dw.forest.model.Notice;
import com.dw.forest.repository.NoticeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    @Autowired
    NoticeRepository noticeRepository;

    public List<Notice> getAllNotice() {
        return noticeRepository.findAll();
    }
}
