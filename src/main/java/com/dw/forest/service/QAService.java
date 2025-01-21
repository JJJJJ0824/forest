package com.dw.forest.service;

import com.dw.forest.dto.QaDTO;
import com.dw.forest.dto.QaReadDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.QA;
import com.dw.forest.repository.QARepository;
import com.dw.forest.repository.TravelerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class QAService {
    @Autowired
    QARepository qaRepository;

    @Autowired
    TravelerRepository travelerRepository;

    public List<QaReadDTO> getAllQas() {
        List<QaReadDTO> q = qaRepository.findAll().stream().map(QA::toRead).toList();
        if (q.isEmpty()) {
            throw new ResourceNotFoundException("Q&A가 없습니다.");
        }
        return q;
    }

    public QaReadDTO createQuestion(QaDTO qaDTO) {
        QA qa = new QA(qaDTO.getId(), travelerRepository.findById(qaDTO.getTraveler_name()).
                orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다.")),
                qaDTO.getTitle(), qaDTO.getContent(), LocalDate.now(), "q");

        if (qa==null) {
            throw new InvalidRequestException("잘못된 형식입니다.");
        }

        qaRepository.save(qa);

        return qa.toRead();
    }

    public QaReadDTO createAnswer(QaDTO qaDTO) {
        QA qa = new QA(qaDTO.getId(), travelerRepository.findById(qaDTO.getTraveler_name()).
                orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다.")),
                qaDTO.getTitle(), qaDTO.getContent(), LocalDate.now(), "a");

        if (qa==null) {throw new InvalidRequestException("잘못된 형식입니다.");}

        qaRepository.save(qa);

        return qa.toRead();
    }

    public QaReadDTO getQA(Long qa_id) {
        QA qa = qaRepository.findById(qa_id).orElseThrow(()->new ResourceNotFoundException("해당 Q&A를 찾을 수 없습니다."));

        return qa.toRead();
    }

    public String deleteById(Long qa_id) {
        if (qaRepository.existsById(qa_id)) {
            try {
                qaRepository.deleteById(qa_id);
            }catch (ResourceNotFoundException e) {
                throw new ResourceNotFoundException(e.getMessage());
            }
            return "해당 글을 삭제하였습니다";
        }
        throw new ResourceNotFoundException("해당 번호의 글이 없습니다.");
    }

    public QaReadDTO updateById(Long qa_id, QaDTO qaDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        
        QA qa = qaRepository.findById(qa_id).
                orElseThrow(()-> new ResourceNotFoundException("해당 번호의 글이 없습니다."));

        if (!travelerName.equals(qa.getTraveler().getTravelerName())) {
            throw new InvalidRequestException("본인의 글이 아닌 글은 수정할 수 없습니다.");
        }

        if (qaDTO.getTitle()==null&&qaDTO.getContent()==null) {
            throw new ResourceNotFoundException("변경할 제목 또는 내용을 입력해주세요.");
        }

        if (qaDTO.getTitle()!=null) {qa.setTitle(qa.getTitle());}

        if (qaDTO.getContent()!=null) {qa.setContent(qaDTO.getContent());}

        qaRepository.save(qa);

        return qa.toRead();
    }

    public List<QaReadDTO> searchByTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new ResourceNotFoundException("검색어는 빈 값일 수 없습니다.");
        }

        String abTitle = "%" + title + "%";

        try {
            List<QaReadDTO> qaReadList = qaRepository.findByTitleLike(abTitle).
                    stream().map(QA::toRead).toList();

            if (qaReadList.isEmpty()) {
                throw new ResourceNotFoundException("해당 제목을 가진 게시글이 없습니다.");
            }

            return qaReadList;

        } catch (Exception e) {
            throw new ResourceNotFoundException("게시글 검색 중 오류가 발생했습니다.");
        }
    }

    public List<QaReadDTO> searchByContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new ResourceNotFoundException("해당 내용을 찾을 수 없습니다.");
        }

        String abContent = "%" + content + "%";

        try {
            List<QA> qas = qaRepository.findByContentLike(abContent);

            if (qas.isEmpty()){
                throw new ResourceNotFoundException("해당 내용을 가진 게시글이 없습니다.");
            }

            return qas.stream().map(QA::toRead).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException("내용 검색 중 오류가 발생했습니다.");
        }
    }

    public List<QaReadDTO> searchByTitleAndContent(String title, String content) {
        try {
            if (title.isEmpty() && content.isEmpty()) {
                throw new ResourceNotFoundException("검색어가 모두 빈 값일 수 없습니다.");
            }

            String asTitle = "%" + title + "%";
            String asContent = "%" + content + "%";

            if (title.isEmpty()) {
                asTitle = asContent;
            }

            if (content.isEmpty()) {
                asContent = asTitle;
            }

            List<QA> qas = qaRepository.findByTitleOrContentLike(asTitle, asContent);

            if (qas.isEmpty()) {
                throw new ResourceNotFoundException("검색어로 게시글이 확인되지 않습니다. 올바른 검색어를 입력하세요.");
            }

            return qas.stream().map(QA::toRead).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
