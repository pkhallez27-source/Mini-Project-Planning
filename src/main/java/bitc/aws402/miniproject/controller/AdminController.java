package bitc.aws402.miniproject.controller;

import bitc.aws402.miniproject.dto.MemberDTO;
import bitc.aws402.miniproject.dto.QnaDTO;
import bitc.aws402.miniproject.service.AdminService;
import bitc.aws402.miniproject.service.MemberService;
import bitc.aws402.miniproject.service.QnaService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//  추형호 -----
@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
public class AdminController {
    private final AdminService adminService;
    private final MemberService memberService;
    private final QnaService qnaService;

    @GetMapping({"","/", "/index"})
    public String adminIndex() {
        // templates/admin/index.html 파일이 존재한다고 가정
        return "/admin/login";
    }

    @GetMapping("/login")
    public String adminLoginForm() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String adminLoginProcess(@RequestParam("memberId") String memberId,
                                    @RequestParam("memberPwd") String memberPwd,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        try {
            MemberDTO member = adminService.findMemberById(memberId);

            if (member != null && member.getMemberLevel() == 99 && memberPwd.equals(member.getMemberPwd())) {
                session.setAttribute("adminUser", member);
                return "redirect:/admin/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "아이디와 비번이 일치하지 않거나 관리자 권한이 없습니다.");
                return "redirect:/admin/login";
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "로그인 처리 중 오류가 발생했습니다.");
            return "redirect:/admin/login";
        }
    }

    @ResponseBody
    @PutMapping("/memberUpdate")
    public ResponseEntity<Integer> adminUpdateMember(@RequestBody MemberDTO member) {
        if(member == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        int updateResult = adminService.updateMember(member);
        return ResponseEntity.ok(updateResult);
    }

    @ResponseBody
    @PostMapping("/memberInsert")
    public ResponseEntity<String> adminInsertMember(@RequestBody MemberDTO member) {
        if(member == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        int checkId = memberService.checkIdExist(member.getMemberId());
        if(checkId > 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 추가 실패 (이미 존재하는 아이디입니다.)");
        }
        try {
            memberService.registerMember(member);
            return ResponseEntity.ok().body("회원 추가 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 추가 실패");
        }
    }

    @ResponseBody
    @GetMapping("/memberSelect")
    public ResponseEntity<String> adminSelectMember(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum) {
        PageInfo<MemberDTO> list = new PageInfo<>(adminService.selectMemberListPage(pageNum), 5);
        ResponseEntity resEntity = ResponseEntity.status(HttpStatus.OK).body(list);
        return resEntity;
    }

    //    mybatis 로 변경 필요
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            // AdminMapper에 정의된 메서드명과 일치시킴
//            List<MemberDTO> members = adminService.selectMemberList();
            List<Map<String, Object>> accommodations = adminService.selectAccommodationList();
            List<Map<String, Object>> reservationList = adminService.selectReservationList();
            List<QnaDTO> qnaList = qnaService.selectQnaList();

            // dashboard.html에서 사용하는 모델 변수명에 맞게 매핑
//            model.addAttribute("members", members);
            model.addAttribute("accommodations", accommodations);
            model.addAttribute("reservationList", reservationList);
            model.addAttribute("qnaList", qnaList);

        } catch (Exception e) {
            e.printStackTrace();
            // 오류 발생 시 뷰에서 에러가 나지 않도록 방어 코드 적용
//            model.addAttribute("members", new java.util.ArrayList<>());
            model.addAttribute("accommodations", new java.util.ArrayList<>());
            model.addAttribute("reservationList", new java.util.ArrayList<>());
            model.addAttribute("qnaList", new java.util.ArrayList<>());
        }

        return "admin/dashboard";
    }

    @GetMapping("/logout")
    public String adminLogout(HttpSession session) {
        session.removeAttribute("adminUser");
        session.invalidate(); // 세션 완전 무효화
        return "redirect:/admin/login";
    }

    // 1. 관리자 메인 페이지 (회원 목록 및 Q&A 목록 조회)
//    mybatis 로 변경 필요
    @GetMapping("")
    public String adminMain(Model model) {
        try {
            model.addAttribute("members", adminService.selectMemberList());
            model.addAttribute("qnaList", qnaService.selectQnaList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin/main";
    }

    // 2. Q&A 상세보기
//    mybatis 로 변경 필요
    @GetMapping("/qna/{id}")
    public String adminQnaDetail(@PathVariable("id") int id, Model model) {
        try {
            QnaDTO qna = qnaService.selectQnaDetail(id);
            model.addAttribute("qna", qna);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin/qnaDetail";
    }

    // 3. 관리자 답글 등록 처리 (수정 완료 후 관리자 대시보드로 이동)
//    mybatis 로 변경 필요
    @PostMapping("/qna/reply/{id}")
    public String adminQnaReply(@PathVariable("id") int id,
                                @RequestParam("reply") String reply,
                                HttpSession session) {
        try {
            MemberDTO adminUser = (MemberDTO) session.getAttribute("adminUser");
            if (adminUser == null) {
                return "redirect:/member/login";
            }

            QnaDTO qna = new QnaDTO();
            qna.setId(id);
            qna.setAnswer(reply);

            // String.valueOf()를 제거하고 숫자(int) 그대로 세팅
            qna.setWriter(adminUser.getMemberIdx());

            // 이미 해당 글에 등록된 답변이 있는지 개수 확인
            int replyCount = adminService.selectReplyCount(id);

            if (replyCount > 0) {
                // 이미 답변이 존재한다면 -> 수정(UPDATE)
                adminService.updateQnaAnswer(qna);
            } else {
                // 답변이 없다면 -> 새로 등록(INSERT)
                adminService.insertQnaAnswer(qna);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/admin/dashboard";
    }

    // 회원 / 숙소 목록 페이지 이동
    @GetMapping("/list")
    public String adminList(Model model) {
        try {
            model.addAttribute("members", adminService.selectMemberList());
            model.addAttribute("accommodations", adminService.selectAccommodationList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin/list";
    }
}