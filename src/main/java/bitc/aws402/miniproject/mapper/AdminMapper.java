package bitc.aws402.miniproject.mapper;

import bitc.aws402.miniproject.dto.MemberDTO;
import bitc.aws402.miniproject.dto.QnaDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminMapper {

  // 관리자 로그인용 회원 단건 조회
  MemberDTO selectMemberById(@Param("memberId") String memberId) throws Exception;

  // 회원 목록 조회
  List<MemberDTO> selectMemberList();

  int updateMember(MemberDTO member);
  // 숙소 목록 조회
  List<Map<String, Object>> selectAccommodationList() throws Exception;

  // 전체 예약 현황 조회
  List<Map<String, Object>> selectReservationList() throws Exception;

  // Q&A 문의 목록 조회
  List<Map<String, Object>> selectQnaList() throws Exception;

  // 관리자 Q&A 답변 등록
  int selectReplyCount(int id);
  void insertQnaAnswer(QnaDTO qna);
  void updateQnaAnswer(QnaDTO qna);
}