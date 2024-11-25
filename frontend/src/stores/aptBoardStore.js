import { defineStore } from 'pinia'
import {
  getBoardsByAptSeq,
  getBoardById,
  likeBoard,
  unlikeBoard,
  createBoard,
  updateBoard,
  deleteBoard,
  createComment,
} from '@/api/aptBoard'
import { useUserStore } from '@/stores/user'
import { useHouseStore } from './houseStore'
export const useAptBoardStore = defineStore('aptBoard', {
  state: () => ({
    boards: [],
    currentBoard: null,
    currentAptSeq: null,
    isLoading: false,
    error: null,
  }),

  getters: {
    getBoardsList: (state) => state.boards,
    getCurrentAptSeq: (state) => state.currentAptSeq,
    getCurrentBoard: (state) => state.currentBoard,
  },

  actions: {
    // 게시글 생성
    async createNewBoard(board) {
      const houseStore = useHouseStore()
      const aptSeq = houseStore.selectedHouse?.aptSeq

      // selectedHouse가 없거나 aptSeq가 없으면 에러
      if (!houseStore.selectedHouse || !aptSeq) {
        throw new Error('선택된 아파트가 없습니다.')
      }

      try {
        await createBoard(
          board,
          (response) => {
            this.fetchBoardsByAptSeq(aptSeq)
          },
          (error) => {
            console.error('게시글 생성 실패:', error)
            throw error
          },
        )
      } catch (error) {
        throw error
      }
    },

    // 게시글 상세 조회
    async fetchBoardDetail(boardId) {
      if (!boardId) return

      this.isLoading = true
      this.error = null

      try {
        await getBoardById(
          boardId,
          (response) => {
            this.currentBoard = response.data
            this.error = null
          },
          (error) => {
            console.error('게시글 조회 실패:', error)
            this.error = '게시글을 불러오는데 실패했습니다.'
            this.currentBoard = null
          },
        )
      } catch (error) {
        console.error('게시글 조회 중 에러:', error)
        this.error = '게시글을 불러오는데 실패했습니다.'
        this.currentBoard = null
      } finally {
        this.isLoading = false
      }
    },

    // 아파트별 게시글 목록 조회
    async fetchBoardsByAptSeq(aptSeq) {
      const houseStore = useHouseStore()

      // selectedHouse가 없거나 aptSeq가 없으면 리턴
      if (!houseStore.selectedHouse || !aptSeq) {
        this.boards = []
        return
      }

      this.isLoading = true
      this.currentAptSeq = aptSeq

      try {
        await getBoardsByAptSeq(
          aptSeq,
          (response) => {
            this.boards = response.data
            this.error = null
          },
          (error) => {
            console.error('게시글 목록 조회 실패:', error)
            this.error = '게시글을 불러오는데 실패했습니다.'
            this.boards = []
          },
        )
      } catch (error) {
        console.error('게시글 목록 조회 중 에러:', error)
        this.error = '게시글을 불러오는데 실패했습니다.'
        this.boards = []
      } finally {
        this.isLoading = false
      }
    },

    // 게시글 수정
    async updateBoardContent(boardId, boardData) {
      try {
        await updateBoard(
          boardId,
          boardData,
          (response) => {
            const index = this.boards.findIndex((b) => b.id === boardId)
            if (index !== -1) {
              this.boards[index] = { ...this.boards[index], ...boardData }
            }
            if (this.currentBoard?.id === boardId) {
              this.currentBoard = { ...this.currentBoard, ...boardData }
            }
          },
          (error) => {
            console.error('게시글 수정 실패:', error)
            throw error
          },
        )
      } catch (error) {
        throw error
      }
    },

    // 게시글 삭제
    async removeBoardById(boardId) {
      try {
        await deleteBoard(
          boardId,
          () => {
            this.boards = this.boards.filter((b) => b.id !== boardId)
            if (this.currentBoard?.id === boardId) {
              this.currentBoard = null
            }
          },
          (error) => {
            console.error('게시글 삭제 실패:', error)
            throw error
          },
        )
      } catch (error) {
        throw error
      }
    },

    // 게시글 좋아요 상태 업데이트
    async updateBoardLike(boardId, isLiked) {
      const userStore = useUserStore()
      if (!userStore.isLoggedIn || !userStore.userId) {
        throw new Error('로그인이 필요한 서비스입니다.')
      }

      try {
        if (isLiked) {
          await likeBoard(boardId, userStore.userId)
        } else {
          await unlikeBoard(boardId, userStore.userId)
        }

        // 게시글 목록에서 해당 게시글 업데이트
        const boardIndex = this.boards.findIndex((b) => b.id === boardId)
        if (boardIndex !== -1) {
          // 배열 요소를 새 객체로 교체하여 반응성 보장
          this.boards[boardIndex] = {
            ...this.boards[boardIndex],
            liked: isLiked,
            likesCount: this.boards[boardIndex].likesCount + (isLiked ? 1 : -1),
          }
        }

        // 현재 보고 있는 게시글도 새 객체로 교체
        if (this.currentBoard?.id === boardId) {
          this.currentBoard = {
            ...this.currentBoard,
            liked: isLiked,
            likesCount: this.currentBoard.likesCount + (isLiked ? 1 : -1),
          }
        }
      } catch (error) {
        console.error('좋아요 상태 업데이트 실패:', error)
        throw error
      }
    },

    // 댓글 추가 시 게시글의 댓글 수도 업데이트
    async createNewComment(boardId, comment) {
      try {
        await createComment(
          boardId,
          comment,
          (response) => {
            // 게시글 목록에서 해당 게시글의 댓글 수 증가
            const board = this.boards.find((b) => b.id === boardId)
            if (board) {
              board.commentsCount++
            }
            // 현재 보고 있는 게시글의 댓글 수도 증가
            if (this.currentBoard?.id === boardId) {
              this.currentBoard.commentsCount++
            }
          },
          (error) => {
            console.error('댓글 작성 실패:', error)
            throw error
          },
        )
      } catch (error) {
        throw error
      }
    },

    // 초기화
    clearBoards() {
      this.boards = []
      this.currentBoard = null
      this.currentAptSeq = null
      this.error = null
    },
  },
})
