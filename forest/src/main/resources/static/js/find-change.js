document.addEventListener("DOMContentLoaded", function () {
    // 폼 전환 버튼 이벤트 추가
    document.getElementById("find-id").addEventListener("click", toggleFindForm);
    document.getElementById("find-pwd").addEventListener("click", toggleFindForm);
});

let generatedCode = null;  // 🔹 인증번호 저장용 변수

// 폼 전환 기능
function toggleFindForm() {
    const idForm = document.getElementById("id-form");
    const pwdForm = document.getElementById("pwd-form");

    if (document.getElementById("find-id").checked) {
        idForm.classList.add("active");
        pwdForm.classList.remove("active");
    } else {
        idForm.classList.remove("active");
        pwdForm.classList.add("active");
    }
}

function findID() {
    const name = document.getElementById("name").value;
    const phone = document.getElementById("phone").value;
    const email = document.getElementById("email").value;
    const resultElement = document.getElementById("id-result");

    if (!name || !phone || !email) {
        resultElement.innerText = "모든 필드를 입력해주세요!";
        resultElement.style.color = "red";
        return;
    }

    const foundUser = users.find(user => user.name === name && user.phone === phone && user.email === email);

    if (foundUser) {
        resultElement.innerText = `찾은 아이디: ${foundUser.userId}`;
        resultElement.style.color = "blue";
    } else {
        resultElement.innerText = "입력한 정보와 일치하는 아이디가 없습니다.";
        resultElement.style.color = "red";
    }
}

function sendVerification() {
    const userId = document.getElementById("userid").value;
    const phone = document.getElementById("phone-pwd").value;

    if (userId && phone) {
        generatedCode = Math.floor(100000 + Math.random() * 900000);  
        alert(`인증번호가 전송되었습니다! (테스트용: ${generatedCode})`);
    } else {
        alert("아이디와 전화번호를 입력해주세요.");
    }
}

function verifyCode() {
    const enteredCode = document.getElementById("verification-code").value;

    if (enteredCode === generatedCode.toString()) {
        alert("인증번호 확인 완료!");
    } else {
        alert("인증번호가 일치하지 않습니다.");
    }
}

function resetPassword() {
    const userId = document.getElementById("userid").value;
    const phone = document.getElementById("phone-pwd").value;
    const newPassword = document.getElementById("new-password").value;
    const confirmPassword = document.getElementById("confirm-password").value;
    const verificationCode = document.getElementById("verification-code");
    const enteredCode = verificationCode.value;

    if (!userId || !phone || !newPassword || !confirmPassword || !enteredCode) {
        alert("모든 정보를 입력해주세요.");
        return;
    }

    if (newPassword !== confirmPassword) {
        alert("새 비밀번호가 일치하지 않습니다.");
        return;
    }

    if (enteredCode !== generatedCode.toString()) {
        alert("인증번호가 일치하지 않습니다.");
        return;
    }

    let storedUser = localStorage.getItem(userId);
    if (!storedUser) {
        alert("아이디를 찾을 수 없습니다.");
        return;
    }

    let userData = JSON.parse(storedUser);

    userData.password = newPassword;

    localStorage.setItem(userId, JSON.stringify(userData));

    document.getElementById("pwd-result").innerText = "비밀번호 변경 완료!";

    userId.value = "";
    phone.value = "";
    newPassword.value = "";
    confirmPassword.value = "";
    verificationCode.value = "";

    generatedCode = null;

    alert("비밀번호 변경이 완료되었습니다. 다시 로그인 해주세요.");

    window.location.href = "login.html";
}

let generatedIDCode = null;  

function sendIDVerification() {
    const name = document.getElementById("name").value.trim();
    const phone = document.getElementById("phone").value.trim();
    const resultElement = document.getElementById("id-result");

    if (!name || !phone) {
        resultElement.innerText = "이름과 전화번호를 입력해주세요.";
        resultElement.style.color = "red";
        return;
    }
    
    generatedIDCode = Math.floor(100000 + Math.random() * 900000);
    alert(`인증번호가 전송되었습니다! (테스트용: ${generatedIDCode})`);
}

function verifyIDCode() {
    const enteredCode = document.getElementById("id-verification-code").value.trim();
    const resultElement = document.getElementById("id-result");
    
    if (!generatedIDCode) {
        resultElement.innerText = "먼저 인증번호를 발송해주세요.";
        resultElement.style.color = "red";
        return;
    }
    
    if (enteredCode === generatedIDCode.toString()) {
        alert("인증번호 확인 완료!");
        findUserID();  
    } else {
        resultElement.innerText = "인증번호가 일치하지 않습니다.";
        resultElement.style.color = "red";
    }
}

function findUserID() {
    const name = document.getElementById("name").value.trim();
    const phone = document.getElementById("phone").value.trim();
    const resultElement = document.getElementById("id-result");

    if (!name || !phone) {
        resultElement.innerText = "이름과 전화번호를 입력해주세요.";
        resultElement.style.color = "red";
        return;
    }
    
    let foundUser = null;
    for (let i = 0; i < localStorage.length; i++) {
        let key = localStorage.key(i);
        if (key === "emailVerified") continue;
        try {
            let userData = JSON.parse(localStorage.getItem(key));
            if (userData && userData.realName && userData.contact) {
                if (userData.realName === name && userData.contact === phone) {
                    foundUser = userData;
                    break;
                }
            }
        } catch(e) {
            continue;
        }
    }
    
    if (foundUser) {
        resultElement.innerText = `찾은 아이디: ${foundUser.username}`;
        resultElement.style.color = "blue";
    } else {
        resultElement.innerText = "입력한 정보와 일치하는 아이디가 없습니다.";
        resultElement.style.color = "red";
    }
}