<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title>Bootstrap Example</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="${root}/css/board.css" />
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
    <div class="container mt-5 p-3 text-center" style="margin-top: 200px">
        <h2 id="board-title">공지사항</h2>
        <p>자자 지금부터 SSAFY Home의 공지사항에 대해서 알려드리겠습니다</p>
        <div class="mt-5">
            <table class="table table-hover" id="noticeTable">
                <thead>
                    <tr>
                        <th>No</th>
                        <th>Title</th>
                        <th>View</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="post" items="${list}">
                        <tr>
                            <td><a href="${root}/board/view?postId=${post.postId}">${post.postId}</a></td>
                            <td><a href="${root}/board/view?postId=${post.postId}">${post.title}</a></td>
                            <td>${post.view}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <button id="write" type="button" class="btn btn-dark" data-bs-toggle="modal" data-bs-target="#write-modal" style="float: right; margin-right: 20px; margin-bottom: 10px;">글쓰기</button>
        </div>
    </div>
    <%@ include file="../common/footer.jsp"%>

    <!-- 글쓰기 모달 -->
    <div class="modal fade" id="write-modal" data-bs-backdrop="static" data-bs-keyboard="false" tabindex="-1" aria-labelledby="writeModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <form id="write-form" action="${root}/board/write" method="POST">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="writeModalLabel">게시판 글쓰기</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body">
                        <div class="mb-2">제목</div>
                        <div class="mb-3">
                            <input id="post-title" type="text" class="form-control" name="title" placeholder="제목을 입력하세요" required />
                            <p class="post-title-info text-danger"></p>
                        </div>

                        <div class="mb-2">내용</div>
                        <div class="mb-3">
                            <textarea id="post-content" class="form-control" name="content" placeholder="내용을 입력하세요" rows="10" required></textarea>
                            <p class="post-content-info text-danger"></p>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button id="write-button" type="submit" class="btn btn-outline-info">글쓰기</button>
                        <button id="mReset" type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="${root}/js/board.js"></script>
    <script src="${root}/js/user.js"></script>
</body>
</html>
