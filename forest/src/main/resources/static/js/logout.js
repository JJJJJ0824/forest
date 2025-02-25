document.addEventListener("DOMContentLoaded", function () {
  console.log("✅ login.js 로드됨!"); 

  let loginBtn = document.getElementById("loginBtn");
  let usernameInput = document.getElementById("userid");
  let passwordInput = document.getElementById("password");
  let errorMsg = document.getElementById("loginError");

  let loginMenuItem = document.getElementById("nav-login"); 

  let loggedInUser = localStorage.getItem("loggedInUser");
  if (loggedInUser) {
      loginMenuItem.textContent = "로그아웃";
      loginMenuItem.href = "#"; 
      loginMenuItem.addEventListener("click", logout);
  } else {
      loginMenuItem.textContent = "로그인";
      loginMenuItem.href = "login.html"; 
  }

  if (loginBtn) {
      loginBtn.addEventListener("click", function (event) {
          event.preventDefault(); 

          let username = usernameInput.value.trim();
          let password = passwordInput.value.trim();

          console.log("입력한 아이디:", username);
          console.log("입력한 비밀번호:", password);

          if (!username || !password) { 
              errorMsg.textContent = "아이디와 비밀번호를 입력하세요.";
              errorMsg.style.color = "red";
              return;
          }

          let xhr = new XMLHttpRequest();
          xhr.open("POST", "/api/traveler/login", true);
          xhr.setRequestHeader("Content-Type", "application/json");

          xhr.onload = function() {
              if (xhr.status === 200) { 
                  let response = xhr.responseText;
                  alert(response); 

                  localStorage.setItem("loggedInUser", username);

                  window.location.href = "index.html"; 

                  loginMenuItem.textContent = "로그아웃";
                  loginMenuItem.href = "#";
                  loginMenuItem.addEventListener("click", logout);

              } else {
                  errorMsg.textContent = "아이디 또는 비밀번호가 올바르지 않습니다.";
                  errorMsg.style.color = "red";
              }
          };

          xhr.onerror = function() {
              errorMsg.textContent = "네트워크 오류가 발생했습니다. 다시 시도해주세요.";
              errorMsg.style.color = "red";
          };

          let loginData = {
              travelerName: username, 
              password: password   
          };

          xhr.send(JSON.stringify(loginData)); 
      });
  } else {
      console.error("loginBtn 요소를 찾을 수 없습니다. login.html을 확인하세요.");
  }
});

function logout() {
  console.log("로그아웃 실행됨!");

  let xhr = new XMLHttpRequest();
  xhr.open("POST", "/api/traveler/logout", true); 
  xhr.setRequestHeader("Content-Type", "application/json");

  xhr.onload = function () {
      if (xhr.status === 200) {
          console.log("로그아웃 성공!");

          localStorage.removeItem("loggedInUser");

          window.location.href = "login.html"; 
      } else {
          console.error("로그아웃 실패!");
          alert("로그아웃에 실패했습니다. 다시 시도해주세요.");
      }
  };

  xhr.onerror = function () {
      console.error("네트워크 오류 발생");
      alert("네트워크 오류가 발생했습니다. 다시 시도해주세요.");
  };

  xhr.send(); 
}
