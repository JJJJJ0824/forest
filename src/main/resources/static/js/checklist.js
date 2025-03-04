document.addEventListener('DOMContentLoaded', function () {
  let currentQuestionIndex = 0;
  const questions = document.querySelectorAll('.checklist form div'); 
  const submitButton = document.getElementById('submit'); 
  const resultSection = document.getElementById('resultSection'); 
  const resultText = document.getElementById('resultText'); 
  const form = document.querySelector('form'); 
  const selectedAnswers = []; 

  fetchPreviousAnswers();

  questions[currentQuestionIndex].classList.add('active');

  document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
      checkbox.addEventListener('change', function () {
          const answerId = checkbox.id;
          if (checkbox.checked) {
              if (!selectedAnswers.includes(answerId)) {
                  selectedAnswers.push(answerId);
              }
          } else {
              const index = selectedAnswers.indexOf(answerId);
              if (index !== -1) {
                  selectedAnswers.splice(index, 1);
              }
          }

          if (selectedAnswers.length > 0 && currentQuestionIndex < questions.length - 1) {
              questions[currentQuestionIndex].classList.remove('active');

              currentQuestionIndex++;

              questions[currentQuestionIndex].classList.add('active');

              if (currentQuestionIndex === questions.length - 1) {
                  submitButton.style.display = 'block';
              }
              sendSelectedAnswerToServer(selectedAnswers);
          }
      });
  });

  submitButton.addEventListener('click', function (event) {
      event.preventDefault();

      if (selectedAnswers.length === questions.length) {
          const result = getResultType(selectedAnswers);
          resultText.textContent = `당신의 유형: ${result.type}`;

          const answersList = selectedAnswers.map(answer => {
              const questionText = getQuestionText(answer);
              return `<li>${questionText} : ${answer}</li>`;
          }).join('');
          resultSection.innerHTML = `
              <h3>결과</h3>
              <p>${result.type}</p>
              <ul>${answersList}</ul>
              <button id="btn-checklist-view">체크리스트 보기</button>
              <button id="btn-checklist-rewrite">체크리스트 재작성</button>
          `;
      } else {
          alert('모든 질문에 답변을 완료해 주세요.');
      }
  });

  function sendSelectedAnswerToServer(selectedAnswers) {
      const data = {
          answers: selectedAnswers  
      };

      fetch('/api/checklist/submit', {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json'
          },
          body: JSON.stringify(data)
      })
      .then(response => response.json())
      .then(data => {
          console.log('서버 응답:', data);
      })
      .catch(error => {
          console.error('에러 발생:', error);
      });
  }

  function getResultType(selectedAnswers) {
      let type = "자유여행"; 

      if (selectedAnswers.includes('q1-a1') && selectedAnswers.includes('q3-a2')) {
          type = "자유여행";
      }
      else if (selectedAnswers.includes('q2-a1') && selectedAnswers.includes('q4-a1')) {
          type = "패키지여행";
      }
      else if (selectedAnswers.includes('q2-a2') && selectedAnswers.includes('q5-a1')) {
          type = "가족여행";
      }

      return { type };
  }

  function getQuestionText(answerId) {
      const question = document.querySelector(`label[for="${answerId}"]`);
      return question ? question.textContent : "알 수 없는 질문";
  }

  document.getElementById('btn-checklist-rewrite').addEventListener('click', function () {
      const checkboxes = document.querySelectorAll('input[type="checkbox"]');
      checkboxes.forEach(checkbox => {
          checkbox.checked = false;
      });

      resultText.textContent = '';
      resultSection.innerHTML = '';
      submitButton.style.display = 'none'; 

      currentQuestionIndex = 0;
      selectedAnswers.length = 0;
      questions.forEach(question => {
          question.classList.remove('active');
      });

      questions[currentQuestionIndex].classList.add('active');
  });

  document.getElementById('btn-checklist-view').addEventListener('click', function () {
      const resultText = document.getElementById('resultText').textContent;
      alert(resultText || '선택된 항목이 없습니다.');
  });

  function fetchPreviousAnswers() {
      fetch('/api/checklist/me/check')  
      .then(response => response.json())
      .then(data => {
          if (data && data.answers) {
              data.answers.forEach(answer => {
                  selectedAnswers.push(answer);
                  const checkbox = document.getElementById(answer);
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
