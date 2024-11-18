// 공지사항 데이터를 배열로 정의
/*
const notices = [
  { no: 1, title: "SSAFY Home 첫 번째 공지", view: 35 },
  { no: 2, title: "SSAFY Home 두 번째 공지", view: 416 },
  { no: 3, title: "2024년 서비스 출시 리뷰 이벤트 당첨자 안내", view: 3215 },
  { no: 4, title: "아파트 매매 거래 분석 리포트", view: 250 },
  { no: 5, title: "신축 아파트 전세 시세 동향", view: 987 },
  { no: 6, title: "다세대주택 월세 거래 증가 분석", view: 523 },
  { no: 7, title: "관심지역 상가 임대 정보 업데이트", view: 812 },
  { no: 8, title: "최신 부동산 환경 정보 제공 안내", view: 650 },
  { no: 9, title: "신규 등록 매물 알림", view: 440 },
  { no: 10, title: "다세대주택 매매 거래 급증 원인 분석", view: 1105 },
];*/


//글쓰기 버튼
const writeBtn = document.getElementById("write-button");

//업데이트 버튼
const updateBtn = document.getElementById("update-button");

// 테이블의 tbody 요소를 선택
const tbody = document.querySelector("#noticeTable tbody");



// 공지사항 데이터를 기반으로 테이블 행을 생성
/*
notices.forEach((notice) => {
  const row = document.createElement("tr");

  const noCell = document.createElement("td");
  noCell.textContent = notice.no;
  row.appendChild(noCell);

  const titleCell = document.createElement("td");
  titleCell.textContent = notice.title;
  row.appendChild(titleCell);

  const viewCell = document.createElement("td");
  viewCell.textContent = notice.view;
  row.appendChild(viewCell);

  tbody.appendChild(row);
});
*/

for (let i = 0; i < 10; i++) {
  const row = document.createElement("tr");

  const noCell = document.createElement("td");
  noCell.innerHTML = "&nbsp;"; // 빈 공백을 표시
  row.appendChild(noCell);

  const titleCell = document.createElement("td");
  titleCell.innerHTML = "&nbsp;"; // 빈 공백을 표시
  row.appendChild(titleCell);

  const viewCell = document.createElement("td");
  viewCell.innerHTML = "&nbsp;"; // 빈 공백을 표시
  row.appendChild(viewCell);

  tbody.appendChild(row);
}

// 글쓰기 버튼
if(writeBtn){
  writeBtn.addEventListener("click",(e)=>{
    const writemodal = document.getElementById("write-modal");
    const modal = bootstrap.Modal.getInstance(writemodal);
    alert("저장되었습니다");
    modal.hide();
    renderNavBar();
  })
}


if(updateBtn){
  updateBtn.addEventListener("click",(e)=>{
    const updatemodal = document.getElementById("update-modal");
    const modal = bootstrap.Modal.getInstance(updatemodal);
    alert("되었습니다.");
    modal.hide();
    renderNavBar();
  })
}