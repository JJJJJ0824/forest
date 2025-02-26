document.addEventListener('DOMContentLoaded', function() {
    const questions = document.querySelectorAll("#question");
    const submitButton = document.querySelector('button[type="submit"]'); 
    const progressBar = document.querySelector("progress");
    const resultText = document.getElementById("resultText");
    const checklistViewButton = document.getElementById("btn-checklist-view");
    const checklistRewriteButton = document.getElementById("btn-checklist-rewrite");
    const resultSection = document.getElementById("resultSection"); 

    let userResponses = {}; 
    let currentQuestionIndex = 0; 

    const updateQuestionDisplay = () => {
        questions.forEach((question, index) => {
            if (index === currentQuestionIndex) {
                question.classList.add("active");
            } else {
                question.classList.remove("active");
            }
        });
        const progress = ((currentQuestionIndex + 1) / questions.length) * 100;

        console.log("currentQuestionIndex:", currentQuestionIndex);
        console.log("questions.length:", questions.length);
        console.log("calculated progress:", progress);

        if (isNaN(progress) || progress < 0 || progress > 100) {
            console.error("Invalid progress value: ", progress);
            progressBar.value = 0;  
        } else {
            progressBar.value = progress;
        }

        if (currentQuestionIndex === questions.length - 1) {
            resultSection.style.display = "block"; 
            submitButton.style.display = "block"; 
        }
    };

    const savedData = JSON.parse(localStorage.getItem("userResponses"));
    const savedProgress = localStorage.getItem("currentQuestionIndex");

    if (savedData) {
        userResponses = savedData;
        currentQuestionIndex = parseInt(savedProgress, 10);
        updateQuestionDisplay();  
    }

    resultSection.style.display = "none";
    submitButton.style.display = "none";

    const radioButtons = document.querySelectorAll("input[type='radio']");
    radioButtons.forEach(button => {
        button.addEventListener("change", function() {
            const questionName = this.name;
            const selectedOption = this.value;

            userResponses[questionName] = selectedOption;
            localStorage.setItem("userResponses", JSON.stringify(userResponses));
            localStorage.setItem("currentQuestionIndex", currentQuestionIndex);

            handleNextQuestion();
            checkIfChecklistCompleted();
        });
    });

    const handleNextQuestion = () => {
        if (currentQuestionIndex < questions.length - 1) {
            currentQuestionIndex++;
            updateQuestionDisplay();
        }
    };

    submitButton.addEventListener("click", function(e) {
        e.preventDefault(); 

        if (currentQuestionIndex === questions.length - 1) {
            const userType = calculateUserType(userResponses);
            resultText.textContent = `당신의 유형은: ${userType}입니다.`; 
        }
    });

    function calculateUserType(responses) {
        let familyPoints = 0;
        let freePoints = 0;
        let packagePoints = 0;

        if (responses['q4'] === '가족 (아이들과 함께)' || responses['q1'] === '휴식과 여유' || responses['q2'] === '가족 친화적인 호텔') {
            familyPoints++;
        }

        if (responses['q3'] === '완전 자유 여행 (자유롭게 일정 조정)' || responses['q5'] === '자유 일정') {
            freePoints++;
        }

        if (responses['q3'] === '패키지 여행 (사전 계획된 일정)') {
            packagePoints++;
        }

        if (familyPoints > freePoints && familyPoints > packagePoints) {
            return "가족 유형";
        } else if (freePoints > familyPoints && freePoints > packagePoints) {
            return "자유 유형";
        } else if (packagePoints > familyPoints && packagePoints > freePoints) {
            return "패키지 유형";
        }

        return "자유 유형"; 
    }

    checklistViewButton.addEventListener("click", function() {
        if (Object.keys(userResponses).length > 0) {
            let checklistContent = '<h3>저장된 체크리스트</h3>';
            for (let question in userResponses) {
                const questionText = getQuestionText(question);
                checklistContent += `<p><strong>${questionText}</strong>: ${userResponses[question]}</p>`;
            }
            resultText.innerHTML = checklistContent;
        } else {
            resultText.textContent = "저장된 체크리스트가 없습니다.";
        }
    });

    checklistRewriteButton.addEventListener("click", function() {
        localStorage.removeItem("userResponses");
        localStorage.removeItem("currentQuestionIndex");
        userResponses = {};
        currentQuestionIndex = 0;
        updateQuestionDisplay();
        resultText.textContent = "체크리스트가 초기화되었습니다. 다시 작성해주세요.";
    });

    function checkIfChecklistCompleted() {
        const allAnswered = Object.keys(userResponses).length === questions.length; 
        const isLastQuestion = currentQuestionIndex === questions.length - 1; 

        if (allAnswered && isLastQuestion) {
            submitButton.disabled = false;
            checklistViewButton.disabled = false; 
            checklistRewriteButton.disabled = false; 
        } else {
            submitButton.disabled = true;
            checklistViewButton.disabled = true; 
            checklistRewriteButton.disabled = true; 
        }
    }

    function getQuestionText(questionName) {
        switch (questionName) {
            case 'q1':
                return "보라카이에 가는 주된 목적은 무엇인가요?";
            case 'q2':
                return "보라카이에서 선호하는 숙소 유형은 무엇인가요?";
            case 'q3':
                return "보라카이 여행을 계획하면서 어떤 스타일을 선호하시나요?";
            case 'q4':
                return "여행에 누구와 함께 가나요?";
            case 'q5':
                return "여행 일정은 얼마나 유동적이어야 하나요?";
            case 'q6':
                return "보라카이에서 해양 스포츠 활동을 얼마나 선호하시나요?";
            case 'q7':
                return "보라카이에서 어떤 스타일의 식사를 선호하시나요?";
            case 'q8':
                return "여행 중 쇼핑을 얼마나 중요하게 생각하시나요?";
            case 'q9':
                return "자연을 가까이에서 경험하는 것이 중요하신가요?";
            case 'q10':
                return "보라카이 여행의 예산은 어느 정도인가요?";
            default:
                return "알 수 없는 질문";
        }
    }
});
