package bitc.aws402.miniproject.service;

import bitc.aws402.miniproject.dto.RestayDTO;
import bitc.aws402.miniproject.mapper.RestayMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DetailService {

    private final RestayMapper restayMapper;

    public List<RestayDTO> selectRoomList() {
        return restayMapper.selectRoomList();
    }

    public RestayDTO selectRoomDetail(int roomIdx) {
        return restayMapper.selectRoomDetail(roomIdx);
    }
    
    public List<RestayDTO> selectResourceList(int roomIdx) {
        return restayMapper.selectResourceList(roomIdx);
    }
}
