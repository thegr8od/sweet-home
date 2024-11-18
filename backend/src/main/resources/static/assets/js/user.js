window.onload = function () {
  // 사용자 데이터와 로그인 상태 관리
  let user = null;
  let isLoggedIn = false;
  // DOM 요소 선택
  const loginBtn = document.getElementById("login-button");
  const logoutBtn = document.getElementById("logout");
  const joinBtn = document.getElementById("join-button");
  const updateBtn = document.getElementById("update-button");
  const quitBtn = document.getElementById("quit-button");

  // 네비게이션 바 초기 렌더링
  renderNavBar();

  // 로그인 이벤트
  if (loginBtn) {
    loginBtn.addEventListener("click", (e) => {
      e.preventDefault(); // 기본 폼 제출 동작 방지

      const userId = document.getElementById("login-userId").value;
      const password = document.getElementById("login-password").value;

      // 사용자 인증
      if (user && userId === user.userId && password === user.password) {
        alert("로그인 성공");
        isLoggedIn = true;
        renderNavBar(); // 네비게이션 바 업데이트
      } else {
        // 로그인 실패 시 메시지 표시 및 모달 닫기
        alert("아이디 비밀번호가 일치하지 않습니다.");
      }
    });
  }

  // 회원가입 이벤트
  if (joinBtn) {
    joinBtn.addEventListener("click", (e) => {
      e.preventDefault(); // 기본 폼 제출 동작 방지

      const userId = document.getElementById("join-userId").value;
      const password = document.getElementById("join-password").value;
      const name = document.getElementById("join-name").value;
      const phone = document.getElementById("join-phone").value;
      const email = document.getElementById("join-email").value;

      const idInfo = document.querySelector(".join-id-info");
      const pwInfo = document.querySelector(".join-pw-info");
      const nameInfo = document.querySelector(".join-name-info");
      const phoneInfo = document.querySelector(".join-phone-info");
      const emailInfo = document.querySelector(".join-email-info");

      idInfo.textContent = "";
      pwInfo.textContent = "";
      nameInfo.textContent = "";
      phoneInfo.textContent = "";
      emailInfo.textContent = "";

      // 모든 필드가 채워졌는지 확인
      if (userId && password && name && phone && email) {
        user = { userId, password, name, phone, email }; // 사용자 정보 저장
        alert("회원가입 완료!");
        closeModal("join-modal"); // 모달 닫기
      } else {
        // 빈 필드에 대한 오류 메시지 표시
        if (!userId) idInfo.textContent = "아이디를 입력하세요";
        if (!password) pwInfo.textContent = "비밀번호를 입력하세요";
        if (!name) nameInfo.textContent = "이름을 입력하세요";
        if (!phone) phoneInfo.textContent = "휴대번호를 입력하세요";
        if (!email) emailInfo.textContent = "이메일을 입력하세요";
      }
    });
  }

  // 로그아웃 이벤트
  if (logoutBtn) {
    logoutBtn.addEventListener("click", (e) => {
      e.preventDefault(); // 기본 동작 방지

      isLoggedIn = false; // 로그인 상태 해제
      alert("로그아웃 되었습니다.");
      renderNavBar(); // 네비게이션 바 업데이트
    });
  }

  // 회원정보 수정 이벤트
  if (updateBtn) {
    updateBtn.addEventListener("click", (e) => {
      e.preventDefault(); // 기본 동작 방지

      if (!isLoggedIn || !user) return;

      const name = document.getElementById("update-name").value;
      const phone = document.getElementById("update-phone").value;
      const email = document.getElementById("update-email").value;

      // 사용자 정보 업데이트
      if (name) user.name = name;
      if (phone) user.phone = phone;
      if (email) user.email = email;

      alert("회원정보가 수정되었습니다.");
      closeModal("my-modal"); // 모달 닫기
    });
  }

  // 회원탈퇴 이벤트
  if (quitBtn) {
    quitBtn.addEventListener("click", (e) => {
      e.preventDefault(); // 기본 동작 방지

      if (!isLoggedIn) return;

      user = null; // 사용자 정보 삭제
      isLoggedIn = false; // 로그인 상태 해제
      alert("탈퇴되었습니다.");
      closeModal("my-modal"); // 모달 닫기
      renderNavBar(); // 네비게이션 바 업데이트
    });
  }

  // 네비게이션 바 렌더링 함수
  function renderNavBar() {
    const beforeLogin = document.getElementById("before-login");
    const afterLogin = document.getElementById("after-login");

    if (isLoggedIn) {
      beforeLogin.style.display = "none";
      afterLogin.style.display = "flex";
    } else {
      beforeLogin.style.display = "flex";
      afterLogin.style.display = "none";
    }
  }

  // 모달 닫기 함수
  function closeModal(modalId) {
    const modalElement = document.getElementById(modalId);
    const modalInstance = bootstrap.Modal.getInstance(modalElement) || new bootstrap.Modal(modalElement);
    modalInstance.hide();
  }
};
