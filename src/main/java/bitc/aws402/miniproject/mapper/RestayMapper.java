package bitc.aws402.miniproject.mapper;

import bitc.aws402.miniproject.dto.RestayDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//  김진형 -----
@Mapper
public interface RestayMapper {

    // 1. 방 목록 조회
    List<RestayDTO> selectRoomList();

    // 2. 방 등록
    void insertRoom(RestayDTO room);

    // 3. 방 상세 조회(세부 페이지)
    RestayDTO selectRoomDetail(@Param("roomIdx") int roomIdx);

    // 4. 방 정보 수정
    void updateRoom(RestayDTO room);

    // 5. 방 정보 삭제
    void deleteRoom(@Param("roomIdx") int roomIdx);

    List<RestayDTO> selectResourceList(int roomIdx);
}
