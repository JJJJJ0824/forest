document.addEventListener('DOMContentLoaded', function () {
  let currentQuestionIndex = 0; // 현재 질문 인덱스
  const questions = document.querySelectorAll('.checklist form div'); // 모든 질문 요소
  const submitButton = document.getElementById('submit'); // '결과 보기' 버튼
  const resultSection = document.getElementById('resultSection'); // 결과 영역
  const resultText = document.getElementById('resultText'); // 결과 텍스트
  const form = document.querySelector('form'); // 폼 요소 추가
  const selectedAnswers = []; // 선택된 답변 저장 배열

  // 페이지 로딩 시, 이전 답변을 서버로부터 가져와서 처리하는 함수
  fetchPreviousAnswers();

  // 첫 번째 질문에 'active' 클래스 추가
  questions[currentQuestionIndex].classList.add('active');

  // 각 체크박스 클릭 시
  document.querySelectorAll('input[type="checkbox"]').forEach(checkbox => {
      checkbox.addEventListener('change', function () {
          // 선택된 답변을 저장
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

          // 답변을 제출한 후, 자동으로 다음 질문으로 넘어가기
          if (selectedAnswers.length > 0 && currentQuestionIndex < questions.length - 1) {
              // 현재 질문 숨기기
              questions[currentQuestionIndex].classList.remove('active');

              // 질문 인덱스 증가
              currentQuestionIndex++;

              // 새로운 질문 보이기
              questions[currentQuestionIndex].classList.add('active');

              // '결과 보기' 버튼은 마지막 질문에서만 보이게 함
              if (currentQuestionIndex === questions.length - 1) {
                  submitButton.style.display = 'block';
              }
              // 바로 답변이 저장되므로 DB로 전송
              sendSelectedAnswerToServer(answerId);
          }
      });
  });

  // '결과 보기' 버튼 클릭 시
  submitButton.addEventListener('click', function (event) {
      event.preventDefault();

      if (selectedAnswers.length === questions.length) {
          // 선택된 답변을 바탕으로 유형 도출
          const result = getResultType(selectedAnswers);
          // 결과 텍스트 업데이트
          resultText.textContent = `당신의 유형: ${result.type}`;

          // 결과와 함께 모든 질문과 선택된 답변 보여주기
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

  function sendSelectedAnswerToServer(answer) {
      const data = {
          answer: selectedAnswer
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

document.addEventListener('click', function) {
  function getSelectedAnswers() {
      const checkboxes = document.querySelectorAll('input[type="checkbox"]:checked');
      let selectedAnswers = [];
      checkboxes.forEach(checkbox => {
          selectedAnswers.push(checkbox.id);
      });
      return selectedAnswers;
  }
};

  function getResultType(selectedAnswers) {
      let type = "자유여행";  // 기본값

      // 자유여행
      if (selectedAnswers.includes('q1-a1') && selectedAnswers.includes('q3-a2')) {
          type = "자유여행";
      }
      // 패키지여행
      else if (selectedAnswers.includes('q2-a1') && selectedAnswers.includes('q4-a1')) {
          type = "패키지여행";
      }
      // 가족여행
      else if (selectedAnswers.includes('q2-a2') && selectedAnswers.includes('q5-a1')) {
          type = "가족여행";
      }

      return { type };
  }

  // 선택된 항목의 질문 텍스트를 가져오는 함수
  function getQuestionText(answerId) {
      // 예시로 ID와 연결된 질문 텍스트를 반환하는 방법입니다.
      const question = document.querySelector(`label[for="${answerId}"]`);
      return question ? question.textContent : "알 수 없는 질문";
  }

  // '체크리스트 재작성' 버튼 클릭 시 체크박스 초기화
  document.getElementById('btn-checklist-rewrite').addEventListener('click', function () {
      const checkboxes = document.querySelectorAll('input[type="checkbox"]');
      checkboxes.forEach(checkbox => {
          checkbox.checked = false;
      });

      // 결과 텍스트 초기화
      resultText.textContent = '';
      resultSection.innerHTML = '';  // 결과 영역 초기화
      submitButton.style.display = 'none'; // 결과보기 버튼 숨기기

      // 첫 번째 질문부터 다시 시작
      currentQuestionIndex = 0;
      selectedAnswers.length = 0; // 선택된 답변 배열 초기화
      questions.forEach(question => {
          question.classList.remove('active');
      });

      // 첫 번째 질문에 active 클래스 추가
      questions[currentQuestionIndex].classList.add('active');
  });

  // '체크리스트 보기' 버튼 클릭 시 선택된 항목 보기
  document.getElementById('btn-checklist-view').addEventListener('click', function () {
      const resultText = document.getElementById('resultText').textContent;
      alert(resultText || '선택된 항목이 없습니다.');
  });

  // 이전에 저장된 답변을 서버에서 가져오는 함수
  function fetchPreviousAnswers() {
      fetch('/api/checklist/me/check')  // 이전 답변을 로드하는 API 엔드포인트
      .then(response => response.json())
      .then(data => {
          if (data && data.answers) {
              // 이전에 선택한 답변을 selectedAnswers 배열에 추가
              data.answers.forEach(answer => {
                  selectedAnswers.push(answer);
                  const checkbox = document.getElementById(answer);
                  if (checkbox) {
                      checkbox.checked = true;
                  }
              });

              // 마지막으로 답변한 질문 인덱스를 가져와서, 해당 질문까지 보여줍니다.
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
