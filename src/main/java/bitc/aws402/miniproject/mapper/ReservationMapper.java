package bitc.aws402.miniproject.mapper;

import bitc.aws402.miniproject.dto.MemberDTO;
import bitc.aws402.miniproject.dto.ReservationDTO;
import bitc.aws402.miniproject.dto.RoomDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReservationMapper {

//  선택한 기간에 예약 가능한 객실 목록
  List<RoomDTO> selectAvailableRoomList(
      @Param("startDate") String startDate,
      @Param("endDate") String endDate
  );

//  객실 상세 정보
  RoomDTO selectRoomDetail(@Param("roomIdx") int roomIdx);

  // 예약 상세 화면용 객실 전체 이미지
  List<String> selectRoomResourceList(@Param("roomIdx") int roomIdx);

//  회원 번호로 회원 정보 조회
  MemberDTO selectMemberInfo(@Param("memberIdx") int memberIdx);

//  로그인 팀원이 memberId 만 세션에 저장할 경우 사용 가능
  MemberDTO selectMemberInfoById(@Param("memberId") String memberId);

//  예약 등록
  void insertReservation(ReservationDTO reservation);

//  예약 완료 페이지 출력용
  ReservationDTO selectReservationComplete(@Param("rvCode") String rvCode);
}
