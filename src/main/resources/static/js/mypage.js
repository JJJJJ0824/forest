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
    console.log("mypage.js 로드됨!");

});

function getLoggedInUser() {
    fetch('/api/traveler/mypage', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include' 
    })
    .then(response => response.json())
    .then(data => {
        if (data && data.success) {

            displayUserInfo(data.userData);
        } else {
            alert("로그인이 필요합니다!");
            window.location.href = "login.html";
        }
    })
    .catch(error => {
        console.error("로그인 상태 확인 실패:", error);
        alert("마이페이지를 로딩하는 중 오류가 발생했습니다.");
        window.location.href = "login.html";
    });
}


function displayUserInfo(userData) {
    document.getElementById("userName").textContent = userData.realName || "이름 없음";
    document.getElementById("userPhone").textContent = userData.contact || "연락처 없음";
    document.getElementById("userEmail").textContent = userData.email || "이메일 없음";
    
}
