// signup.js 파일
document.addEventListener("DOMContentLoaded", function () {

  let signupBtn = document.getElementById("signupBtn");
  if (signupBtn) {
      signupBtn.addEventListener("click", register);
  }

  // 로그인 버튼 이벤트 (login.html에서 실행)
  let loginBtn = document.getElementById("loginBtn");
  if (loginBtn) {
      loginBtn.addEventListener("click", login);
  }
});