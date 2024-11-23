import { apiInstance } from "./index";

const api = apiInstance();

export const getQnaList = async () => {
  return await api.get(`/qna/list`);
};

export const createQuestion = async (question) => {
  return await api.post(`/qna/create`, {
    title: question.title,
    content: question.content
  });
};

export const createAnswer = async (answer) => {
  return await api.post(`/qna/answer`, {
    idx: answer.idx,
    answer: answer.answer
  });
};

export const updateQuestion = async (question) => {
  return await api.put(`/qna/update`, {
    idx: question.idx,
    title: question.title,
    content: question.content,
    userid: question.userid
  });
};

export const deleteQuestion = async (idx) => {
  return await api.delete(`/qna/delete/${idx}`);
};

export const deleteAnswer = async (idx) => {
  return await api.delete(`/qna/delete-answer/${idx}`);
};

