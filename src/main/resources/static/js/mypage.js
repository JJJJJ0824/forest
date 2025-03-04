document.addEventListener("DOMContentLoaded", function () {
    console.log("mypage.js 로드됨!");

    const buttons = document.querySelectorAll('button#btn-info, button#btn-checklist, button#btn-courses');
    const leftSide = document.getElementById('left-side');
    const center = document.getElementById('center');
    const rightSide = document.getElementById('right-side');

    let currentUserData = {};  

    getLoggedInUser();

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

    function getLoggedInUser() {
        fetch('/api/traveler/mypage', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
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
    }

    // function loadUserChecklistData() {
    //     fetch('/api/checklist/me/check')
    //         .then(response => response.json())
    //         .then(data => {
    //             data.forEach(item => {
    //                 const checkbox = document.querySelector(`input[type="checkbox"][name="${item.direction}"]`);
    //                 if (checkbox) {
    //                     checkbox.checked = item.isChecked;
    //                     userResponses[item.direction] = item.isChecked;
    //                 }
    //                 console.log(checkbox);
    //             });
    //         })
    //         .catch(error => console.error('Error loading checklist data:', error));
    // }

    // document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
    //     checkbox.addEventListener('change', function () {
    //         const questionId = this.id.split('-')[0];  
    //         const checkboxes = document.querySelectorAll(`#${questionId} input[type="checkbox"]`);

    //         checkboxes.forEach(checkbox => {
    //             if (checkbox !== this) {
    //                 checkbox.checked = false;
    //             }
    //         });

    //         const checklistData = {
    //             id: this.id,
    //             travelerName: currentUserData.travelerName,
    //             direction: this.name,
    //             isChecked: this.checked
    //         };

    //         fetch('/api/checklist/update', {
    //             method: 'PUT',
    //             headers: { 'Content-Type': 'application/json' },
    //             body: JSON.stringify(checklistData)
    //         })
    //         .then(response => response.json())
    //         .then(data => console.log('Checklist updated:', data))
    //         .catch(error => console.error('Error updating checklist:', error));
    //     });
    // });

    // function checkIfChecklistCompleted() {
    //     const allAnswered = Object.keys(userResponses).length === questions.length;
    //     const isLastQuestion = currentQuestionIndex === questions.length - 1;

    //     if (allAnswered && isLastQuestion) {
    //         submitButton.disabled = false;
    //         checklistViewButton.disabled = false;
    //         checklistRewriteButton.disabled = false;
    //     } else {
    //         submitButton.disabled = true;
    //         checklistViewButton.disabled = true;
    //         checklistRewriteButton.disabled = true;
    //     }
    // }

    // const handleNextQuestion = () => {
    //     if (currentQuestionIndex < questions.length - 1) {
    //         currentQuestionIndex++;
    //         updateQuestionDisplay();
    //     }
    // };

    // const updateQuestionDisplay = () => {
    //     questions.forEach((question, index) => {
    //         if (index === currentQuestionIndex) {
    //             question.classList.add("active");
    //         }
    //     });

    //     const progress = ((currentQuestionIndex) / questions.length) * 100;
    //     progressBar.value = isNaN(progress) || progress < 0 || progress > 100 ? 0 : progress;
    // };

    // submitButton.addEventListener("click", function (e) {
    //     e.preventDefault();

    //     if (currentQuestionIndex === questions.length - 1) {
    //         const userType = calculateUserType(userResponses);
    //         resultText.textContent = `당신의 유형은: ${userType}입니다.`;

    //         sendChecklistDataToServer(userResponses);
    //     }
    // });

    // function calculateUserType(responses) {
    //     let familyPoints = 0;
    //     let freePoints = 0;
    //     let packagePoints = 0;

    //     if (responses['q4'] === '가족 (아이들과 함께)' || responses['q1'] === '휴식과 여유') {
    //         familyPoints++;
    //     }

    //     if (responses['q3'] === '완전 자유 여행 (자유롭게 일정 조정)') {
    //         freePoints++;
    //     }

    //     if (responses['q3'] === '패키지 여행 (사전 계획된 일정)') {
    //         packagePoints++;
    //     }

    //     if (familyPoints > freePoints && familyPoints > packagePoints) {
    //         return "가족 유형";
    //     } else if (freePoints > familyPoints && freePoints > packagePoints) {
    //         return "자유 유형";
    //     } else if (packagePoints > familyPoints && packagePoints > freePoints) {
    //         return "패키지 유형";
    //     }

    //     return "자유 유형";
    // }

    // function sendChecklistDataToServer(userResponses) {
    //     const url = '/api/checklist/submit';
    //     const data = {
    //         userResponses: userResponses,
    //         userType: calculateUserType(userResponses)
    //     };

    //     fetch(url, {
    //         method: 'POST',
    //         headers: { 'Content-Type': 'application/json' },
    //         body: JSON.stringify(data)
    //     })
    //     .then(response => response.json())
    //     .then(data => console.log('Success:', data))
    //     .catch((error) => {
    //         console.error('Error:', error);
    //     });
    // }

});
