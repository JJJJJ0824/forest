document.addEventListener("DOMContentLoaded", function () {
  console.log("✅ login.js 로드됨!");

  let loginBtn = document.getElementById("loginBtn");
  let usernameInput = document.getElementById("userid");
  let passwordInput = document.getElementById("password");
  let errorMsg = document.getElementById("loginError");

  // 로그인 버튼 클릭 시
  if (loginBtn) {
    loginBtn.addEventListener("click", function (event) {
      event.preventDefault();

      let username = usernameInput.value.trim();
      let password = passwordInput.value.trim();

      if (!username || !password) {
        errorMsg.textContent = "아이디와 비밀번호를 입력하세요.";
        errorMsg.style.color = "red";
        return;
      }

      let xhr = new XMLHttpRequest();
      xhr.open("POST", "/api/traveler/login", true);
      xhr.setRequestHeader("Content-Type", "application/json");

      xhr.onload = function () {
        if (xhr.status === 200) {
          let response = xhr.responseText;

          // 로그인 성공 시 localStorage에 사용자 정보 저장
          localStorage.setItem("loggedInUser", username);

          // 로그인 후 홈 페이지로 리다이렉트
          window.location.href = "index.html";
        } else {
          errorMsg.textContent = "아이디 또는 비밀번호가 올바르지 않습니다.";
          errorMsg.style.color = "red";
        }
      };

      xhr.onerror = function () {
        errorMsg.textContent = "네트워크 오류가 발생했습니다. 다시 시도해주세요.";
        errorMsg.style.color = "red";
      };

      let loginData = {
        travelerName: username,
        password: password
      };

      xhr.send(JSON.stringify(loginData));
    });
  }
});
