document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const questionId = urlParams.get('id'); // URL에서 questionId 가져오기

    if (!questionId || questionId === "null") {
        alert("잘못된 접근입니다.");
        return;
    }

    // 1. 질문 정보와 답변 정보 가져오기
    let xhr = new XMLHttpRequest();
    xhr.open("GET", `/api/q_a/${questionId}`, true); // 해당 질문과 답변을 포함한 QaDTO 가져오기
    xhr.onload = function() {
        if (xhr.status === 200) {
            const qaDTO = JSON.parse(xhr.responseText); // QaDTO 파싱

            // 2. 질문 제목과 내용을 화면에 표시
            document.getElementById("question-title").textContent = qaDTO.title;  // 질문 제목 표시
            document.getElementById("question-content").textContent = qaDTO.content;

            // 3. 답변 표시
            const answerElement = document.getElementById("answer");

            // "답변" 제목을 동적으로 변경하려면 먼저 <h3>를 찾아서 업데이트해야 합니다.
            const answerTitleElement = document.querySelector("section.answer-section h3"); // <h3>답변</h3> 요소 찾기
            if (qaDTO.qaReadDTO) {
                // 답변이 있을 경우, 해당 답변의 제목을 <h3>에 반영
                answerTitleElement.textContent = qaDTO.qaReadDTO.title;  // 답변 제목으로 <h3> 텍스트 변경
            } else {
                // 답변이 없으면 "답변"으로 유지
                answerTitleElement.textContent = "답변";
            }

            // 답변 내용을 처리
            const answerContent = document.createElement('p');
            const answer = qaDTO.qaReadDTO ? qaDTO.qaReadDTO.content : null;  // 답변이 있을 경우
            const replyButton = document.querySelector(".button_reply");

            if (answer) {
                answerContent.textContent = answer;  // 답변 내용 추가
                replyButton.remove();  // 답변이 있으면 '답변하기' 버튼을 DOM에서 완전히 제거
            } else {
                answerContent.textContent = "답변이 없습니다.";  // 답변이 없을 경우
            }

            answerElement.appendChild(answerContent);

            // 답변 제목과 내용을 화면에 추가
            answerElement.appendChild(answerTitle);  // "답변" 제목 추가
            answerElement.appendChild(answerContent);  // 답변 내용 추가

        } else {
            console.error("질문과 답변 정보를 불러오는데 실패했습니다. 상태 코드:", xhr.status);
        }
    };
    xhr.onerror = function() {
        console.error("네트워크 오류가 발생했습니다.");
    };
    xhr.send();

    // '답변하기' 버튼 클릭 시 새로운 페이지로 이동
    const replyButton = document.querySelector(".button_reply");
    if (replyButton) {
        replyButton.addEventListener("click", function () {
            window.location.href = "q.html?id=" + questionId; // 질문 페이지로 이동하여 답변 작성
        });
    } else {
        console.error("답변하기 버튼을 찾을 수 없습니다.");
    }
});
