document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ login.js 로드됨!"); // 🔥 JavaScript 파일이 실행되는지 확인

    let loginButton = document.getElementById("loginBtn");
    let usernameInput = document.getElementById("userid");
    let passwordInput = document.getElementById("password");
    let errorMsg = document.getElementById("loginError");

    console.log("로그인 버튼 요소:", loginButton); // 🔥 버튼이 정상적으로 가져와지는지 확인

    if (loginButton) {
        loginButton.addEventListener("click", function (event) {
            event.preventDefault(); // 기본 동작 방지 (페이지 새로고침 방지)

            let username = usernameInput.value.trim();
            let password = passwordInput.value.trim();

            console.log("입력한 아이디:", username);
            console.log("입력한 비밀번호:", password);

            if (!username || !password) { 
                errorMsg.textContent = "아이디와 비밀번호를 입력하세요.";
                errorMsg.style.color = "red";
                return;
            }

            let storedUser = localStorage.getItem(username);
            if (!storedUser) {
                errorMsg.textContent = "존재하지 않는 아이디입니다.";
                return;
            }

            let userData = JSON.parse(storedUser); // JSON 데이터 변환
            console.log("저장된 회원 데이터:", userData);

            if (userData.password !== password) {
                errorMsg.textContent = "비밀번호가 올바르지 않습니다.";
                return;
            }

            // 로그인 성공
            localStorage.setItem("loggedInUser", username);
            alert("로그인 성공!");
            window.location.href = "index.html"; 
        });
    } else {
        console.error("loginBtn 요소를 찾을 수 없습니다. login.html을 확인하세요.");
    }
});

document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ login.js 로드됨!"); // JavaScript 로드 확인

    // 회원가입 버튼 이벤트 (signup.html에서 실행)
    let signupBtn = document.getElementById("signupBtn");
    if (signupBtn) {
        signupBtn.addEventListener("click", register);
    }

    // 로그인 버튼 이벤트 (login.html에서 실행)
    // let loginBtn = document.getElementById("loginBtn");
    // if (loginBtn) {
    //     loginBtn.addEventListener("click", login);
    // }
});


