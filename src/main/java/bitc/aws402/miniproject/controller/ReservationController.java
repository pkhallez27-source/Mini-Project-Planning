package bitc.aws402.miniproject.controller;

import bitc.aws402.miniproject.dto.MemberDTO;
import bitc.aws402.miniproject.dto.ReservationDTO;
import bitc.aws402.miniproject.dto.RoomDTO;
import bitc.aws402.miniproject.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ReservationController {

  private final ReservationService reservationService;

//  예약 가능한 객실 목록
//  최초 접속 시 오늘 ~ 내일을 기본 날짜로 사용
  @GetMapping("/reservation")
  public String reservation(
      @RequestParam(value = "startDate", required = false) String startDate,
      @RequestParam(value = "endDate", required = false) String endDate,
      Model model
  ) {

    LocalDate today = LocalDate.now();

    if (startDate == null || startDate.isBlank()) {
      startDate = today.toString();
    }

    if (endDate == null || endDate.isBlank()) {
      endDate = today.plusDays(1).toString();
    }

    try {
      LocalDate start = LocalDate.parse(startDate);
      LocalDate end = LocalDate.parse(endDate);

      if (start.isBefore(today) || !end.isAfter(start)) {
        model.addAttribute("message", "체크인/체크아웃 날짜를 다시 확인해주세요.");
        startDate = today.toString();
        endDate = today.plusDays(1).toString();
      }
    }
    catch (DateTimeParseException e) {
      model.addAttribute("message", "올바른 날짜 형식이 아닙니다.");
      startDate = today.toString();
      endDate = today.plusDays(1).toString();
    }

    List<RoomDTO> roomList = reservationService.selectAvailableRoomList(startDate, endDate);

    for (RoomDTO room : roomList) {
      int totalPrice =
              reservationService.calculateRoomPrice(
                      room,
                      startDate,
                      endDate
              );

      room.setTotalPrice(totalPrice);
    }

    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("today", today.toString());
    model.addAttribute("roomList", roomList);

    return "reservation/reservation";
  }

//  예약 상세 입력 화면
@GetMapping("/reservation/detail")
public String reservationDetail(
        @RequestParam("roomIdx") int roomIdx,
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate,
        HttpSession session,
        Model model
) {

  MemberDTO member = getLoginMember(session);

  if (member == null) {
    session.setAttribute(
            "loginRedirectUrl",
            "/reservation/detail?roomIdx=" + roomIdx
                    + "&startDate=" + startDate
                    + "&endDate=" + endDate
    );

    return "redirect:/member/login";
  }

  RoomDTO room =
          reservationService.selectRoomDetail(roomIdx);

  if (room == null) {
    return "redirect:/reservation";
  }

  // 예약 상세 화면에서만 tb_resource의 전체 이미지를 사용
  List<String> roomImageList =
          reservationService.selectRoomResourceList(roomIdx);

  int totalPrice =
          reservationService.calculateRoomPrice(
                  room,
                  startDate,
                  endDate
          );

  model.addAttribute("room", room);
  model.addAttribute("roomImageList", roomImageList);
  model.addAttribute("member", member);
  model.addAttribute("startDate", startDate);
  model.addAttribute("endDate", endDate);

  // 이게 꼭 있어야 함
  model.addAttribute("totalPrice", totalPrice);

  return "reservation/reservationDetail";
}
//  예약 등록 처리
  @PostMapping("/reservation")
  public String insertReservation(
      @ModelAttribute ReservationDTO reservation,
      HttpSession session
  ) {

    MemberDTO member = getLoginMember(session);

    if (member == null) {
      return "redirect:/member/login";
    }

    if (!isValidReservationDate(reservation.getRvStartDate(), reservation.getRvEndDate())) {
      return "redirect:/reservation";
    }

    reservation.setRvMemberIdx(member.getMemberIdx());

    String rvCode = reservationService.insertReservation(reservation);

    return "redirect:/reservation/complete?rvCode=" + rvCode;
  }

//  예약 완료 화면
  @GetMapping("/reservation/complete")
  public String reservationComplete(
      @RequestParam("rvCode") String rvCode,
      HttpSession session,
      Model model
  ) {

    MemberDTO member = getLoginMember(session);

    if (member == null) {
      return "redirect:/member/login";
    }

    ReservationDTO reservation = reservationService.selectReservationComplete(rvCode);

    if (reservation == null || reservation.getRvMemberIdx() != member.getMemberIdx()) {
      return "redirect:/reservation";
    }

    model.addAttribute("reservation", reservation);

    return "reservation/reservationComplete";
  }

  private MemberDTO getLoginMember(HttpSession session) {
    Object memberIdx = session.getAttribute("memberIdx");

    if (memberIdx != null) {
      try {
        return reservationService.selectMemberInfo(Integer.parseInt(memberIdx.toString()));
      }
      catch (NumberFormatException e) {
        return null;
      }
    }

    Object memberId = session.getAttribute("memberId");

    if (memberId != null) {
      return reservationService.selectMemberInfoById(memberId.toString());
    }

    return null;
  }

  private boolean isValidReservationDate(String startDate, String endDate) {
    try {
      LocalDate today = LocalDate.now();
      LocalDate start = LocalDate.parse(startDate);
      LocalDate end = LocalDate.parse(endDate);

      return !start.isBefore(today) && end.isAfter(start);
    }
    catch (Exception e) {
      return false;
    }
  }
}
