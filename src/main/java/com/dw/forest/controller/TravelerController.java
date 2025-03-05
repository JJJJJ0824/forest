package com.dw.forest.controller;

import com.dw.forest.dto.ChangePassDTO;
import com.dw.forest.dto.FindID_PWD;
import com.dw.forest.dto.TravelerDTO;
import com.dw.forest.dto.TravelerResponseDTO;
import com.dw.forest.exception.ResourceNotFoundException;
import com.dw.forest.exception.UnauthorizedTravelerException;
import com.dw.forest.service.TravelerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traveler")
public class TravelerController {
    @Autowired
    TravelerService travelerService;

    @PostMapping("/register")
    public ResponseEntity<TravelerDTO> registerTraveler(@RequestBody TravelerResponseDTO travelerResponseDTO) {
        return new ResponseEntity<>(
                travelerService.registerTraveler(travelerResponseDTO),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody TravelerDTO travelerDTO, HttpServletRequest request) {
        String travelerName = travelerDTO.getTravelerName();
        String password = travelerDTO.getPassword();
        if (travelerService.validateUser(travelerName, password)) {
            HttpSession session = request.getSession();
            session.setAttribute("travelerName", travelerName);
            return new ResponseEntity<>("로그인 성공!", HttpStatus.OK);
        } else {
            throw new UnauthorizedTravelerException("계정명 또는 비밀번호가 잘못되었습니다.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate(); // 세션 종료
        if (request==null&&response==null) {
            throw new ResourceNotFoundException("로그아웃 실패 !");
        }
        return new ResponseEntity<>("로그아웃 성공 !", HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TravelerDTO>> getAllTravelers(HttpServletRequest request) {
        return new ResponseEntity<>(travelerService.getAllTravelers(request), HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public ResponseEntity<TravelerResponseDTO> mypage(HttpServletRequest request) {
        return new ResponseEntity<>(travelerService.getCurrentTraveler(request), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<TravelerResponseDTO> updateTraveler(HttpServletRequest request, @RequestBody TravelerResponseDTO travelerResponseDTO) {
        return new ResponseEntity<>(travelerService.updateTraveler(request, travelerResponseDTO),HttpStatus.OK);
    }

    @PutMapping("/password")
    public ResponseEntity<String> changePassword(HttpServletRequest request, @RequestBody ChangePassDTO passDTO) {
        return new ResponseEntity<>(travelerService.changePassword
                (request, passDTO.getOldPassword(), passDTO.getNewPassword(), passDTO.getNewPasswordCheck())+
                "가 정상적으로 변경되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/find-id")
    public ResponseEntity<String> findId(@RequestBody FindID_PWD findIDPwd) {
        return new ResponseEntity<>(travelerService.findId(findIDPwd), HttpStatus.OK);
    }

    @GetMapping("/find-pwd")
    public ResponseEntity<String> findPwd(@RequestBody FindID_PWD findIDPwd) {
        return new ResponseEntity<>(travelerService.findPwd(findIDPwd), HttpStatus.OK);
    }

    @DeleteMapping("/{traveler_name}/delete")
    public ResponseEntity<String> deleteTraveler(HttpServletRequest request, @PathVariable String traveler_name) {
        return new ResponseEntity<>("여행자 삭제에 "+travelerService.deleteTraveler(request, traveler_name), HttpStatus.OK);
    }
}
