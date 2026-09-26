package bitc.aws402.miniproject.service;

import bitc.aws402.miniproject.dto.QnaDTO;
import bitc.aws402.miniproject.mapper.QnaMapper;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//  추형호 -----
@RequiredArgsConstructor
@Service
public class QnaService {

    private final QnaMapper qnaMapper;

    public List<QnaDTO> selectQnaList() throws Exception {
        return qnaMapper.selectQnaList();
    }

    public List<QnaDTO> selectQnaListPaging(int pageNum) throws Exception {
        PageHelper.startPage(pageNum,10);
        return qnaMapper.selectQnaList();
    }

    public void insertQna(QnaDTO qna) throws Exception {
        qnaMapper.insertQna(qna);
    }

    public QnaDTO selectQnaDetail(int id) throws Exception {
        return qnaMapper.selectQnaDetail(id);
    }

    public void updateHitCount(int id) throws Exception {
        qnaMapper.updateHitCount(id);
    }

    public void updateQna(QnaDTO qna) throws Exception {
        qnaMapper.updateQna(qna);
    }

    public void deleteQna(int id) throws Exception {
        qnaMapper.deleteQna(id);
    }

    // 컨트롤러에서 호출하는 서비스 빈 이름(qnaService)과 매칭
    public void insertQnaAnswer(QnaDTO qna) throws Exception {
        qnaMapper.insertQnaAnswer(qna);
    }
}