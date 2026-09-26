package bitc.aws402.miniproject.mapper;

import bitc.aws402.miniproject.dto.AccommodationDTO;
import bitc.aws402.miniproject.dto.MemberDTO;
import bitc.aws402.miniproject.dto.ReservationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//  추형호 -----
@Mapper
public interface MemberMapper {
    int checkIdExist(@Param("memberId") String memberId);
    void insertMember(MemberDTO member) throws Exception;
    MemberDTO selectMemberByUserId(@Param("memberId") String memberId);
    List<MemberDTO> selectAllMembers() throws Exception;

    // 마이페이지 및 정보 수정
    MemberDTO selectUserInfo(String userId);
    void updateUserInfo(MemberDTO user);

    // 관리자 조회용
    List<AccommodationDTO> selectAllAccommodations() throws Exception;
    List<ReservationDTO> selectAllReservations() throws Exception;
}