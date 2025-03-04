document.addEventListener('DOMContentLoaded', function () {
  let currentQuestionIndex = 0;
  const questions = document.querySelectorAll('.checklist form div'); 
  const submitButton = document.getElementById('submit'); 
  const resultSection = document.getElementById('resultSection'); 
  const resultText = document.getElementById('resultText'); 
  const selectedAnswers = []; 

  fetchPreviousAnswers();

  questions[currentQuestionIndex].classList.add('active');

  // 각 체크박스 클릭 시 이벤트 리스너
  document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
      checkbox.addEventListener('change', function () {
          const answerId = checkbox.id;
          const questionText = checkbox.dataset.questionText; 
          const answerText = checkbox.dataset.answerText;

          if (checkbox.checked) {
              if (!selectedAnswers.some(item => item.answerId === answerId)) {
                  selectedAnswers.push({ answerId, questionText, answerText });
              }
          } else {
              const index = selectedAnswers.findIndex(item => item.answerId === answerId);
              if (index !== -1) {
                  selectedAnswers.splice(index, 1);
              }
          }

          // 답변 제출 후, 자동으로 다음 질문으로 넘어가기
          if (selectedAnswers.length > 0 && currentQuestionIndex < questions.length - 1) {
              questions[currentQuestionIndex].classList.remove('active');
              currentQuestionIndex++;
              questions[currentQuestionIndex].classList.add('active');

              // 마지막 질문에서만 '결과 보기' 버튼 표시
              if (currentQuestionIndex === questions.length - 1) {
                  submitButton.style.display = 'block';
              }

              // 선택된 답변을 서버로 보내기
              sendSelectedAnswerToServer(selectedAnswers[selectedAnswers.length - 1]);
          }
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
              <button id="btn-checklist-view" class="active">체크리스트 보기</button>
              <button id="btn-checklist-rewrite" class="active">체크리스트 재작성</button>
          `;
      } else {
          alert('모든 질문에 답변을 완료해 주세요.');
      }
  });

  function getQuestionText(checkbox) {
    // 체크박스가 속한 질문의 id를 찾기
    const questionId = checkbox.closest('div[id^="question"]').id; // question1, question2 등
    const questionElement = document.getElementById(questionId);
    return questionElement.querySelector('p').textContent; // 질문 텍스트 추출
  }

  function getAnswerText(answerId) {
    const label = document.querySelector(`label[for="${answerId}"]`);
    if (label) {
        return label.textContent.trim(); 
    } else {
        return `답변을 찾을 수 없습니다 (id: ${answerId})`;
    }
  }

  function sendSelectedAnswerToServer(answer) {
    const questionText = getQuestionText(answer.answerId);  
    const answerText = getAnswerText(answer.answerId);   
    const travelerName = currentUserData.travelerName; 
    const categoryName = getResultType(selectedAnswers).type;  

    console.log('보내는 데이터:', {
        answerId: answer.answerId,
        questionText: questionText,
        answerText: answerText,
        traveler: travelerName,  
        category: categoryName  
    });

    fetch('/api/checklist/submit', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            direction: questionText,
            response: answerText,
            isChecked: true,
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
        console.log('서버 응답:', data);
    })
    .catch(error => {
        console.error('에러 발생:', error);
    });
  }

  // 결과 유형 계산
  function getResultType(selectedAnswers) {
      let type = "자유여행"; 

      if (selectedAnswers.some(answer => answer.answerId === 'q1-a1') && selectedAnswers.some(answer => answer.answerId === 'q3-a2')) {
          type = "자유여행";
      } else if (selectedAnswers.some(answer => answer.answerId === 'q2-a1') && selectedAnswers.some(answer => answer.answerId === 'q4-a1')) {
          type = "패키지여행";
      } else if (selectedAnswers.some(answer => answer.answerId === 'q2-a2') && selectedAnswers.some(answer => answer.answerId === 'q5-a1')) {
          type = "가족여행";
      }

      return { type };
  }

  // 이전 답변 가져오기
  function fetchPreviousAnswers() {
      fetch('/api/checklist/me/check')
      .then(response => response.json())
      .then(data => {
          if (data && data.answers) {
              data.answers.forEach(answer => {
                  selectedAnswers.push(answer);
                  const checkbox = document.getElementById(answer.answerId);
                  if (checkbox) {
                      checkbox.checked = true;
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
