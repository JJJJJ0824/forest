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
            console.log(qaDTO);

            // 2. 질문 제목과 내용을 화면에 표시
            document.getElementById("question-title").textContent = qaDTO.title;
            document.getElementById("question-content").textContent = qaDTO.content;

            // 3. 답변 표시
            const answerElement = document.getElementById("answer");
            
        
            
            const answer = qaDTO.qaReadDTO ? qaDTO.qaReadDTO.content : null; 

            const li = document.createElement('li');
            if (answer) {
                li.textContent = answer;  // 답변 내용 추가
            } else {
                li.textContent = "답변이 없습니다.";  // 답변이 없을 경우
            }
            answerElement.appendChild(li);  // li 요소를 답변 목록에 추가
        } else {
            console.error("질문과 답변 정보를 불러오는데 실패했습니다. 상태 코드:", xhr.status);
        }
    };
    xhr.onerror = function() {
        console.error("네트워크 오류가 발생했습니다.");
    };
    xhr.send();

    const replyButton = document.querySelector(".button_reply");
    if (replyButton) {
        replyButton.addEventListener("click", function () {
            window.location.href = "q.html?id=" + questionId; 
        });
    } else {
        console.error("답변하기 버튼을 찾을 수 없습니다.");
    }
});
