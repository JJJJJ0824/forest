document.addEventListener("DOMContentLoaded", function () {
    console.log("mypage.js 로드됨!");

    const buttons = document.querySelectorAll('button#btn-info, button#btn-checklist, button#btn-courses');
    const leftSide = document.getElementById('left-side');
    const center = document.getElementById('center');
    const rightSide = document.getElementById('right-side');

    const editBtn = document.querySelector('.edit-btn');
    const saveBtn = document.getElementById('saveChanges');
    const cancelBtn = document.getElementById('cancelEdit');
    const editForm = document.getElementById('editForm');
    const editNameInput = document.getElementById('editNameInput');
    const editPhoneInput = document.getElementById('editPhoneInput');
    const editEmailInput = document.getElementById('editEmailInput');

    let currentUserData = {};

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

    getLoggedInUser();

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
            if (data && data !== "") {
                displayUserInfo(data);
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
        currentUserData = userData;

        document.getElementById("userName").textContent = userData.realName || "이름 없음";
        document.getElementById("userPhone").textContent = userData.contact || "연락처 없음";
        document.getElementById("userEmail").textContent = userData.email || "이메일 없음";
        
        editNameInput.value = userData.realName || '';
        editPhoneInput.value = userData.contact || '';
        editEmailInput.value = userData.email || '';
    }

    editBtn.addEventListener('click', function () {
        editForm.style.display = 'block';
        editNameInput.value = currentUserData.realName || '';
        editPhoneInput.value = currentUserData.contact || ''; 
        editEmailInput.value = currentUserData.email || '';
    });

    saveBtn.addEventListener('click', function () {
        const updatedData = {
            contact: editPhoneInput.value,
            email: editEmailInput.value
        };

        fetch('/api/traveler/update', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(updatedData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.contact || data.email) {
                alert("정보가 성공적으로 수정되었습니다!");
                currentUserData = updatedData; 
                displayUserInfo(updatedData);  
                editForm.style.display = 'none'; 
            } else {
                alert("정보 수정에 실패했습니다. 다시 시도해주세요.");
            }
        })
        .catch(error => {
            console.error("정보 수정 실패:", error);
            alert("정보 수정 중 오류가 발생했습니다.");
        });
    });

    cancelBtn.addEventListener('click', function () {
        editForm.style.display = 'none'; 
    });
});

