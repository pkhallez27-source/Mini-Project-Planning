package bitc.aws402.miniproject.controller;

import bitc.aws402.miniproject.dto.MemberDTO;
import bitc.aws402.miniproject.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//  추형호 -----
@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm(){
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signupProcess(MemberDTO member, RedirectAttributes redirectAttributes, Model model){
        int checkId = memberService.checkIdExist(member.getMemberId());
        if(checkId > 0){
            return back("회원가입 실패 (이미 존재하는 아이디입니다.)", model);
        }
        try {
            memberService.registerMember(member);
            redirectAttributes.addFlashAttribute("msg", "회원가입에 성공하였습니다.");
            return "redirect:/member/login";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "회원가입 실패 (이미 존재하는 아이디일 수 있습니다)");
            return "redirect:/member/signup";
        }
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "redirectURL", required = false) String redirectURL, Model model) {
        model.addAttribute("redirectURL", redirectURL);
        return "member/login";
    }

    @PostMapping("/login")
    public String loginProcess(@RequestParam("memberId") String memberId,
                               @RequestParam("memberPwd") String memberPwd,
                               @RequestParam(value = "redirectURL", required = false) String redirectURL,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) throws Exception {
        MemberDTO member = memberService.findMemberById(memberId);

        String redirectParam = (redirectURL != null && !redirectURL.isEmpty()) ? "?redirectURL=" + redirectURL : "";

        if (member == null) {
            redirectAttributes.addFlashAttribute("error", "등록되지 않은 아이디입니다.");
            return "redirect:/member/login" + redirectParam;
        }

        if (!member.getMemberPwd().equals(memberPwd)) {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/member/login" + redirectParam;
        }

        session.setAttribute("loginUser", member);
        session.setAttribute("memberIdx", member.getMemberIdx());
        session.setAttribute("memberId", member.getMemberId());
        session.setAttribute("memberName", member.getMemberName());
        session.setAttribute("memberPhone", member.getMemberPhone());
        session.setAttribute("memberGender", member.getMemberGender());
        session.setAttribute("memberEmail", member.getMemberEmail());
        session.setAttribute("memberJoinDate", member.getMemberJoinDate());
        session.setAttribute("memberLevel", member.getMemberLevel());
        session.setAttribute("memberStatus", member.getMemberStatus());
        session.setMaxInactiveInterval(60 * 30);  //세션 유지 시간 30분        redirectAttributes.addFlashAttribute("msg", "로그인 성공하였습니다");

        // 예약페이지에서 로그인 페이지로 넘어온 경우 -- 구정민
        String loginRedirectUrl =
                (String) session.getAttribute("loginRedirectUrl");


        if (loginRedirectUrl != null) {

            session.removeAttribute("loginRedirectUrl");

            return "redirect:" + loginRedirectUrl;
        }


        if (redirectURL != null && !redirectURL.isEmpty()) {
            return "redirect:" + redirectURL;
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, HttpServletRequest request) {
        session.invalidate();
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("error", "로그인 후 이용해주세요.");
            return "redirect:/member/login?redirectURL=/member/mypage";
        }
        MemberDTO userInfo = memberService.getUserInfo(loginUser.getMemberId());
        model.addAttribute("user", userInfo);
        return "member/mypage";
    }

    @PostMapping("/update")
    public String updateUserInfo(MemberDTO user, HttpSession session, RedirectAttributes redirectAttributes) throws Exception {
        MemberDTO loginUser = (MemberDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/member/login";
        }
        user.setMemberId(loginUser.getMemberId());
        memberService.updateUserInfo(user);
        MemberDTO updatedUser = memberService.findMemberById(loginUser.getMemberId());
        session.setAttribute("loginUser", updatedUser);
        redirectAttributes.addFlashAttribute("msg", "회원 정보가 수정되었습니다.");
        return "redirect:/";
    }

    private String back(String msg, Model model){
        model.addAttribute("msg", msg);
        System.out.println(msg);
        return "common/back";
    }

}