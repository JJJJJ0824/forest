package com.dw.forest.service;

import com.dw.forest.dto.QaDTO;
import com.dw.forest.dto.QaReadDTO;
import com.dw.forest.exception.InvalidRequestException;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.model.A;
import com.dw.forest.model.Q;
import com.dw.forest.repository.ARepository;
import com.dw.forest.repository.QRepository;
import com.dw.forest.repository.TravelerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class QAService {
    @Autowired
    QRepository qRepository;

    @Autowired
    ARepository aRepository;

    @Autowired
    TravelerRepository travelerRepository;

    public List<QaDTO> getAllQas() {
        List<QaDTO> q = qRepository.findAll().stream().map(Q::toDTO).toList();
        if (q.isEmpty()) {
            throw new ResourceNotFoundException("Q&A가 없습니다.");
        }
        return q;
    }

    public QaDTO createQuestion(HttpServletRequest request, QaDTO qaDTO) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }

        String travelerName = (String) session.getAttribute("travelerName");

        Q q = new Q(null, travelerRepository.findById(travelerName).
                orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다.")),
                qaDTO.getTitle(), qaDTO.getContent(), LocalDate.now(), null);

        if (q.equals(new Q())) {
            throw new InvalidRequestException("잘못된 형식입니다.");
        }

        qRepository.save(q);

        return q.toDTO();
    }

    public QaDTO createAnswer(HttpServletRequest request, QaDTO qaDTO, Long qa_id) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {throw new InvalidRequestException("세션이 없습니다.");}

        String travelerName = (String) session.getAttribute("travelerName");
        Q q = qRepository.findById(qa_id).orElseThrow(()->new ResourceNotFoundException("작성된 질문에만 답할 수 있습니다."));

        A a = new A(null, travelerRepository.findById(travelerName).orElseThrow(()->new ResourceNotFoundException("계정명이 잘못되었습니다."))
                , qaDTO.getTitle(), qaDTO.getContent(), LocalDate.now(), q);

        aRepository.save(a);

        return a.toDTO();
    }

    public QaDTO getQA(Long q_id) {
        Q q = qRepository.findById(q_id).orElseThrow(()->new ResourceNotFoundException("해당 Q&A를 찾을 수 없습니다."));

        return q.toDTO();
    }

    public String deleteById(HttpServletRequest request, Long qa_id) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");
        if (qRepository.existsById(qa_id)) {
            try {
                if (!qRepository.findByTraveler_TravelerName(travelerName).equals(Collections.emptyList())) {
                    throw new ResourceNotFoundException("자신의 글이 아닌 게시글은 삭제할 수 없습니다.");
                }
                qRepository.deleteById(qa_id);
            }catch (ResourceNotFoundException e) {
                throw new ResourceNotFoundException(e.getMessage());
            }
            return "해당 글을 삭제하였습니다";
        }
        throw new ResourceNotFoundException("해당 번호의 글이 없습니다.");
    }

    public QaDTO updateById(Long q_id, QaDTO qaDTO, HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션이 없으면 예외처리
        if (session == null) {
            throw new InvalidRequestException("세션이 없습니다.");
        }
        String travelerName = (String) session.getAttribute("travelerName");

        Q q = qRepository.findById(q_id).
                orElseThrow(()-> new ResourceNotFoundException("해당 번호의 글이 없습니다."));

        if (!travelerName.equals(q.getTraveler().getTravelerName())) {
            throw new InvalidRequestException("본인의 글이 아닌 글은 수정할 수 없습니다.");
        }

        if ((qaDTO.getTitle().isEmpty() && qaDTO.getContent().isEmpty()) ||
                (qaDTO.getQaReadDTO().getTitle().isEmpty() && qaDTO.getQaReadDTO().getContent().isEmpty())) {
            throw new ResourceNotFoundException("변경할 제목 또는 내용을 입력해주세요.");
        }

        if (!qaDTO.getTitle().isEmpty()) {
            q.setTitle(q.getTitle());}
        // qaReadDTO는 q를 가지며 qaDTO에 있는 메인 객체가 a이다.
        if (!qaDTO.getContent().isEmpty()) {
            q.setContent(qaDTO.getContent());}

        if (!qaDTO.getQaReadDTO().getTitle().isEmpty()) {
            q.getA().setTitle(qaDTO.getQaReadDTO().getTitle());
        }

        if (!qaDTO.getQaReadDTO().getContent().isEmpty()) {
            q.getA().setContent(qaDTO.getQaReadDTO().getContent());
        }

        qRepository.save(q);

        return q.toDTO();
    }

    public List<QaDTO> searchByQuestionTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new ResourceNotFoundException("검색어는 빈 값일 수 없습니다.");
        }

        String abTitle = "%" + title + "%";

        try {
            List<QaDTO> qaDTOList = qRepository.findByTitleLike(abTitle).
                    stream().map(Q::toDTO).toList();

            if (qaDTOList.isEmpty()) {
                throw new ResourceNotFoundException("해당 제목을 가진 게시글이 없습니다.");
            }

            return qaDTOList;
        } catch (Exception e) {
            throw new ResourceNotFoundException("게시글 검색 중 오류가 발생했습니다.");
        }
    }

    public List<QaDTO> searchByAnswerTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new ResourceNotFoundException("검색어는 빈 값일 수 없습니다.");
        }

        String abTitle = "%" + title + "%";

        try {
            List<A> as = aRepository.findByTitleLike(abTitle);

            if (as.equals(new ArrayList<>())) {
                throw new ResourceNotFoundException("해당 제목을 가진 게시글이 없습니다.");
            }

            return as.stream().map(A::toDTO).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException("게시글 검색 중 오류가 발생했습니다.");
        }
    }

    public List<QaDTO> searchByQuestionContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new ResourceNotFoundException("해당 내용을 찾을 수 없습니다.");
        }

        String abContent = "%" + content + "%";

        try {
            List<Q> qs = qRepository.findByContentLike(abContent);

            if (qs.equals(new ArrayList<>())){
                throw new ResourceNotFoundException("해당 내용을 가진 게시글이 없습니다.");
            }

            return qs.stream().map(Q::toDTO).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException("내용 검색 중 오류가 발생했습니다.");
        }
    }

    public List<QaDTO> searchByAnswerContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new ResourceNotFoundException("해당 내용을 찾을 수 없습니다.");
        }

        String abContent = "%" + content + "%";

        try {
            List<A> qs = aRepository.findByContentLike(abContent);

            if (qs.equals(new ArrayList<>())){
                throw new ResourceNotFoundException("해당 내용을 가진 게시글이 없습니다.");
            }

            return qs.stream().map(A::toDTO).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException("내용 검색 중 오류가 발생했습니다.");
        }
    }

    public List<QaDTO> searchByQuestionTitleAndContent(String title, String content) {
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

            List<Q> qs = qRepository.findByTitleOrContentLike(asTitle, asContent);

            if (qs.isEmpty()) {
                throw new ResourceNotFoundException("검색어로 게시글이 확인되지 않습니다. 올바른 검색어를 입력하세요.");
            }

            return qs.stream().map(Q::toDTO).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public List<QaDTO> searchByAnswerTitleAndContent(String title, String content) {
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

            List<A> as = aRepository.findByTitleOrContentLike(asTitle, asContent);

            if (as.isEmpty()) {
                throw new ResourceNotFoundException("검색어로 게시글이 확인되지 않습니다. 올바른 검색어를 입력하세요.");
            }

            return as.stream().map(A::toDTO).toList();
        } catch (Exception e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
