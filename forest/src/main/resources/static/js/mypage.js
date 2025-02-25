const buttons = document.querySelectorAll('button#btn-info, button#btn-checklist, button#btn-courses');
const leftSide = document.getElementById('left-side');
const center = document.getElementById('center');
const rightSide = document.getElementById('right-side');

buttons.forEach(button => {
    button.addEventListener('click', function() {
        buttons.forEach(btn => btn.classList.remove('active'));
        
        this.classList.add('active');
        
        leftSide.classList.remove('active');
        center.classList.remove('active');
        rightSide.classList.remove('active');
        
        if (this.id === 'btn-info') {
            leftSide.classList.add('active');
        } else if (this.id === 'btn-checklist') {
            center.classList.add('active');
        } else if (this.id === 'btn-courses') {
            rightSide.classList.add('active');
        }
    });
});
document.addEventListener("DOMContentLoaded", function () {
    console.log("✅ mypage.js 로드됨!");

    let loggedInUser = localStorage.getItem("loggedInUser");

    if (!loggedInUser || loggedInUser === "null" || loggedInUser === "undefined" || loggedInUser.trim() === "") {
        alert("로그인이 필요합니다!");
        window.location.href = "login.html";
        return;
    }

    let userData = JSON.parse(localStorage.getItem(loggedInUser));

    if (!userData) {
        alert("로그인 정보가 올바르지 않습니다. 다시 로그인해주세요.");
        localStorage.removeItem("loggedInUser");
        window.location.href = "login.html";
        return;
    }

    document.getElementById("userName").textContent = userData.realName || "이름 없음";
    document.getElementById("userPhone").textContent = userData.contact || "연락처 없음";
    document.getElementById("userEmail").textContent = userData.email || "이메일 없음";
    

    

    let logoutButton = document.getElementById("logoutButton");
    if (logoutButton) {
        logoutButton.addEventListener("click", logout);
    }
});

function logout() {
    console.log("로그아웃 실행됨!");
    
    localStorage.removeItem("loggedInUser");
    
    alert("로그아웃 되었습니다!");
    
    window.location.href = "login.html";
}