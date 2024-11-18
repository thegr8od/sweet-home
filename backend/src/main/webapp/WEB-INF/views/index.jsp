
<!-- 사용되지 않는 파일로 삭제해도 될 듯?? -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%
String root = request.getContextPath();
%>
<c:set var="root" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>SSAFY HOME</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous" />
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css" />
<script src="/assets/js/key.js"></script>
<link rel="stylesheet" href="/assets/css/main.css" />
</head>



<body>
	<%@ include file="./common/top.jsp"%>
	<!-- 로그인 실패 시 alert 메시지 표시 -->
	<c:if test="${not empty loginError}">
		<script>
        alert("${loginError}");
    </script>
	</c:if>
	<!-- 중앙 content start -->
	<div class="container">
		<div class="row">
			<div class="col-md">
				<div class="row col-md-12 mb-2 p-3">
					<div class="form-group col-md-2">
						${sidoList } <select class="form-select text-dark" id="sido">
							<option value="">시도선택</option>
							<c:forEach var="sido" items="${sidoList}">
								<option value="${sido}">${sido}</option>
							</c:forEach>
						</select>
					</div>
					<div class="form-group col-md-2">
						<select class="form-select text-dark" id="gugun">
							<option value="">구군선택</option>
						</select>
					</div>
					<div class="form-group col-md-2">
						<select class="form-select text-dark" id="dong">
							<option value="">동선택</option>
						</select>
					</div>
					<div class="form-group col-md-2">
						<select class="form-select text-dark" id="year">
							<option value="">매매년도선택</option>
						</select>
					</div>
					<div class="form-group col-md-2">
						<select class="form-select text-dark" id="month">
							<option value="">매매월선택</option>
						</select>
					</div>
					<div class="form-group col-md-2">
						<button type="button" id="list-btn"
							class="btn btn-outline-primary">아파트매매정보</button>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div id="apt-table-container" class="col-md-5" style="display: none">
				<table class="table table-hover text-center">
					<thead>
						<tr>
							<th>아파트이름</th>
							<th>층</th>
							<th>면적</th>
							<th>법정동</th>
							<th>거래금액</th>
						</tr>
					</thead>
					<tbody id="aptlist"></tbody>
				</table>
			</div>
			<div id="map" class="col-md" style="width: 100%; height: 1000px"></div>
		</div>
	</div>

	<!-- 중앙 content end -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
		
		    <!-- key.js 파일을 먼저 로드 -->
    <script src="key.js"></script>
    
    <!-- kakao 지도 API 스크립트, appkey에 변수 kakaoKey 사용 -->
    <script type="text/javascript" 
            src="//dapi.kakao.com/v2/maps/sdk.js?appkey=" + kakaoKey + "&libraries=services,clusterer,drawing">
    </script>
	<script type="text/javascript"
		src="//dapi.kakao.com/v2/maps/sdk.js?appkey=583d23ad1b685db36adfb14978eecc19&libraries=services,clusterer,drawing">
	</script>

	<script src="/assets/js/kakaomap.js"></script>
	<script>
    	const root = "<%=root%>";
        // 페이지가 로드될 때 임의의 좌표로 지도 초기화
        window.onload = function() {
            renderMap(37.5665, 126.978); // 서울의 위도와 경도
        };
	</script>

	<script src="/assets/js/user.js"></script>
	<script>
    	const root = "<%=root%>
		";
	</script>
	<script src="/assets/js/house_list.js"></script>
	<%@include file="./common/footer.jsp"%>
</body>



</html>
