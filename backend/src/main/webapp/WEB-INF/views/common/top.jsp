<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<nav
	class="navbar navbar-expand-lg navbar-light bg-light shadow fixed-top">
	<div class="container">
		<a class="navbar-brand text-dark fw-bold"
			href="${root}/user?action=mvindex"> <img
			src="./assets/img/geo-map-pin.png" alt="" width="50" /> SSAFY Home
		</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">

			<ul class="navbar-nav me-auto mb-lg-0">
				<li class="nav-item"><a class="nav-link" aria-current="page"
					href="${root}/board/list">공지사항</a></li>
				<li class="nav-item"><a class="nav-link" aria-current="page"
					href="#">오늘의 뉴스</a></li>
			</ul>
			<c:if test="${userinfo == null}">
				<ul id="before-login" class="navbar-nav mb-2 me-2 mb-lg-0">
					<li class="nav-item"><a id="join" class="nav-link"
						data-bs-toggle="modal" data-bs-target="#join-modal"
						aria-current="page" href="#">회원가입</a></li>
					<li class="nav-item"><a id="login" class="nav-link"
						data-bs-toggle="modal" data-bs-target="#login-modal"
						aria-current="page" href="#">로그인</a></li>
				</ul>
			</c:if>
			<c:if test="${userinfo != null}">
				<ul id="after-login" class="navbar-nav mb-2 me-2 mb-lg-0"">
					<li class="nav-item"><a id="logout" class="nav-link"
						aria-current="page" href="#">로그아웃</a></li>
					<li class="nav-item"><a id="myPage" class="nav-link"
						data-bs-toggle="modal" data-bs-target="#my-modal"
						aria-current="page" href="#">마이페이지</a></li>
					<li class="nav-item dropdown"><a
						class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
						role="button" data-bs-toggle="dropdown" aria-expanded="false">
							메뉴 </a>
						<ul class="dropdown-menu" aria-labelledby="navbarDropdown">
							<li><a class="dropdown-item" href="#" data-bs-toggle="modal"
								data-bs-target="#pollModal"></a></li>
							<li><a class="dropdown-item" href="#">관심 지역 추가</a></li>
							<li><a class="dropdown-item" href="#">관심 지역</a></li>
						</ul></li>
				</ul>
			</c:if>
		</div>
	</div>
</nav>


<!-- 로그인 -->
<form id="login-form" action="${root}/user/login" method="POST">
	<div class="modal fade" id="login-modal" data-bs-backdrop="static"
		data-bs-keyboard="false" tabindex="-1"
		aria-labelledby="staticBackdropLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h1 class="modal-title fs-5" id="staticBackdropLabel">LOGIN</h1>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<div class="mb-3">
						<input id="login-userId" type="text" class="form-control"
							name="id" placeholder="아이디" />
						<p class="login-id-info text-danger"></p>
					</div>
					<div class="mb-3">
						<input id="login-password" type="password" class="form-control"
							name="password" placeholder="비밀번호" />
						<p class="login-pw-info text-danger"></p>
					</div>
				</div>
				<div class="modal-footer">
					<button id="login-button" type="submit" class="btn btn-dark">로그인</button>
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">닫기</button>
				</div>
			</div>
		</div>
	</div>
</form>

<!-- 회원가입 -->
<div class="modal fade" id="join-modal" data-bs-backdrop="static"
	data-bs-keyboard="false" tabindex="-1"
	aria-labelledby="staticBackdropLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<form id="join-form" action="${root}/user/create" method="POST">
				<div class="modal-header">
					<h1 class="modal-title fs-5" id="staticBackdropLabel">회원가입</h1>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<div class="mb-3">
						<input id="join-userId" type="text" class="form-control" name="id"
							placeholder="아이디" required />
						<p class="join-id-info text-danger"></p>
					</div>
					<div class="mb-3">
						<input id="join-password" type="password" class="form-control"
							name="password" placeholder="비밀번호" required />
						<p class="join-pw-info text-danger"></p>
					</div>
					<div class="mb-3">
						<input id="join-name" type="text" class="form-control" name="name"
							placeholder="이름" required />
						<p class="join-name-info text-danger"></p>
					</div>
					<div class="mb-3">
						<input id="join-email" type="email" class="form-control"
							name="email" placeholder="이메일" required />
						<p class="join-email-info text-danger"></p>
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-dark">회원가입</button>
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">닫기</button>
				</div>
			</form>
		</div>
	</div>
</div>

<!-- 내 정보 -->
<div class="modal fade" id="my-modal" data-bs-backdrop="static"
	data-bs-keyboard="false" tabindex="-1"
	aria-labelledby="staticBackdropLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<form id="my-info-form" action="${root}/user/update" method="POST">
				<div class="modal-header">
					<h1 class="modal-title fs-5" id="staticBackdropLabel">마이 페이지</h1>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<div class="mb-2">아이디</div>
					<div class="mb-3 ma">
						<input id="my-userId" type="text" class="form-control" name="id"
							value="${userinfo.id}" readonly />
						<p class="my-id-info text-danger"></p>
					</div>
					<div class="mb-2">비밀번호</div>
					<div class="mb-3">
						<input id="my-password" type="password" class="form-control"
							name="password" placeholder="비밀번호" value="${userinfo.password }"
							required />
						<p class="my-pw-info text-danger"></p>
					</div>
					<div class="mb-2">이름</div>
					<div class="mb-3">
						<input id="my-name" type="text" class="form-control" name="name"
							value="${userinfo.name}" required />
						<p class="my-name-info text-danger"></p>
					</div>
				</div>
				<div class="modal-footer">
					<button id="quit-button" type="button"
						class="btn btn-outline-danger" onclick="submitWithdrawal()">탈퇴하기</button>
					<button type="submit" class="btn btn-dark">변경하기</button>
				</div>
			</form>
		</div>
	</div>
</div>


<script>
    document.getElementById('logout').addEventListener('click', function(event) {
        event.preventDefault(); // 기본 링크 클릭 동작 방지

        fetch('${root}/user/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.ok) {
                // 로그아웃 성공 시 처리
                window.location.href = '${root}/'; // 리다이렉트할 URL
            } else {
                // 오류 처리
                alert('로그아웃 실패. 다시 시도해주세요.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    });

     function submitWithdrawal() {
       if (confirm("정말로 탈퇴하시겠습니까?")) {
         const form = document.createElement('form');
         form.method = 'POST';
         form.action = '${root}/user/delete'; // 탈퇴 요청을 보낼 URL

         const userId = document.getElementById('my-userId').value;
         const input = document.createElement('input');
         input.type = 'hidden';
         input.name = 'id'; // 서버에서 필요한 사용자 ID
         input.value = userId;

         form.appendChild(input);
         document.body.appendChild(form);
         form.submit(); // 폼 제출
       }
     }

</script>
