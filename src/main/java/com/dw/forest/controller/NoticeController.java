package com.dw.forest.controller;

import com.dw.forest.dto.NoticeDTO;
import com.dw.forest.model.Notice;
import com.dw.forest.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    // 모든 공지사항 조회
    @GetMapping("/all")
    public ResponseEntity<List<NoticeDTO>> getAllNotices() {
        return new ResponseEntity<>(noticeService.getAllNotice(), HttpStatus.OK);
    }

    // 공지사항 생성
    @PostMapping("/create")
    public ResponseEntity<NoticeDTO> createNotice(
            @RequestBody NoticeDTO noticeDTO, HttpServletRequest request) {

        NoticeDTO createdNotice = noticeService.createNotice(noticeDTO.getTitle(), noticeDTO.getContent(), request);
        return new ResponseEntity<>(createdNotice, HttpStatus.CREATED);
    }

    // 특정 공지사항 조회
    @GetMapping("/{notice_id}")
    public ResponseEntity<NoticeDTO> getNoticeById(@PathVariable("notice_id") Long notice_id) {
        NoticeDTO notice = noticeService.getNoticeById(notice_id);
        return new ResponseEntity<>(notice, HttpStatus.OK);
    }

    // 제목으로 공지사항 검색
    @GetMapping("/search-by-title")
    public ResponseEntity<List<NoticeDTO>> searchNoticesByTitle(@RequestParam String title) {
        List<NoticeDTO> notices = noticeService.searchNoticesByTitle(title);
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    // 내용으로 공지사항 검색
    @GetMapping("/search-by-content")
    public ResponseEntity<List<NoticeDTO>> searchNoticesByContent(@RequestParam String content) {
        List<NoticeDTO> notices = noticeService.searchNoticesByContent(content);
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    // 특정 날짜의 공지사항 검색
    @GetMapping("/search-by-date")
    public ResponseEntity<List<NoticeDTO>> searchNoticesByDate(@RequestParam String date) {
        List<NoticeDTO> notices = noticeService.searchNoticesByDate(date);
        return new ResponseEntity<>(notices, HttpStatus.OK);
    }

    // 공지사항 수정
    @PutMapping("/{notice_id}")
    public ResponseEntity<NoticeDTO> updateNotice(@PathVariable Long notice_id, @RequestBody NoticeDTO noticeDTO) {
        NoticeDTO updatedNotice = noticeService.updateNotice(notice_id, noticeDTO);
        return new ResponseEntity<>(updatedNotice, HttpStatus.OK);
    }

    // 공지사항 제목 수정
    @PutMapping("/update-title")
    public ResponseEntity<NoticeDTO> updateTitle(@RequestParam Long id, @RequestBody NoticeDTO noticeDTO) {
        NoticeDTO updatedTitle = noticeService.updateTitle(id, noticeDTO);
        return new ResponseEntity<>(updatedTitle, HttpStatus.OK);
    }

    // 공지사항 내용 수정
    @PutMapping("/update-content")
    public ResponseEntity<NoticeDTO> updateContent(@RequestParam Long id, @RequestBody NoticeDTO noticeDTO) {
        NoticeDTO updatedContent = noticeService.updateContent(id, noticeDTO);
        return new ResponseEntity<>(updatedContent, HttpStatus.OK);
    }

    // 공지사항 삭제 (ID 기준)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNotice(@PathVariable Long id) {
        return new ResponseEntity<>(noticeService.deleteNotice(id), HttpStatus.OK);
    }

    // 공지사항 삭제 (제목 기준)
    @DeleteMapping("/delete-by-title")
    public ResponseEntity<String> deleteNoticeByTitle(@RequestParam String title) {
        String message = noticeService.deleteNoticeByTitle(title);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}