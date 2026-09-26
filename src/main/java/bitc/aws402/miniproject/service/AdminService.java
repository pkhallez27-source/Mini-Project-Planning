package bitc.aws402.miniproject.service;

import bitc.aws402.miniproject.dto.MemberDTO;
import bitc.aws402.miniproject.dto.QnaDTO;
import bitc.aws402.miniproject.mapper.AdminMapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final AdminMapper adminMapper;

  // 관리자 로그인용 회원 단건 조회
  public MemberDTO findMemberById(String memberId) throws Exception {
    return adminMapper.selectMemberById(memberId);
  }

  // 회원 목록 조회
  public List<MemberDTO> selectMemberList() throws Exception {
    return adminMapper.selectMemberList();
  }

  public List<MemberDTO> selectMemberListPage(int pageNum){
    PageHelper.startPage(pageNum,10);
    return adminMapper.selectMemberList();
  }

  // 회원 정보 수정 -- lhs
  public int updateMember(MemberDTO member){
    return adminMapper.updateMember(member);
  }

  // 숙소 목록 조회
  public List<Map<String, Object>> selectAccommodationList() throws Exception {
    return adminMapper.selectAccommodationList();
  }

  // 전체 예약 현황 조회
  public List<Map<String, Object>> selectReservationList() throws Exception {
    return adminMapper.selectReservationList();
  }

  // Q&A 문의 목록 조회
  public List<Map<String, Object>> selectQnaList() throws Exception {
    return adminMapper.selectQnaList();
  }

  // Q&A 상세 조회
  public Map<String, Object> getQnaDetail(Long id) throws Exception {
    return null;
  }

  // Q&A 답글 등록 (기존 선언부 유지 및 saveQnaAnswer와 연동)
  public void registerQnaReply(Long id, String reply) throws Exception {
  }

  // 기존 단건 insert 메서드 유지
  public void insertQnaAnswer(QnaDTO qna) throws Exception {
    adminMapper.insertQnaAnswer(qna);
  }

  // 답변 존재 여부에 따라 INSERT 또는 UPDATE를 수행하는 통합 메서드

  // 답변 존재 여부를 확인하는 메서드 추가
  public int selectReplyCount(int id) throws Exception {
    return adminMapper.selectReplyCount(id);
  }

  // 기존 답변을 수정하는 메서드 추가
  public void updateQnaAnswer(QnaDTO qna) throws Exception {
    adminMapper.updateQnaAnswer(qna);
  }
}