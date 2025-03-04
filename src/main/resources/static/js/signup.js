document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ signup.js 로드됨!");
  
    let signupBtn = document.getElementById("signupBtn");
    let emailVerifyBtn = document.getElementById("emailVerifyBtn");
    let verifyCodeBtn = document.getElementById("verifyCodeBtn");
  
    if (signupBtn) {
        signupBtn.addEventListener("click", register);
    }
  
    if (emailVerifyBtn) {
        emailVerifyBtn.addEventListener("click", sendEmailVerification);
    }
  
    if (verifyCodeBtn) {
        verifyCodeBtn.addEventListener("click", verifyEmailCode);
    }
  });
  
  function sendEmailVerification() {
    let email = document.getElementById("signupEmail").value.trim();
    let message = document.getElementById("emailVerifyMessage");
  
    if (!email.includes("@") || !email.includes(".")) {
        message.textContent = "올바른 이메일 주소를 입력하세요.";
        message.style.color = "red";
        return;
    }
  
    // 🔥 6자리 랜덤 인증 코드 생성
    let verificationCode = Math.floor(100000 + Math.random() * 900000).toString();
    localStorage.setItem("emailVerificationCode", verificationCode);
  
    console.log("📌 생성된 인증 코드:", verificationCode); // 🔥 생성된 코드 확인 (테스트용)
    
    alert(`이메일로 인증 코드가 전송되었습니다! (테스트용 코드: ${verificationCode})`);
  
    document.getElementById("emailCode").disabled = false;
    document.getElementById("verifyCodeBtn").disabled = false;
  };
  
  // ✅ 이메일 인증 코드 검증
  function verifyEmailCode() {
    let inputCode = document.getElementById("emailCode").value.trim();
    let storedCode = localStorage.getItem("emailVerificationCode");
    let message = document.getElementById("emailCodeMessage");
  
    if (inputCode === storedCode) {
        message.textContent = "이메일 인증 완료!";
        message.style.color = "green";
  
        localStorage.removeItem("emailVerificationCode");
  
        localStorage.setItem("emailVerified", "true");
    } else {
        message.textContent = "잘못된 인증 코드입니다.";
        message.style.color = "red";
    }
  };
  
  function register() {
    console.log("register() 실행됨!");
  
    let username = document.getElementById("signupUser").value.trim();
    let realName = document.getElementById("realName").value.trim();
    let email = document.getElementById("signupEmail").value.trim();
    let contact = document.getElementById("signupContact").value.trim();
    let password = document.getElementById("signupPass").value.trim();
    let confirmPassword = document.getElementById("signupConfirm").value.trim();
    let agreeTerms = document.getElementById("agreeTerms").checked;
    let errorMsg = document.getElementById("signupError");
  
    if (!username || !realName || !email || !contact || !password || !confirmPassword) {
        errorMsg.textContent = "모든 필드를 입력하세요.";
        return;
    }
  
    if (!localStorage.getItem("emailVerified")) {
        errorMsg.textContent = "이메일 인증을 완료해야 회원가입이 가능합니다.";
        return;
    }
  
    if (!email.includes("@") || !email.includes(".")) {
        errorMsg.textContent = "올바른 이메일 형식을 입력하세요.";
        return;
    }
  
    if (!contact.match(/^01[0-9]-?\d{3,4}-?\d{4}$/)) {
        errorMsg.textContent = "연락처 형식이 올바르지 않습니다. (예: 010-1234-5678)";
        return;
    }
  
    if (password !== confirmPassword) {
        errorMsg.textContent = "비밀번호가 일치하지 않습니다.";
        return;
    }
  
    if (!agreeTerms) {
        errorMsg.textContent = "약관에 동의해야 합니다.";
        return;
    }
  
    if (localStorage.getItem(username)) {
        errorMsg.textContent = "이미 존재하는 아이디입니다.";
        return;
    }
  
    let userData = {
      travelerName: username,
      email: email,
      contact: contact,
      password: password,
      realName: realName
    };
  
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/api/traveler/register", true);
    xhr.setRequestHeader("Content-Type", "application/json");
  
    xhr.onload = function() {
      if (xhr.status === 201) {
          alert("회원가입이 완료되었습니다!");
  
          // 회원가입 후 자동 로그인 요청
          login(username, password); // 로그인 함수 호출
  
      } else {
          errorMsg.textContent = "회원가입에 실패했습니다. 다시 시도해주세요.";
      }
    };
  
    xhr.onerror = function() {
        errorMsg.textContent = "네트워크 오류가 발생했습니다. 다시 시도해주세요.";
    };
  
    xhr.send(JSON.stringify(userData));
  }
  
  // 로그인 처리 함수
  function login(username, password) {
      let loginData = {
          travelerName: username,
          password: password
      };
  
      let xhrLogin = new XMLHttpRequest();
      xhrLogin.open("POST", "/api/traveler/login", true);
      xhrLogin.setRequestHeader("Content-Type", "application/json");
  
      xhrLogin.onload = function () {
          if (xhrLogin.status === 200) {
              alert("로그인 성공!");
              window.location.href = "index.html"; // 로그인 후 메인 페이지로 이동
          } else {
              let errorMsg = document.getElementById("signupError");
              errorMsg.textContent = "로그인에 실패했습니다.";
              errorMsg.style.color = "red";
          }
      };
  
      xhrLogin.onerror = function () {
          let errorMsg = document.getElementById("signupError");
          errorMsg.textContent = "로그인 요청 오류.";
          errorMsg.style.color = "red";
      };
  
      xhrLogin.send(JSON.stringify(loginData)); // 로그인 요청 전송
  }
  