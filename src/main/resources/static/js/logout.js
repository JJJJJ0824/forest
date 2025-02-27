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
  
  document.addEventListener("DOMContentLoaded", function () {
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
  });
  