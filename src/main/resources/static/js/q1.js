document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const questionId = urlParams.get('id'); // 예: ?id=123

    // id 값이 없거나 null인 경우
    if (!questionId || questionId === "null") {
        alert("잘못된 접근입니다.");
        return;
    }

    // 질문과 답변을 서버에서 불러오기
    let xhr = new XMLHttpRequest();
    xhr.open("GET", `/api/q_a/${questionId}`, true);
    xhr.onload = function() {
        if (xhr.status === 200) {
            const question = JSON.parse(xhr.responseText); // 질문 세부 정보 파싱

            document.getElementById("question-title").textContent = question.title;
            document.getElementById("question-content").textContent = question.content;

            // 답변 목록 표시
            const answerList = document.getElementById("answer-list");
            answerList.innerHTML = ''; // 기존 답변 목록 초기화

            // question.answers가 배열인지 확인 후, 없으면 빈 배열로 처리
            const answers = Array.isArray(question.answers) ? question.answers : [];

            answers.forEach(answer => {
                const li = document.createElement('li');
                li.textContent = answer.content;
                answerList.appendChild(li);
            });
        } else {
            console.error("질문 세부 정보를 불러오는데 실패했습니다. 상태 코드:", xhr.status);
        }
    };

    xhr.onerror = function() {
        console.error("네트워크 오류가 발생했습니다.");
    };

    xhr.send();
});
