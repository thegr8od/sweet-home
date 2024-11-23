	package com.ssafy.qna.model.service;
	
	import java.util.List;
	
	import org.springframework.stereotype.Service;
	
	import com.ssafy.qna.mapper.QnAMapper;
	import com.ssafy.qna.model.QnADto;
	
	@Service
	public class QnAServiceImpl implements QnAService {
	
	    private final QnAMapper qnaMapper;
	
	    public QnAServiceImpl(QnAMapper qnaMapper) {
	        this.qnaMapper = qnaMapper;
	    }
	
	    @Override
	    public List<QnADto> getQnAList() throws Exception {
	        return qnaMapper.getQnAList();
	    }
	
	    @Override
	    public int insertQuestion(QnADto qnaDto) throws Exception {
	        return qnaMapper.insertQuestion(qnaDto);
	    }
	
	    @Override
	    public int insertAnswer(QnADto qnaDto) throws Exception {
	        return qnaMapper.insertAnswer(qnaDto);
	    }
	
	    @Override
	    public int updateQuestion(QnADto qnaDto) throws Exception {
	        return qnaMapper.updateQuestion(qnaDto);
	    }
	
	    @Override
	    public int deleteQuestion(int idx) throws Exception {
	        return qnaMapper.deleteQuestion(idx);
	    }
	
	    @Override
	    public int deleteAnswer(int idx) throws Exception {
	        return qnaMapper.deleteAnswer(idx);
	    }
	    
	    @Override
	    public QnADto getQnA(int idx) throws Exception {
	        return qnaMapper.getQnA(idx);
	    }
	    
	}
