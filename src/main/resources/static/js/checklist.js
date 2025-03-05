document.addEventListener('DOMContentLoaded', function () {
    console.log("checklist.js 로드됨! 사용자 정보 로드 완료");
  
    let currentQuestionIndex = 0;
    let viewMode = false; // false: 새 작성 모드, true: 보기/수정 모드
    const questions = document.querySelectorAll('.checklist form div[id^="q"]');
    const submitButton = document.getElementById('submit');
    const resultSection = document.getElementById('resultSection');
    const resultText = document.getElementById('resultText');
  
    // 첫 질문 active 처리
    questions[currentQuestionIndex].classList.add('active');
  
    // 1. 서버에서 제출된 답변을 불러와서 표시
    loadCheckedChecklist();
  
    // radio 버튼 변경 이벤트 (각 질문에서 발생)
    document.querySelectorAll('input[type="radio"]').forEach(radio => {
        radio.addEventListener('change', function () {
            const questionId = radio.name;  // 예: "q1", "q2" 등
            const answerText = radio.value;
            const questionText = getQuestionText(questionId);
            const questionContainer = document.getElementById(questionId);
  
            if (!questionContainer) {
                console.error("해당 질문 컨테이너를 찾을 수 없음:", questionId);
                return;
            }
  
            if (viewMode) {
                // 보기 모드에서 수정 시 PUT 요청으로 전송
                updateAnswerInDatabase({ questionText, answerText });
            } else {
                // 처음 제출이면 POST 요청
                if (questionContainer.dataset.submitted !== "true") {
                    questionContainer.dataset.submitted = "true";
                    sendAnswerToServer({ questionText, answerText });
                }
  
                // 새 작성 모드일 경우, 다음 질문으로 이동
                if (currentQuestionIndex < questions.length - 1) {
                    questions[currentQuestionIndex].classList.remove('active');
                    currentQuestionIndex++;
                    questions[currentQuestionIndex].classList.add('active');
                    if (currentQuestionIndex === questions.length - 1) {
                        submitButton.style.display = 'block';
                    }
                }
            }
        });
    });
  
    // 최종 결과 보기 (제출) 버튼 클릭 이벤트
    submitButton.addEventListener('click', function () {
        const allAnswers = getAllAnswers();
        if (allAnswers.length !== questions.length) {
            resultText.textContent = "모든 질문에 답변을 완료해 주세요.";
            return;
        }
        const result = getResultType(allAnswers);
        resultText.textContent = `당신의 유형: ${result.type}`;
        const answersList = allAnswers.map(item => `<li>${item.questionText} : ${item.answerText}</li>`).join('');
        resultSection.classList.add('active');
        resultSection.innerHTML = `
            <h3>결과</h3>
            <p>${result.type}</p>
            <ul>${answersList}</ul>
        `;
        // 보기 및 재작성 버튼 추가
        const viewButton = document.createElement('button');
        viewButton.textContent = '체크리스트 보기';
        viewButton.id = 'btn-checklist-view';
        viewButton.type = 'button';
        const rewriteButton = document.createElement('button');
        rewriteButton.textContent = '체크리스트 재작성';
        rewriteButton.id = 'btn-checklist-rewrite';
        rewriteButton.type = 'button';
        resultSection.appendChild(viewButton);
        resultSection.appendChild(rewriteButton);
        addViewAndRewriteListeners();
    });
  
    // 보기 / 재작성 버튼 이벤트 등록
    function addViewAndRewriteListeners() {
        // 보기 버튼: 모든 질문을 active로 만들고(즉, 전체 답변 표시), 수정은 PUT 요청으로 처리
        document.getElementById('btn-checklist-view').addEventListener('click', function () {
            viewMode = true;
            questions.forEach(q => q.classList.add('active'));
        });
  
        // 재작성 버튼: DELETE 요청으로 DB 체크리스트 삭제 후 UI 리셋
        document.getElementById('btn-checklist-rewrite').addEventListener('click', function () {
            fetch('/api/checklist/delete', {
                method: 'DELETE'
            })
            .then(response => response.json())
            .then(data => {
                console.log('✅ 체크리스트 삭제 성공:', data);
                viewMode = false;
                resetChecklist();
            })
            .catch(error => console.error('🚨 체크리스트 삭제 중 에러 발생:', error));
        });
    }
  
    // 서버에서 제출된 답변을 불러오기 (API 변경)
    function loadCheckedChecklist() {
        fetch('/api/checklist/me/check')  // 사용자가 체크한 체크리스트 불러오기
            .then(response => response.json())
            .then(data => {
                data.forEach(answer => {
                    const questionElement = Array.from(questions).find(q => {
                        return q.querySelector('p').textContent.trim() === answer.direction;
                    });
                    if (questionElement) {
                        questionElement.dataset.submitted = "true"; // 제출된 상태로 표시
                        const radio = questionElement.querySelector(`input[type="radio"][value="${answer.response}"]`);
                        if (radio) {
                            radio.checked = true; // 이미 선택된 답변 표시
                        }
                    }
                });
            })
            .catch(error => {
                console.error('🚨 제출된 답변 로드 실패:', error);
            });
    }
  
    function getQuestionText(questionId) {
        const questionElement = document.querySelector(`#${questionId} p`);
        if (!questionElement) {
            console.error(`해당 questionElement를 찾을 수 없습니다: ${questionId}`);
            return "";
        }
        return questionElement.textContent.trim();
    }
  
    function getAllAnswers() {
        let answers = [];
        questions.forEach(q => {
            const questionText = q.querySelector('p').textContent.trim();
            const checkedInput = q.querySelector('input[type="radio"]:checked');
            if (checkedInput) {
                answers.push({ questionText, answerText: checkedInput.value });
            }
        });
        return answers;
    }
  
    function sendAnswerToServer({ questionText, answerText }) {
        fetch('/api/checklist/submit', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                direction: questionText,   
                response: answerText,
                isChecked: true,
            })
        })
        .then(response => {
            if (!response.ok) throw new Error(response.statusText);
            return response.json();
        })
        .then(data => {
            console.log('✅ 체크리스트 저장 성공:', data.JSON);
        })
        .catch(error => {
            console.error('🚨 체크리스트 저장 중 에러 발생:', error);
        });
    }
  
    function updateAnswerInDatabase({ questionText, answerText }) {
        fetch('/api/checklist/update', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                direction: questionText,
                response: answerText,
                isChecked: true,
            })
        })
        .then(response => {
            if (!response.ok) throw new Error(response.statusText);
            return response.json();
        })
        .then(data => {
            console.log('✅ 체크리스트 업데이트 성공:', data);
        })
        .catch(error => {
            console.error('🚨 체크리스트 업데이트 에러:', error);
        });
    }
  
    // 재작성 시 UI 및 DOM 상태 초기화
    function resetChecklist() {
        questions.forEach(q => {
            q.classList.remove('active');
            q.removeAttribute('data-submitted');
            q.querySelectorAll('input[type="radio"]').forEach(radio => {
                radio.checked = false;
                radio.disabled = false;
            });
        });
        currentQuestionIndex = 0;
        questions[currentQuestionIndex].classList.add('active');
        submitButton.style.display = 'none';
        resultSection.innerHTML = '';
        resultText.textContent = '';
    }
  
    // 예시로 작성한 결과 유형 결정 함수 (필요에 따라 수정)
    function getResultType(answers) {
        let type = "자유여행";
        if (answers.some(a => a.questionText.includes("보라카이에 가는 주된 목적") && a.answerText === "휴식과 여유")) {
            type = "가족여행";
        } else if (answers.some(a => a.questionText.includes("보라카이에서 선호하는 숙소") && a.answerText === "럭셔리 리조트")) {
            type = "패키지여행";
        }
        return { type };
    }
  });
  