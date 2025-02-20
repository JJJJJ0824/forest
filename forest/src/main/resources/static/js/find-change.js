function toggleFindForm() {
   let idForm = document.getElementById("id-form");
   let pwdForm = document.getElementById("pwd-form");

   if (document.getElementById("find-id").checked) {
       idForm.classList.add("active");
       pwdForm.classList.remove("active");
   } else {
       idForm.classList.remove("active");
       pwdForm.classList.add("active");
   }
}

function findID() {
   let name = document.getElementById("name").value;
   let phone = document.getElementById("phone").value;
   let email = document.getElementById("email").value;

   if (name === "" || phone === "" || email === "") {
       alert("모든 필드를 입력하세요!");
       return;
   }

   alert("입력된 정보 확인 중...");
   setTimeout(() => {
       alert(`당신의 아이디는 ${name}_user 입니다!`);
   }, 2000);
}

function sendVerification() {
   let userID = document.getElementById("userid").value;
   let phone = document.getElementById("phone-pwd").value;

   if (userID === "" || phone === "") {
       alert("모든 필드를 입력하세요!");
       return;
   }

   alert("인증번호가 전송되었습니다!");
}

function resetPassword() {
   let newPassword = document.getElementById("new-password").value;
   let confirmPassword = document.getElementById("confirm-password").value;

   if (newPassword === "" || confirmPassword === "") {
       alert("모든 필드를 입력하세요!");
       return;
   }

   if (newPassword !== confirmPassword) {
       alert("비밀번호가 일치하지 않습니다!");
       return;
   }

   alert("비밀번호가 변경되었습니다!");
}