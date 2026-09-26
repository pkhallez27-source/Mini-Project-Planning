package bitc.aws402.miniproject.mapper;

import bitc.aws402.miniproject.dto.QnaDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//  추형호 -----
@Mapper
public interface QnaMapper {
    List<QnaDTO> selectQnaList() throws Exception;
    void insertQna(QnaDTO qna) throws Exception;
    QnaDTO selectQnaDetail(int id) throws Exception;
    void updateHitCount(int id) throws Exception;
    void updateQna(QnaDTO qna) throws Exception;
    void deleteQna(int id) throws Exception;
    void insertQnaAnswer(QnaDTO qna) throws Exception;
}