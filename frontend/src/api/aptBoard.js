import { apiInstance } from './index.js'
const api = apiInstance()

// 게시글 관련 API
/**
 * 새로운 게시글 생성
 * @param {Object} board - 게시글 정보 (title, content, aptSeq 필수)
 */
async function createBoard(board, success, fail) {
  await api.post(`/aptboard`, board).then(success).catch(fail)
}

/**
 * 게시글 상세 정보 조회
 * @param {number} id - 게시글 ID
 */
async function getBoardById(id, success, fail) {
  await api.get(`/aptboard/${id}/get`).then(success).catch(fail)
}

/**
 * 아파트별 게시글 목록 조회
 * @param {string} aptSeq - 아파트 고유 번호
 */
async function getBoardsByAptSeq(aptSeq, success, fail) {
  await api.get(`/aptboard/list/${aptSeq}`).then(success).catch(fail)
}

/**
 * 게시글 수정
 * @param {number} id - 게시글 ID
 * @param {Object} board - 수정할 게시글 정보
 */
async function updateBoard(id, board, success, fail) {
  await api.put(`/aptboard/${id}`, board).then(success).catch(fail)
}

/**
 * 게시글 삭제
 * @param {number} id - 게시글 ID
 */
async function deleteBoard(id, success, fail) {
  await api.delete(`/aptboard/${id}`).then(success).catch(fail)
}

/**
 * 게시글 좋아요 추가
 * @param {number} id - 게시글 ID
 * @param {string} userId - 사용자 ID
 */
async function likeBoard(id, userId, success, fail) {
  await api.post(`/aptboard/${id}/like`, { userId: userId }, {
    headers: {
      'Content-Type': 'application/json'
    }
  }).then(success).catch(fail)
}

/**
 * 게시글 좋아요 취소
 * @param {number} id - 게시글 ID
 * @param {string} userId - 사용자 ID
 */
async function unlikeBoard(id, userId, success, fail) {
  await api.delete(`/aptboard/${id}/like`, { 
    headers: {
      'Content-Type': 'application/json'
    },
    data: { userId: userId }
  }).then(success).catch(fail)
}

/**
 * 댓글 작성
 * @param {number} boardId - 게시글 ID
 * @param {Object} comment - 댓글 정보 (content 필수)
 */
async function createComment(boardId, comment, success, fail) {
  await api.post(`/aptboard/comment/${boardId}`, comment).then(success).catch(fail)
}

/**
 * 댓글 목록 조회
 * @param {number} boardId - 게시글 ID
 */
async function getComments(boardId, success, fail) {
  await api.get(`/aptboard/comment/list/${boardId}`).then(success).catch(fail)
}

/**
 * 댓글 수정
 * @param {number} commentId - 댓글 ID
 * @param {Object} comment - 수정할 댓글 정보
 */
async function updateComment(commentId, comment, success, fail) {
  await api.put(`/aptboard/comment/${commentId}`, comment).then(success).catch(fail)
}

/**
 * 댓글 삭제
 * @param {number} commentId - 댓글 ID
 */
async function deleteComment(commentId, success, fail) {
  await api.delete(`/aptboard/comment/${commentId}`).then(success).catch(fail)
}

/**
 * 댓글 좋아요 추가
 * @param {number} commentId - 댓글 ID
 * @param {string} userId - 사용자 ID
 */
async function likeComment(commentId, userId, success, fail) {
  await api.post(`/aptboard/comment/${commentId}/like`, { userId: userId }, {
    headers: {
      'Content-Type': 'application/json'
    }
  }).then(success).catch(fail)
}

/**
 * 댓글 좋아요 취소
 * @param {number} commentId - 댓글 ID
 * @param {string} userId - 사용자 ID
 */
async function unlikeComment(commentId, userId, success, fail) {
  await api.delete(`/aptboard/comment/${commentId}/like`, {
    headers: {
      'Content-Type': 'application/json'
    },
    data: { userId: userId }
  }).then(success).catch(fail)
}

export {
  createBoard,
  getBoardById,
  getBoardsByAptSeq,
  updateBoard,
  deleteBoard,
  likeBoard,
  unlikeBoard,
  createComment,
  getComments,
  updateComment,
  deleteComment,
  likeComment,
  unlikeComment,
} 