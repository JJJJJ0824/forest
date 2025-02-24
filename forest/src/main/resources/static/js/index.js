document.addEventListener("DOMContentLoaded", function () {
  console.log("index.js 로드됨!");

  let loggedInUser = localStorage.getItem("loggedInUser");
  let loginMenuItem = document.getElementById("nav-login"); // 네비게이션 로그인 버튼

  if (loggedInUser) {
      // "로그인"을 "로그아웃"으로 변경
      loginMenuItem.textContent = "로그아웃";
      loginMenuItem.href = "#"; // href 제거
      loginMenuItem.addEventListener("click", logout);
  }
});

// 로그아웃 함수
function logout() {
  console.log("로그아웃 실행됨!");
  
  // 로그인 정보 삭제
  localStorage.removeItem("loggedInUser");
  
  alert("로그아웃 되었습니다!");
  
  // 로그인 페이지로 이동
  window.location.href = "index.html";
}