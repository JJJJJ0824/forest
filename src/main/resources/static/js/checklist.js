document.addEventListener('DOMContentLoaded', function () {
  // 사용자 정보가 없으면 백엔드에서 불러온 후 콜백 실행
  loadUserInfoIfNeeded(function () {
    console.log("checklist.js 로드됨! 사용자 정보 로드 완료");

    let currentQuestionIndex = 0;
    const questions = document.querySelectorAll('.checklist form div');
    const submitButton = document.getElementById('submit');
    const resultSection = document.getElementById('resultSection');
    const resultText = document.getElementById('resultText');
    const selectedAnswers = [];

    fetchPreviousAnswers();

    document.querySelectorAll('input[type="radio"]').forEach(radio => {
      radio.addEventListener('change', function () {
        const questionId = radio.name;         
        const answerText = radio.value;          
        const questionText = getQuestionText(questionId);

        console.log("Answer selected:", answerText);

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

        if (currentQuestionIndex < questions.length - 1) {
          questions[currentQuestionIndex].classList.remove('active');
          currentQuestionIndex++;
          questions[currentQuestionIndex].classList.add('active');

          if (currentQuestionIndex === questions.length - 1) {
            submitButton.style.display = 'block';
          }
        }

        sendSelectedAnswerToServer();
      });
    });

    submitButton.addEventListener('click', function (event) {
      event.preventDefault();
    
      const allAnswered = selectedAnswers.length === questions.length &&
        selectedAnswers.every(answer => answer.answerText !== undefined && answer.answerText !== '');
    
      if (allAnswered) {
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
    
        const checklistButton = document.getElementById('btn-checklist');
        const rewriteButton = document.getElementById('btn-checklist-rewrite');
        checklistButton.classList.add('active');
        rewriteButton.classList.add('active');
      } else {
        alert('모든 질문에 답변을 완료해 주세요.');
      }
    });
    

    function getQuestionText(questionId) {
      const questionElement = document.querySelector(`#${questionId} p`);
      if (!questionElement) {
        console.error(`해당 questionElement를 찾을 수 없습니다: ${questionId}`);
        return null;
      }
      return questionElement.textContent.trim();
    }

    function sendSelectedAnswerToServer() {
      const travelerName = window.currentUser.travelerName;
      const categoryName = getResultType(selectedAnswers).type;
    
      const lastAnswer = selectedAnswers[selectedAnswers.length - 1]; 
      if (!lastAnswer) {
        console.error("🚨 선택된 답변이 없습니다!");
        return;
      }
    
      fetch('/api/checklist/submit', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          direction: lastAnswer.questionText, 
          response: lastAnswer.answerText,
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

    function fetchPreviousAnswers() {
      fetch('/api/checklist/me/check')
        .then(response => response.json())
        .then(data => {
          console.log("📌 서버에서 받은 데이터:", data);
          
          if (!Array.isArray(data)) {
            console.error("❌ 서버 응답이 배열이 아님:", data);
            return;
          }
    
          let lastAnsweredIndex = 0; 
    
          data.forEach((item, index) => {
            const questionText = item.direction;
            const answerText = item.response;
    
            console.log(`🔍 questionText: ${questionText}, answerText: ${answerText}`);
    
            if (questionText) {
              selectedAnswers.push({ questionText, answerText });
    
              const radio = document.querySelector(`input[value="${answerText}"]`);
              if (radio) {
                radio.checked = true;
    
                lastAnsweredIndex = index;
              }
            } else {
              console.warn(`⚠️ 질문을 찾을 수 없음: ${questionText}`);
            }
          });
    
          updateQuestionView(lastAnsweredIndex);
        })
        .catch(error => {
          console.error('🚨 이전 답변 로딩 중 에러 발생:', error);
        });
    }
    
    function updateQuestionView(index) {
      const questions = document.querySelectorAll('.checklist form div');
      questions.forEach((q, i) => {
        q.classList.toggle('active', i === index);
      });
    
      currentQuestionIndex = index; 
    }
    
    
  });
});

function loadUserInfoIfNeeded(callback) {
  if (window.currentUser) {
    callback();
  } else {
    window.location.href = "login.html";
  }
}