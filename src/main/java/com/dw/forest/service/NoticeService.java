package com.dw.forest.service;

import com.dw.forest.dto.NoticeDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.exception.UnauthorizedTravelerException;
import com.dw.forest.model.Notice;
import com.dw.forest.model.Traveler;
import com.dw.forest.repository.NoticeRepository;
import com.dw.forest.repository.TravelerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

@Service
public class NoticeService {
    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    TravelerRepository travelerRepository;

    public List<NoticeDTO> getAllNotice() {
        List<Notice> notices = noticeRepository.findAll();

        if (notices.isEmpty()) {
            throw new ResourceNotFoundException("공지사항이 없습니다.");
        }

        return notices.stream().map(Notice::toDTO).toList();
    }

    // 공지사항 생성
    public NoticeDTO createNotice(NoticeDTO noticeDTO) {
        if (!noticeDTO.getTravelerName().equals("admin")) {
            throw new InvalidRequestException("공지사항은 관리자만 작성 가능합니다.");
        }

        Traveler traveler = travelerRepository.findById(noticeDTO.getTravelerName()).
                orElseThrow(()->new UnauthorizedTravelerException("관리자명을 찾을 수 없습니다"));

        Notice notice = new Notice();
        notice.setTraveler(traveler);
        notice.setTitle(noticeDTO.getTitle());
        notice.setContent(noticeDTO.getContent());
        notice.setCreatedAt(LocalDate.now());

        noticeRepository.save(notice);

        return notice.toDTO();
    }

    // 특정 공지사항 조회
    public NoticeDTO getNoticeById(Long id) {
        return noticeRepository.findById(id).map(Notice::toDTO)
                .orElseThrow(() -> new RuntimeException("공지사항을 찾을 수 없습니다."));
    }

    // 제목으로 공지사항 검색
    public List<NoticeDTO> searchNoticesByTitle(String title) {
        return noticeRepository.findAll().stream()
                .filter(notice -> notice.getTitle().contains(title))
                .map(Notice::toDTO)
                .toList();
    }

    // 내용으로 공지사항 검색
    public List<NoticeDTO> searchNoticesByContent(String content) {
        return noticeRepository.findAll().stream()
                .filter(notice -> notice.getContent().contains(content))
                .map(Notice::toDTO)
                .toList();
    }

    // 특정 날짜의 공지사항 검색
    public List<NoticeDTO> searchNoticesByDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            return noticeRepository.findAll().stream()
                    .filter(notice -> notice.getCreatedAt().equals(localDate))
                    .map(Notice::toDTO)
                    .toList();
        }catch (DateTimeException e) {
            throw new InvalidRequestException("유효한 날짜 형태가 아닙니다. yyyy-mm-dd 형식으로 입력해주세요.");
        }
    }

    // 공지사항 수정
    public NoticeDTO updateNotice(Long id, NoticeDTO noticeDTO) {
        if (!noticeDTO.getTravelerName().equals("admin")) {
            throw new InvalidRequestException("공지사항은 관리자만 작성 가능합니다.");
        }

        Traveler traveler = travelerRepository.findById(noticeDTO.getTravelerName()).
                orElseThrow(()->new UnauthorizedTravelerException("관리자명을 찾을 수 없습니다"));

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("공지사항을 찾을 수 없습니다."));
        notice.setTitle(noticeDTO.getTitle());
        notice.setContent(noticeDTO.getContent());
        notice.setTraveler(traveler);
        notice.setCreatedAt(LocalDate.now());
        noticeRepository.save(notice);
        return notice.toDTO();
    }

    // 공지사항 제목 수정
    public NoticeDTO updateTitle(Long id, NoticeDTO noticeDTO) {
        if (!noticeDTO.getTravelerName().equals("admin")) {
            throw new InvalidRequestException("공지사항은 관리자만 작성 가능합니다.");
        }
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("공지사항을 찾을 수 없습니다."));

        if (noticeDTO.getTitle().isEmpty()) {
            throw new InvalidRequestException("제목이 비었습니다.");
        }

        Traveler traveler = travelerRepository.findById(noticeDTO.getTravelerName()).
                orElseThrow(()->new UnauthorizedTravelerException("관리자명을 찾을 수 없습니다"));

        notice.setTitle(noticeDTO.getTitle());
        notice.setTraveler(traveler);
        noticeRepository.save(notice);
        return notice.toDTO();
    }

    // 공지사항 내용 수정
    public NoticeDTO updateContent(Long id, NoticeDTO noticeDTO) {
        if (!noticeDTO.getTravelerName().equals("admin")) {
            throw new InvalidRequestException("공지사항은 관리자만 작성 가능합니다.");
        }

        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("공지사항을 찾을 수 없습니다."));

        if (noticeDTO.getContent().isEmpty()) {
            throw new InvalidRequestException("내용이 비었습니다.");
        }

        Traveler traveler = travelerRepository.findById(noticeDTO.getTravelerName()).
                orElseThrow(()->new UnauthorizedTravelerException("관리자명을 찾을 수 없습니다"));

        notice.setContent(noticeDTO.getContent());
        notice.setTraveler(traveler);
        noticeRepository.save(notice);
        return notice.toDTO();
    }

    // 공지사항 삭제 (ID 기준)
    public String deleteNotice(Long id) {
        if (!noticeRepository.existsById(id)) {
            throw new ResourceNotFoundException("공지사항을 찾을 수 없습니다.");
        }
        noticeRepository.deleteById(id);

        return "공지사항이 삭제되었습니다";
    }

    // 공지사항 삭제 (제목 기준)
    public String deleteNoticeByTitle(String title) {
        Notice notice = noticeRepository.findByTitleLike2(title);
        if (notice==null) {
            throw new ResourceNotFoundException("해당 제목의 공지사항이 없습니다.");
        }
        noticeRepository.delete(notice);
        return "공지사항이 삭제되었습니다";
    }
}
