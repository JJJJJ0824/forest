document.addEventListener("DOMContentLoaded", function () {
  let loggedInUser = localStorage.getItem("loggedInUser");
  let loginMenuItem = document.getElementById("nav-login"); // 네비게이션 로그인 버튼

  // 로그인 상태 확인
  if (loggedInUser) {
      // 로그인된 경우 "로그인"을 "로그아웃"으로 변경
      loginMenuItem.textContent = "로그아웃";
      loginMenuItem.href = "#"; // href를 #로 변경 (기본적으로 이동하지 않음)
      
      // "로그아웃" 클릭 시 로그아웃 처리
      loginMenuItem.addEventListener("click", logout);
  } else {
      // 로그인되지 않은 상태에서는 로그인 버튼을 보이게 하고, 클릭 시 로그인 페이지로 이동
      loginMenuItem.textContent = "로그인";
      loginMenuItem.href = "login.html"; // 로그인 페이지로 이동
  }
});

function logout() {
  console.log("로그아웃 실행됨!");

  // 로컬스토리지에서 로그인 정보 삭제
  localStorage.removeItem("loggedInUser");

  // 로그아웃 후 로그인 페이지로 이동
  window.location.href = "login.html";
}