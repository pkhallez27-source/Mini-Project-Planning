package bitc.aws402.miniproject.service;

import bitc.aws402.miniproject.dto.AccommodationDTO;
import bitc.aws402.miniproject.dto.MemberDTO;
import bitc.aws402.miniproject.dto.ReservationDTO;
import bitc.aws402.miniproject.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

//  추형호 -----
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberMapper memberMapper;

    // --- 회원 관련 ---
    public int checkIdExist(String memberId) {
        int checkResult = memberMapper.checkIdExist(memberId);
        return checkResult;
    }

    public void registerMember(MemberDTO member) throws Exception {
        memberMapper.insertMember(member);
    }

    public MemberDTO findMemberById(String memberId) {
        return memberMapper.selectMemberByUserId(memberId);
    }

    public List<MemberDTO> getAllMembers() throws Exception {
        List<MemberDTO> list = memberMapper.selectAllMembers();
        return list != null ? list : Collections.emptyList();
    }

    // 마이페이지 회원 정보 조회
    public MemberDTO getUserInfo(String userId) {
        return memberMapper.selectUserInfo(userId);
    }

    // 회원 정보 수정
    public void updateUserInfo(MemberDTO user) {
        memberMapper.updateUserInfo(user);
    }

    // --- 숙소 및 예약 관련 ---
    public List<AccommodationDTO> getAllAccommodations() throws Exception {
        List<AccommodationDTO> list = memberMapper.selectAllAccommodations();
        return list != null ? list : Collections.emptyList();
    }

    public List<ReservationDTO> getAllReservations() throws Exception {
        List<ReservationDTO> list = memberMapper.selectAllReservations();
        return list != null ? list : Collections.emptyList();
    }
}