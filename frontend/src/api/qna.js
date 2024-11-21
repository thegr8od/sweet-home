import { apiInstance } from "./index";

const api = apiInstance();

// QnA 목록 조회
export const getQnaList = async () => {
  return await api.get(`/qna/list`);
};

// 질문 등록
export const createQuestion = async (question) => {
  return await api.post(`/qna/create`, question);
};

// 답변 등록
export const createAnswer = async (answer) => {
  return await api.post(`/qna/answer`, answer);
};

// 질문 수정
export const updateQuestion = async (question) => {
  return await api.put(`/qna/update`, question);
};

// 질문 삭제
export const deleteQuestion = async (idx) => {
  return await api.delete(`/qna/delete/${idx}`);
};

// 답변 삭제
export const deleteAnswer = async (idx) => {
  return await api.delete(`/qna/delete-answer/${idx}`);
};
