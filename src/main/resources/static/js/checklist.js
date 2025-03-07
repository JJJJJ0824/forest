document.addEventListener('DOMContentLoaded', function () {
    console.log("checklist.js 로드됨! 사용자 정보 로드 완료");

    let currentQuestionIndex = 0;
    let viewMode = false; // false: 새 작성 모드, true: 보기/수정 모드
    const questions = document.querySelectorAll('.checklist form div[id^="q"]');
    const submitButton = document.getElementById('submit');
    const resultSection = document.getElementById('resultSection');
    const resultText = document.getElementById('resultText');

    // 답변을 저장할 배열을 밖에 선언
    let allAnswers = [];  

    questions[currentQuestionIndex].classList.add('active');

    loadCheckedChecklist();

    // radio 버튼 변경 이벤트
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
                updateAnswerInDatabase({ questionText, answerText });
            } else {
                if (questionContainer.dataset.submitted !== "true") {
                    questionContainer.dataset.submitted = "true";
                    sendAnswerToServer({ questionText, answerText });
                }

                // allAnswers 배열에 추가
                allAnswers.push({ questionText, answerText });

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

    submitButton.addEventListener("click", function () {
    // 모든 질문에 답변이 완료되었는지 확인
    if (allAnswers.length !== questions.length) {
        resultText.textContent = "모든 질문에 답변을 완료해 주세요.";
        return;
    }

    document.querySelectorAll(".checklist form div[id^='q']").forEach((data) => {
        if (!data.classList.contains("active")) {
            data.classList.add("active");
        }
    });

    // 결과를 표시할 때, allAnswers 배열이 올바르게 채워졌는지 확인
    console.log("모든 답변:", allAnswers);

    const result = getResultType(allAnswers);
    resultText.textContent = `당신의 유형: ${result.type}`;

    // 모든 질문과 그에 대한 답변을 보여줍니다.
    const answersList = allAnswers
        .map(
            (item) => `
                <li>질문 : ${item.questionText}</li>
                <li>답변 : ${item.answerText}</li>
            `
        )
        .join("");

    resultSection.classList.add("active");
    resultSection.innerHTML = `
            <h3>결과 : ${result.type}</h3>
            <ul>${answersList}</ul>
        `;

    // 수정 및 재작성 버튼 추가
    const rewriteButton = document.createElement("button");
    rewriteButton.textContent = "체크리스트 재작성";
    rewriteButton.id = "btn-checklist-rewrite";
    rewriteButton.type = "button";
    resultSection.appendChild(rewriteButton);
    addRewriteListener();  // 재작성 버튼 클릭 시 초기화 함수 호출
});

    

    function addRewriteListener() {
        document.getElementById('btn-checklist-rewrite').addEventListener('click', function () {
            fetch('/api/checklist/delete', {
                method: 'DELETE'
            })
            .then(response => response.text())
            .then(data => {
                console.log('체크리스트 삭제 성공:', data);
                viewMode = false;
                resetChecklist();
            })
            .catch(error => console.error('체크리스트 삭제 중 에러 발생:', error));
        });
    }

    function loadCheckedChecklist() {
        fetch('/api/checklist/me/check')
            .then(response => response.json())
            .then(data => {
                data.forEach(answer => {
                    const questionElement = Array.from(questions).find(q => {
                        return q.querySelector('p').textContent.trim() === answer.direction;
                    });
                    if (questionElement) {
                        questionElement.dataset.submitted = "true"; 
                        const radio = questionElement.querySelector(`input[type="radio"][value="${answer.response}"]`);
                        if (radio) {
                            radio.checked = true;
                            allAnswers.push({ questionText: answer.direction, answerText: answer.response });
                        }
                    }
                });
                // 답변이 모두 작성된 경우 바로 보기 모드로 전환
                if (data.length === questions.length) {
                    viewMode = true;
                    questions.forEach(q => {
                        q.classList.add('active'); // 모든 질문을 보이게 함
                        q.querySelectorAll('input[type="radio"]').forEach(radio => {
                            radio.disabled = false; // 보기 모드에서 수정 가능하도록 설정
                        });
                    });
                } else {
                    moveToNextQuestion();  // 답변을 완료하지 않으면 다음 질문으로 이동
                }
            })
            .catch(error => {
                console.error('제출된 답변 로드 실패:', error);
            });
    }

    function moveToNextQuestion() {
        let nextQuestionIndex = currentQuestionIndex;
        for (let i = currentQuestionIndex; i < questions.length; i++) {
            const question = questions[i];
            if (question.dataset.submitted === "true") {
                nextQuestionIndex = i + 1;
            }
        }

        if (nextQuestionIndex < questions.length) {
            questions[currentQuestionIndex].classList.remove('active');
            currentQuestionIndex = nextQuestionIndex;
            questions[currentQuestionIndex].classList.add('active');

            if (currentQuestionIndex === questions.length - 1) {
                submitButton.style.display = 'block';
            }
        }
    }

    function getQuestionText(questionId) {
        const questionElement = document.querySelector(`#${questionId} p`);
        if (!questionElement) {
            console.error(`해당 questionElement를 찾을 수 없습니다: ${questionId}`);
            return "";
        }
        return questionElement.textContent.trim();
    }

    function sendAnswerToServer({ questionText, answerText }) {
        fetch('/api/checklist/submit', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                direction: questionText,   
                response: answerText,
                checked: 0==0,
                category: getResultType(allAnswers).type
            })
        })
        .then(response => {
            if (!response.ok) throw new Error(response.statusText);
            return response.json();
        })
        .then(data => {
            console.log('체크리스트 저장 성공:', data);
        })
        .catch(error => {
            console.error('체크리스트 저장 중 에러 발생:', error);
        });
    }

    function updateAnswerInDatabase({ questionText, answerText }) {
        fetch('/api/checklist/update', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                direction: questionText,
                response: answerText,
                checked: 0==0,
                category: getResultType(allAnswers).type
            })
        })
        .then(response => {
            if (!response.ok) throw new Error(response.statusText);
            return response.json();
        })
        .then(data => {
            console.log('체크리스트 업데이트 성공:', data);
        })
        .catch(error => {
            console.error('체크리스트 업데이트 에러:', error);
        });
    }

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
        resultSection.classList.remove('active'); 
        resultText.textContent = ''; 
    
        allAnswers = [];
    
        viewMode = false;
    }

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
