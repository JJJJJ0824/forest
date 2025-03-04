document.addEventListener('DOMContentLoaded', function () {
  // 사용자 정보가 없으면 백엔드에서 불러온 후 콜백 실행
  loadUserInfoIfNeeded(function () {
    console.log("checklist.js 로드됨! 사용자 정보 로드 완료");

    let currentQuestionIndex = 0;
    const questions = document.querySelectorAll('.checklist form div');
    const submitButton = document.getElementById('submit');
    const resultSection = document.getElementById('resultSection');
    const resultText = document.getElementById('resultText');
    // 각 질문의 답변 정보를 저장 (questionId, questionText, answerText)
    const selectedAnswers = [];

    // 이전 답변 불러오기 (백엔드가 키/값 객체 형태로 응답하는 경우)
    fetchPreviousAnswers();

    // 라디오 버튼 선택 시 이벤트 등록
    document.querySelectorAll('input[type="radio"]').forEach(radio => {
      radio.addEventListener('change', function () {
        const questionId = radio.name;          // 예: "q1"
        const answerText = radio.value;           // 예: "휴식과 여유"
        const questionText = getQuestionText(questionId); // 해당 질문의 텍스트

        console.log("Answer selected:", answerText);

        // 이미 해당 질문에 대한 답변이 있으면 업데이트, 없으면 새로 추가
        const existingIndex = selectedAnswers.findIndex(item => item.questionId === questionId);
        if (existingIndex !== -1) {
          selectedAnswers[existingIndex].answerText = answerText;
        } else {
          selectedAnswers.push({ 
            questionId: questionId, 
            questionText: questionText, 
            answerText: answerText 
          });
        }

        // 자동으로 다음 질문으로 이동
        if (currentQuestionIndex < questions.length - 1) {
          questions[currentQuestionIndex].classList.remove('active');
          currentQuestionIndex++;
          questions[currentQuestionIndex].classList.add('active');

          // 마지막 질문이면 '결과 보기' 버튼 표시
          if (currentQuestionIndex === questions.length - 1) {
            submitButton.style.display = 'block';
          }
        }

        // 선택한 답변을 서버로 전송
        sendSelectedAnswerToServer();
      });
    });

    // '결과 보기' 버튼 클릭 시
    submitButton.addEventListener('click', function (event) {
      event.preventDefault();

      if (selectedAnswers.length === questions.length) {
        const result = getResultType(selectedAnswers);
        resultText.textContent = `당신의 유형: ${result.type}`;

        const answersList = selectedAnswers.map(item => {
          return `<li>${item.questionText} : ${item.answerText}</li>`;
        }).join('');
        resultSection.innerHTML = `
          <h3>결과</h3>
          <p>${result.type}</p>
          <ul>${answersList}</ul>
        `;

        // 결과 후 체크리스트 버튼 활성화
        const checklistButton = document.getElementById('btn-checklist');
        const rewriteButton = document.getElementById('btn-checklist-rewrite');
        checklistButton.classList.add('active');
        rewriteButton.classList.add('active');
      } else {
        alert('모든 질문에 답변을 완료해 주세요.');
      }
    });

    // 각 질문의 텍스트를 가져오는 함수 (각 div 내의 p 태그 사용)
    function getQuestionText(questionId) {
      const questionElement = document.querySelector(`#${questionId} p`);
      if (!questionElement) {
        console.error(`해당 questionElement를 찾을 수 없습니다: ${questionId}`);
        return null;
      }
      return questionElement.textContent.trim();
    }

    // 선택한 답변들을 서버로 전송하는 함수
    function sendSelectedAnswerToServer() {
      const travelerName = window.currentUser.travelerName || window.currentUser.realName;
      const categoryName = getResultType(selectedAnswers).type;
    
      // 🔹 단일 객체에서 direction, response 추출
      const lastAnswer = selectedAnswers[selectedAnswers.length - 1]; // 마지막으로 선택한 답변
      if (!lastAnswer) {
        console.error("🚨 선택된 답변이 없습니다!");
        return;
      }
    
      fetch('/api/checklist/submit', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          direction: lastAnswer.questionText, // 질문 내용
          response: lastAnswer.answerText, // 사용자가 선택한 답변
          checked: true,
          traveler: travelerName,
          category: categoryName
        })
      })
      .then(response => {
        if (!response.ok) {
          throw new Error(`서버 오류: ${response.statusText}`);
        }
        return response.json();
      })
      .then(data => {
        console.log('✅ 서버 응답:', data);
      })
      .catch(error => {
        console.error('🚨 에러 발생:', error);
      });
    }

    // 결과 유형 계산 함수 (조건에 따라 수정하세요)
    function getResultType(selectedAnswers) {
      let type = "자유여행";

      if (
        selectedAnswers.some(answer => answer.questionId === 'q1' && answer.answerText === '휴식과 여유') &&
        selectedAnswers.some(answer => answer.questionId === 'q3' && answer.answerText === '패키지 여행')
      ) {
        type = "자유여행";
      } else if (
        selectedAnswers.some(answer => answer.questionId === 'q2' && answer.answerText === '럭셔리 리조트') &&
        selectedAnswers.some(answer => answer.questionId === 'q4' && answer.answerText === '가족 (아이들과 함께)')
      ) {
        type = "패키지여행";
      } else if (
        selectedAnswers.some(answer => answer.questionId === 'q2' && answer.answerText === '가족 친화적인 호텔') &&
        selectedAnswers.some(answer => answer.questionId === 'q5' && answer.answerText === '완전 고정 일정')
      ) {
        type = "가족여행";
      }

      return { type };
    }

    // 이전 답변을 백엔드에서 불러오는 함수  
    // 백엔드가 각 질문의 키/값 객체로 응답한다고 가정합니다.
    function fetchPreviousAnswers() {
      fetch('/api/checklist/me/check')
        .then(response => response.json())
        .then(data => {
          if (data) {
            // data가 객체 형태라면 각 키(q1, q2, …)를 순회합니다.
            Object.keys(data).forEach(key => {
              if (key === 'lastQuestionIndex') return;
              const questionId = key;
              const answerText = data[key];
              const questionText = getQuestionText(questionId);
              if (questionText) {
                selectedAnswers.push({
                  questionId: questionId,
                  questionText: questionText,
                  answerText: answerText
                });
                // 라디오 버튼 상태 업데이트
                const radio = document.querySelector(`input[name="${questionId}"][value="${answerText}"]`);
                if (radio) {
                  radio.checked = true;
                }
              }
            });

            currentQuestionIndex = data.lastQuestionIndex || 0;
            questions.forEach((question, index) => {
              if (index <= currentQuestionIndex) {
                question.classList.add('active');
              } else {
                question.classList.remove('active');
              }
            });
          }
        })
        .catch(error => {
          console.error('이전 답변 로딩 중 에러 발생:', error);
        });
    }
  });
});

// 사용자 정보가 없는 경우 백엔드에서 직접 가져오는 함수
function loadUserInfoIfNeeded(callback) {
  if (window.currentUser) {
    callback();
  } else {
    fetch('/api/traveler/mypage', {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    })
    .then(response => {
      if (response.status === 401) {
        alert("세션이 만료되었습니다. 다시 로그인해 주세요!");
        window.location.href = "login.html";
        throw new Error("로그인 필요");
      }
      if (!response.ok) throw new Error("사용자 정보 로드 실패");
      return response.json();
    })
    .then(data => {
      window.currentUser = data;
      callback();
    })
    .catch(error => {
      console.error("사용자 정보 로드 실패:", error);
    });
  }
}
