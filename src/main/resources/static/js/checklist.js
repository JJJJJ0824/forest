// document.addEventListener('DOMContentLoaded', function() {
//     const questions = document.querySelectorAll("#question1", "#question2", "#question3", "#question4"
//         ,"#question5", "#question6", "#question7", "#question8", "#question9", "#question10");
//     const submitButton = document.querySelector('button[type="submit"]'); 
//     const progressBar = document.querySelector("progress");
//     const resultText = document.getElementById("resultText");
//     const checklistViewButton = document.getElementById("btn-checklist-view");
//     const checklistRewriteButton = document.getElementById("btn-checklist-rewrite");
//     const resultSection = document.getElementById("resultSection"); 

//     let userResponses = {}; 
//     let currentQuestionIndex = 0; 

//     getLoggedInUser();

//     function getLoggedInUser() {
//         fetch('/api/traveler/mypage', {
//             method: 'GET',
//             headers: { 'Content-Type': 'application/json' },
//             credentials: 'include'
//         })
//         .then(response => response.json())
//         .then(data => {
//             if (data && data !== "") {
//                 displayUserInfo(data);
//             } else {
//                 alert("로그인이 필요합니다!");
//                 window.location.href = "login.html";
//             }
//         })
//         .catch(error => {
//             console.error("로그인 상태 확인 실패:", error);
//             alert("마이페이지를 로딩하는 중 오류가 발생했습니다.");
//             window.location.href = "login.html";
//         });
//     }

//     // 질문을 화면에 표시하고, 진행 상황을 업데이트하는 함수
//     const updateQuestionDisplay = () => {
//         questions.forEach((question, index) => {
//             if (index === currentQuestionIndex) {
//                 question.classList.add("active");
//             } else {
//                 question.classList.remove("active");
//             }
//         });

//         // 진행 상태 업데이트
//         const progress = ((currentQuestionIndex) / questions.length) * 100;
//         progressBar.value = isNaN(progress) || progress < 0 || progress > 100 ? 0 : progress;

//         // 마지막 질문에 도달한 경우
//         if (currentQuestionIndex === questions.length - 1) {
//             resultSection.style.display = "block";  // 결과 화면 보이기
//             submitButton.style.display = "block";   // 제출 버튼 보이기
//             questions.forEach((question) => question.style.display = "none");  // 모든 질문 숨기기
//         }
//     };

//     resultSection.style.display = "none";
//     submitButton.style.display = "none";

//     const radioButtons = document.querySelectorAll("input[type='radio']");
//     radioButtons.forEach(button => {
//         button.addEventListener("change", function() {
//             const questionName = this.name;
//             const selectedOption = this.value;

//             userResponses[questionName] = selectedOption;

//             handleNextQuestion();
//             checkIfChecklistCompleted();
//         });
//     });

//     const handleNextQuestion = () => {
//         if (currentQuestionIndex < questions.length - 1) {
//             currentQuestionIndex++;
//             updateQuestionDisplay();
//         }
//     };

//     submitButton.addEventListener("click", function(e) {
//         e.preventDefault(); 

//         if (currentQuestionIndex === questions.length - 1) {
//             const userType = calculateUserType(userResponses);
//             resultText.textContent = `당신의 유형은: ${userType}입니다.`; 

//             // 서버로 AJAX 요청 보내기
//             sendChecklistDataToServer(userResponses);
//         }
//     });

//     function sendChecklistDataToServer(userResponses) {
//         const url = '/api/checklist/';
//         const data = {
//             userResponses: userResponses,
//             userType: calculateUserType(userResponses)  // 유저 유형도 전송할 수 있습니다
//         };

//         // AJAX 요청 생성 (fetch 사용)
//         fetch(url, {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(data)
//         })
//         .then(response => response.json())
//         .then(data => {
//             console.log('Success:', data);
//             resultText.textContent = '체크리스트가 서버에 저장되었습니다.';
//         })
//         .catch((error) => {
//             console.error('Error:', error);
//             resultText.textContent = '서버에 저장하는데 문제가 발생했습니다.';
//         });
//     }

//     function calculateUserType(responses) {
//         let familyPoints = 0;
//         let freePoints = 0;
//         let packagePoints = 0;

//         if (responses['q4'] === '가족 (아이들과 함께)' || responses['q1'] === '휴식과 여유' || responses['q2'] === '가족 친화적인 호텔') {
//             familyPoints++;
//         }

//         if (responses['q3'] === '완전 자유 여행 (자유롭게 일정 조정)' || responses['q5'] === '자유 일정') {
//             freePoints++;
//         }

//         if (responses['q3'] === '패키지 여행 (사전 계획된 일정)') {
//             packagePoints++;
//         }

//         if (familyPoints > freePoints && familyPoints > packagePoints) {
//             return "가족 유형";
//         } else if (freePoints > familyPoints && freePoints > packagePoints) {
//             return "자유 유형";
//         } else if (packagePoints > familyPoints && packagePoints > freePoints) {
//             return "패키지 유형";
//         }

//         return "자유 유형"; 
//     }

//     function checkIfChecklistCompleted() {
//         const allAnswered = Object.keys(userResponses).length === questions.length; 
//         const isLastQuestion = currentQuestionIndex === questions.length - 1; 

//         if (allAnswered && isLastQuestion) {
//             submitButton.disabled = false;
//             checklistViewButton.disabled = false; 
//             checklistRewriteButton.disabled = false; 
//         } else {
//             submitButton.disabled = true;
//             checklistViewButton.disabled = true; 
//             checklistRewriteButton.disabled = true; 
//         }
//     }

//     // 페이지 로딩 시 서버에서 데이터를 불러옴
//     loadUserDataFromServer();
// });
