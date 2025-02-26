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

    // Ajax 요청을 통해 사용자 데이터 가져오기
    fetchUserData(loggedInUser);
});

// Ajax 요청을 통해 사용자 정보를 서버에서 가져오는 함수
function fetchUserData(userId) {
    // 예시로 GET 방식으로 서버에서 데이터를 가져오는 방식입니다.
    fetch(`/api/traveler/${userId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('사용자 정보를 가져오는 데 실패했습니다.');
            }
            return response.json();
        })
        .then(userData => {
            // 성공적으로 데이터를 받으면 사용자 정보를 페이지에 표시
            document.getElementById("userName").textContent = userData.realName || "이름 없음";
            document.getElementById("userPhone").textContent = userData.contact || "연락처 없음";
            document.getElementById("userEmail").textContent = userData.email || "이메일 없음";
        })
        .catch(error => {
            console.error(error);
            alert("사용자 정보를 불러오는 중 오류가 발생했습니다.");
            localStorage.removeItem("loggedInUser");
            window.location.href = "login.html";
        });
}

let logoutButton = document.getElementById("logoutButton");
if (logoutButton) {
    logoutButton.addEventListener("click", logout);
}

function logout() {
    console.log("로그아웃 실행됨!");
    
    localStorage.removeItem("loggedInUser");
    
    alert("로그아웃 되었습니다!");
    
    window.location.href = "login.html";
}
