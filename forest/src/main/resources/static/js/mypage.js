const buttons = document.querySelectorAll('button#btn-info, button#btn-checklist, button#btn-courses');
const leftSide = document.getElementById('left-side');
const center = document.getElementById('center');
const rightSide = document.getElementById('right-side');

// 버튼 클릭 시 active 클래스 토글
buttons.forEach(button => {
    button.addEventListener('click', function() {
        // 모든 버튼에서 'active' 클래스 제거
        buttons.forEach(btn => btn.classList.remove('active'));
        
        // 클릭된 버튼에 'active' 클래스 추가
        this.classList.add('active');
        
        // 모든 섹션 숨기기
        leftSide.classList.remove('active');
        center.classList.remove('active');
        rightSide.classList.remove('active');
        
        // 클릭된 버튼에 해당하는 섹션만 보이게 하기
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

    // 🔥 로그인되지 않은 경우 (null, 빈 문자열 "", undefined 포함)
    if (!loggedInUser || loggedInUser === "null" || loggedInUser === "undefined" || loggedInUser.trim() === "") {
        alert("로그인이 필요합니다!");
        window.location.href = "login.html";
        return; // 🚀 추가: 로그인 필요 시 이후 코드 실행 방지
    }

    let userData = JSON.parse(localStorage.getItem(loggedInUser));

    if (!userData) { // 🔥 localStorage에 해당 사용자 데이터가 없을 경우 로그아웃 처리
        alert("로그인 정보가 올바르지 않습니다. 다시 로그인해주세요.");
        localStorage.removeItem("loggedInUser");
        window.location.href = "login.html";
        return;
    }

    // ✅ "내 정보" 페이지에 사용자 정보 반영
    document.getElementById("userName").textContent = userData.realName || "이름 없음";
    document.getElementById("userPhone").textContent = userData.contact || "연락처 없음";
    document.getElementById("userEmail").textContent = userData.email || "이메일 없음";
    

    

    // 로그아웃 버튼 클릭 이벤트 추가
    let logoutButton = document.getElementById("logoutButton");
    if (logoutButton) {
        logoutButton.addEventListener("click", logout);
    }
});

// ✅ 로그아웃 함수
function logout() {
    console.log("로그아웃 실행됨!");
    
    // 로그인 정보 삭제
    localStorage.removeItem("loggedInUser");
    
    alert("로그아웃 되었습니다!");
    
    // 로그인 페이지로 이동
    window.location.href = "login.html";
}