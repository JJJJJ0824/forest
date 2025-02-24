document.addEventListener("DOMContentLoaded", function () {
  console.log('logout() 실행됨!');

  let loggedInUser = localStorage.getItem("loggedInUser");
  let loginMenuItem = document.getElementById("nav-login"); // 네비게이션 로그인 버튼

  if (loggedInUser) {
      // "로그인"을 "로그아웃"으로 변경
      loginMenuItem.textContent = "로그아웃";
      loginMenuItem.href = "#"; // href 제거
      loginMenuItem.addEventListener("click", logout);
  }
});

function logout() {
  console.log("로그아웃 실행됨!");
  
  localStorage.removeItem("loggedInUser");
  
  window.location.href = "login.html";
};
