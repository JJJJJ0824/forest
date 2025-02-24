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

// ✅ 이메일 인증 코드 전송
function sendEmailVerification() {
  let email = document.getElementById("signupEmail").value.trim();
  let message = document.getElementById("emailVerifyMessage");

  if (!email.includes("@") || !email.includes(".")) {
      message.textContent = "올바른 이메일 주소를 입력하세요.";
      message.style.color = "red";
      return;
  }

  // 📌 서버에서 인증 코드 전송 (현재는 123456 가상 코드 사용)
  let verificationCode = "123456"; 
  localStorage.setItem("emailVerificationCode", verificationCode);

  alert("이메일로 인증 코드가 전송되었습니다!");

  document.getElementById("emailCode").disabled = false;
  document.getElementById("verifyCodeBtn").disabled = false;
}

// ✅ 이메일 인증 코드 검증
function verifyEmailCode() {
  let inputCode = document.getElementById("emailCode").value.trim();
  let storedCode = localStorage.getItem("emailVerificationCode");
  let message = document.getElementById("emailCodeMessage");

  if (inputCode === storedCode) {
      message.textContent = "이메일 인증 완료!";
      message.style.color = "green";

      // 🔥 인증 완료 후 localStorage에서 인증 코드 삭제
      localStorage.removeItem("emailVerificationCode");

      // 🔥 인증 성공 상태 저장 (회원가입 버튼 클릭 시 검증)
      localStorage.setItem("emailVerified", "true");
  } else {
      message.textContent = "잘못된 인증 코드입니다.";
      message.style.color = "red";
  }
}

// ✅ 회원가입 함수
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

  if (!email.includes("@") || !email.includes(".")) {
      errorMsg.textContent = "올바른 이메일 형식을 입력하세요.";
      return;
  }

  if (!contact.match(/^01[0-9]-\d{3,4}-\d{4}$/)) {
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

  // 🔥 JSON 형식으로 사용자 정보 저장
  let userData = {
      username: username,
      realName: realName,
      email: email,
      contact: contact,
      password: password
  };

  localStorage.setItem(username, JSON.stringify(userData));

  alert("회원가입이 완료되었습니다!");
  window.location.href = "login.html"; // 로그인 페이지로 이동
}
