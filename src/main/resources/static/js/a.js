document.addEventListener('DOMContentLoaded', function() {
    // URL에서 'id' 추출
    const urlParams = new URLSearchParams(window.location.search);
    const questionId = urlParams.get('id');

    // 'id'가 없으면 경고 메시지 출력
    if (!questionId) {
        alert("잘못된 접근입니다. 질문 ID가 없습니다.");
        return;
    }

    // 질문 정보 가져오기
    let xhr = new XMLHttpRequest();
    xhr.open("GET", `/api/q_a/${questionId}`, true);  // 질문 ID를 포함한 GET 요청
    xhr.onload = function() {
        if (xhr.status === 200) {
            const data = JSON.parse(xhr.responseText);  // 서버 응답 데이터 파싱
            document.getElementById('post-title').textContent = data.title;  // 질문 제목 표시
            document.getElementById('post-content').textContent = data.content;  // 질문 내용 표시
        } else {
            alert("질문 데이터를 가져오는 데 실패했습니다.");
        }
    };
    xhr.onerror = function() {
        alert("네트워크 오류가 발생했습니다.");
    };
    xhr.send();

    // 기존 답변 목록 가져오기
    let xhrAnswers = new XMLHttpRequest();
    xhrAnswers.open("GET", `/api/q_a/${questionId}/replies`, true);  // 답변을 가져오는 GET 요청
    xhrAnswers.onload = function() {
        if (xhrAnswers.status === 200) {
            const replies = JSON.parse(xhrAnswers.responseText);  // 서버에서 가져온 답변 목록
            const answerList = document.getElementById('answer-list');
            
            // 답변이 있을 경우
            if (replies.length > 0) {
                replies.forEach(function(reply) {
                    const li = document.createElement('li');
                    li.innerHTML = `<strong>${reply.author}</strong>: ${reply.content}`;
                    answerList.appendChild(li);
                });
            } else {
                // 답변이 없을 경우
                const noRepliesMessage = document.createElement('li');
                noRepliesMessage.textContent = '현재 답변이 없습니다. 첫 번째 답변을 남겨주세요!';
                noRepliesMessage.style.fontStyle = 'italic';  // 조금 더 강조할 수 있도록 스타일링
                answerList.appendChild(noRepliesMessage);
            }
        } else {
            alert("답변을 가져오는 데 실패했습니다.");
        }
    };
    xhrAnswers.onerror = function() {
        alert("네트워크 오류가 발생했습니다.");
    };
    xhrAnswers.send();

    // 답변 제출 처리
    document.getElementById('submit-reply').addEventListener('click', function() {
        const replyContent = document.getElementById('reply-textarea').value;

        if (replyContent.trim() === "") {
            alert("답변 내용을 입력해주세요.");
            return;
        }

        // 답변을 서버에 보내는 요청
        let xhrReply = new XMLHttpRequest();
        xhrReply.open("POST", `/api/q_a/${questionId}/reply`, true);  // 질문 ID 포함
        xhrReply.setRequestHeader("Content-Type", "application/json");
        xhrReply.onload = function() {
            if (xhrReply.status === 200) {
                // 성공적으로 제출되면, 답변이 제출되었습니다 메시지와 함께 이전 페이지로 돌아가기
                alert("답변이 제출되었습니다.");
                window.location.href = `q1.html?id=${questionId}`;  // 답변이 등록된 페이지로 이동
            } else {
                alert("답변 제출에 실패했습니다.");
            }
        };
        xhrReply.onerror = function() {
            alert("네트워크 오류가 발생했습니다.");
        };
        xhrReply.send(JSON.stringify({ content: replyContent }));  // 답변 내용 전송
    });
});
