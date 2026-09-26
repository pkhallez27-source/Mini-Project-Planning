package bitc.aws402.miniproject.service;

import bitc.aws402.miniproject.dto.MemberDTO;
import bitc.aws402.miniproject.dto.ReservationDTO;
import bitc.aws402.miniproject.dto.RoomDTO;
import bitc.aws402.miniproject.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationService {
  private final ReservationMapper reservationMapper;
  public List<RoomDTO> selectAvailableRoomList(String startDate, String endDate) {
    return reservationMapper.selectAvailableRoomList(startDate, endDate);
  }

  public RoomDTO selectRoomDetail(int roomIdx) {
    return reservationMapper.selectRoomDetail(roomIdx);
  }

  public List<String> selectRoomResourceList(int roomIdx) {
    return reservationMapper.selectRoomResourceList(roomIdx);
  }

  public MemberDTO selectMemberInfo(int memberIdx) {
    return reservationMapper.selectMemberInfo(memberIdx);
  }

  public MemberDTO selectMemberInfoById(String memberId) {
    return reservationMapper.selectMemberInfoById(memberId);
  }

  public String insertReservation(ReservationDTO reservation) {
    RoomDTO room = reservationMapper.selectRoomDetail(reservation.getRvRoomIdx());

//    DB 에 rv_pay 가 NOT NULL 이므로 서버에서 숙박 금액을 계산함
//    현재 DB 에 성수기/준성수기 기간 정보가 따로 없어 주중/주말 가격만 사용
    int totalPay = calculateRoomPrice(
        room,
        LocalDate.parse(reservation.getRvStartDate()),
        LocalDate.parse(reservation.getRvEndDate())
    );

    String rvCode = "RV" + java.time.LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

    reservation.setRvCode(rvCode);
    reservation.setRvPay(totalPay);
    reservation.setRvStatus(1);

    reservationMapper.insertReservation(reservation);

    return rvCode;
  }

  public ReservationDTO selectReservationComplete(String rvCode) {
    return reservationMapper.selectReservationComplete(rvCode);
  }

  private int calculateRoomPrice(RoomDTO room, LocalDate startDate, LocalDate endDate) {
    int totalPay = 0;
    LocalDate date = startDate;

    while (date.isBefore(endDate)) {
      DayOfWeek day = date.getDayOfWeek();

      if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
        totalPay += room.getRoomPriceWeekend();
      }
      else {
        totalPay += room.getRoomPriceWeekdays();
      }

      date = date.plusDays(1);
    }

    return totalPay;
  }

  public int calculateRoomPrice(
      RoomDTO room,
      String startDate,
      String endDate
  ) {

    LocalDate start =
        LocalDate.parse(startDate);

    LocalDate end =
        LocalDate.parse(endDate);

    int totalPrice = 0;

    LocalDate date = start;

    while (date.isBefore(end)) {

      DayOfWeek dayOfWeek =
          date.getDayOfWeek();

      if (
          dayOfWeek == DayOfWeek.SATURDAY ||
              dayOfWeek == DayOfWeek.SUNDAY
      ) {

        totalPrice +=
            room.getRoomPriceWeekend();

      }
      else {

        totalPrice +=
            room.getRoomPriceWeekdays();
      }

      date = date.plusDays(1);
    }

    return totalPrice;
  }
}
