<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="ko">
<head>
<title></title>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet" />
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
	<div class="container mt-5 p-3">
		<h2>게시글 보기</h2>
		<div class="mb-3">
			<label class="form-label">제목</label>
			<div class="border p-2">${post.title}</div>
		</div>

		<div class="mb-3">
			<label class="form-label">내용</label>
			<div class="border p-3" style="min-height: 200px;">${post.content}</div>
		</div>

		<div class="mt-4">
			<!--  <a href="" class="btn btn-primary">수정하기</a> -->
			<a id="update" class="btn btn-secondary" data-bs-toggle="modal"
				data-bs-target="#update-modal" aria-current="page" href="#">
				업데이트</a> <a href="${root}/board?action=list" class="btn btn-secondary">리스트
				돌아가기</a> <a href="${root}/board?action=delete&postId=${post.postId}"
				class="btn btn-secondary">삭제</a>
		</div>
	</div>
</body>
<!-- 글 수정 모달 -->
<div class="modal fade" id="update-modal" data-bs-backdrop="static"
	data-bs-keyboard="false" tabindex="-1"
	aria-labelledby="writeModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<form id="update-form" action="${root}/board?action=update"
				method="POST">
				<div class="modal-header">
					<h1 class="modal-title fs-5" id="writeModalLabel">게시글 수정</h1>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>

				<div class="modal-body">
					<!-- 게시글 ID (보이지 않게 처리) -->
					<input type="hidden" id="post-id" name="postId"
						value="${post.postId}" />

					<div class="mb-2">제목</div>
					<div class="mb-3">
						<input id="post-title" type="text" class="form-control"
							name="title" placeholder="제목을 입력하세요" value="${post.title}"
							required />
						<p class="post-title-info text-danger"></p>
					</div>

					<div class="mb-2">내용</div>
					<div class="mb-3">
						<textarea id="post-content" class="form-control" name="content"
							placeholder="내용을 입력하세요" rows="10" required>${post.content}</textarea>
						<p class="post-content-info text-danger"></p>
					</div>
				</div>

				<div class="modal-footer">
					<button id="update-button" type="submit"
						class="btn btn-outline-info">수정하기</button>
					<button id="mReset" type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">닫기</button>
				</div>
			</form>
		</div>
	</div>
</div>

<script src="./js/board.js"></script>
</html>
