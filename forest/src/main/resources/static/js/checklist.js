document.addEventListener('DOMContentLoaded', function() {
  const questions = document.querySelectorAll("#question");  // 모든 질문
  const submitButton = document.querySelector('button[type="submit"]'); // 제출 버튼
  const progressBar = document.querySelector("progress"); // 프로그레스 바
  const resultText = document.getElementById("resultText"); // 결과 출력 div
  const checklistViewButton = document.getElementById("btn-checklist-view");
  const checklistRewriteButton = document.getElementById("btn-checklist-rewrite");
  const resultSection = document.getElementById("resultSection");  // 결과 섹션 (숨기기 위한 변수 추가)

  let userResponses = {};  // 사용자의 응답 저장
  let currentQuestionIndex = 0; // 현재 질문 인덱스

  // 질문 표시 업데이트 함수
  const updateQuestionDisplay = () => {  // 함수 정의를 먼저
      questions.forEach((question, index) => {
          if (index === currentQuestionIndex) {
              question.classList.add("active");
          }
      });
      const progress = ((currentQuestionIndex + 1) / questions.length) * 100;

      // 디버깅: 콘솔에 진행도 값 확인
      console.log("currentQuestionIndex:", currentQuestionIndex);
      console.log("questions.length:", questions.length);
      console.log("calculated progress:", progress);

      // progress가 정상적인 값인지를 확인하고, 아니면 0을 기본값으로 설정
      if (isNaN(progress) || progress < 0 || progress > 100) {
          console.error("Invalid progress value: ", progress);
          progressBar.value = 0;  // Invalid 값일 경우 0으로 설정
      } else {
          progressBar.value = progress;
      }

      // 마지막 질문이 끝날 때 결과 버튼을 표시
      if (currentQuestionIndex === questions.length - 1) {
          resultSection.style.display = "block";  // 결과 보기 버튼을 보이게
          submitButton.style.display = "block";  // 제출 버튼도 보이게
      }
  };

  // 로컬 스토리지에서 기존 응답 및 진행도 불러오기
  const savedData = JSON.parse(localStorage.getItem("userResponses"));
  const savedProgress = localStorage.getItem("currentQuestionIndex");
  
  if (savedData) {
      userResponses = savedData;
      currentQuestionIndex = parseInt(savedProgress, 10);
      updateQuestionDisplay();  // 이제는 함수가 정의된 후 호출됨
  }

  // 결과 섹션과 제출 버튼을 처음에 숨기기
  resultSection.style.display = "none";
  submitButton.style.display = "none";

  // 각 질문에 대한 라디오 버튼 선택 시 응답 저장하고 다음 질문으로 넘어감
  const radioButtons = document.querySelectorAll("input[type='radio']");
  radioButtons.forEach(button => {
      button.addEventListener("change", function() {
          const questionName = this.name;
          const selectedOption = this.value;

          // 응답 저장
          userResponses[questionName] = selectedOption;
          localStorage.setItem("userResponses", JSON.stringify(userResponses));
          localStorage.setItem("currentQuestionIndex", currentQuestionIndex);

          handleNextQuestion();
      });
  });

  const handleNextQuestion = () => {
      if (currentQuestionIndex < questions.length - 1) {
          currentQuestionIndex++;
          updateQuestionDisplay();
      }
  };

  // "결과 보기" 버튼 클릭 시
  submitButton.addEventListener("click", function(e) {
      e.preventDefault();  // 기본 동작 막기 (페이지 이동 방지)

      if (currentQuestionIndex === questions.length - 1) {
          // 결과 계산
          const userType = calculateUserType(userResponses);
          resultText.textContent = `당신의 유형은: ${userType}입니다.`;  // 결과 텍스트로 표시
      }
  });

  // 여행 유형 계산 함수
  function calculateUserType(responses) {
      let familyPoints = 0;
      let freePoints = 0;
      let packagePoints = 0;

      // **가족 여행** 조건
      if (responses['q4'] === '가족 (아이들과 함께)' || responses['q1'] === '휴식과 여유' || responses['q2'] === '가족 친화적인 호텔') {
          familyPoints++;
      }

      // **자유 여행** 조건
      if (responses['q3'] === '완전 자유 여행 (자유롭게 일정 조정)' || responses['q5'] === '자유 일정') {
          freePoints++;
      }

      // **패키지 여행** 조건
      if (responses['q3'] === '패키지 여행 (사전 계획된 일정)') {
          packagePoints++;
      }

      // 각 점수 비교 후 유형 결정
      if (familyPoints > freePoints && familyPoints > packagePoints) {
          return "가족 유형";
      } else if (freePoints > familyPoints && freePoints > packagePoints) {
          return "자유 유형";
      } else if (packagePoints > familyPoints && packagePoints > freePoints) {
          return "패키지 유형";
      }

      return "자유 유형";  // 특정 조건이 없을 때
  }

  // 체크리스트 보기 버튼 클릭 시
  checklistViewButton.addEventListener("click", function() {
      if (Object.keys(userResponses).length > 0) {
          let checklistContent = '';
          for (let question in userResponses) {
              checklistContent += `<p>${question}: ${userResponses[question]}</p>`;
          }
          resultText.innerHTML = `<h3>저장된 체크리스트</h3>${checklistContent}`;
      } else {
          resultText.textContent = "저장된 체크리스트가 없습니다.";
      }
  });

  // 체크리스트 재작성 버튼 클릭 시
  checklistRewriteButton.addEventListener("click", function() {
      // 로컬 스토리지 초기화
      localStorage.removeItem("userResponses");
      localStorage.removeItem("currentQuestionIndex");
      userResponses = {};
      currentQuestionIndex = 0;
      updateQuestionDisplay();
      resultText.textContent = "체크리스트가 초기화되었습니다. 다시 작성해주세요.";
  });
});
