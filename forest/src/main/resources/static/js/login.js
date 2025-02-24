// 회원가입 함수 (signup.html에서 실행)
function register() {
  console.log("register() 실행됨!");

  let username = document.getElementById("signupUser").value.trim();
  let password = document.getElementById("signupPass").value.trim();
  let confirmPassword = document.getElementById("signupConfirm").value.trim();
  let errorMsg = document.getElementById("signupError");

  if (!username || !password || !confirmPassword) {
      errorMsg.textContent = "모든 필드를 입력하세요.";
      console.warn("모든 필드 입력 필요!");
      return;
  }

  if (password !== confirmPassword) {
      errorMsg.textContent = "비밀번호가 일치하지 않습니다.";
      console.warn("비밀번호 불일치!");
      return;
  }

  if (localStorage.getItem(username)) {
      errorMsg.textContent = "이미 존재하는 아이디입니다.";
      console.warn("중복된 아이디!");
      return;
  }

  localStorage.setItem(username, password);
  console.log("회원가입 성공!", username);
  alert("회원가입이 완료되었습니다!");
  window.location.href = "login.html"; // 로그인 페이지로 이동
}

// 로그인 함수 (login.html에서 실행)
function login() {
  console.log("login() 실행됨!");

  let username = document.getElementById("userid").value.trim();
  let password = document.getElementById("password").value.trim();
  let errorMsg = document.getElementById("loginError");

  if (!username || !password) {
      errorMsg.textContent = "아이디와 비밀번호를 입력하세요.";
      console.warn("모든 필수 사항 입력 필요!");
      return;
  }

  let storedPassword = localStorage.getItem(username);
  if (!storedPassword) {
      errorMsg.textContent = "존재하지 않는 아이디입니다.";
      console.warn("존재하지 않는 아이디!");
      return;
  }

  if (storedPassword !== password) {
      errorMsg.textContent = "비밀번호가 일치하지 않습니다.";
      console.warn("비밀번호 불일치!");
      return;
  }

  alert("로그인 성공!");
  console.log("로그인 성공:", username);
  window.location.href = "mypage.html"; // 로그인 후 마이페이지로 이동
  
  // 로그인한 사용자 정보 저장
      localStorage.setItem("loggedInUser", username);

  // 메인페이지로 이동
      window.location.href = "index.html";
}
