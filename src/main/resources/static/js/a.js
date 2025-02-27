document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const questionId = urlParams.get('id');  // URL에서 'id' 파라미터를 추출

    if (!questionId) {
        alert("잘못된 접근입니다. 질문 ID가 없습니다.");
        return;
    }

    // 질문 ID가 유효하다면, 서버에서 해당 질문의 제목과 내용을 가져옵니다.
    let xhr = new XMLHttpRequest();
    xhr.open("GET", `/api/q_a/${questionId}`, true);  // 질문 ID를 URL에 포함
    xhr.onload = function() {
        if (xhr.status === 200) {
            // 서버로부터 받은 질문 정보를 파싱
            const questionData = JSON.parse(xhr.responseText);  
            document.getElementById('post-title').textContent = questionData.title;
            document.getElementById('post-content').textContent = questionData.content;
        } else {
            alert("질문 정보를 불러오는 데 실패했습니다.");
        }
    };
    xhr.onerror = function() {
        alert("네트워크 오류가 발생했습니다.");
    };
    xhr.send();

    // 답변을 제출하는 처리
    document.querySelector('.submit-reply').addEventListener('click', function() {
        const replyTitle = document.getElementById('title').value;  // 답변 제목
        const replyContent = document.getElementById('reply-textarea').value;  // 답변 내용

        if (replyTitle.trim() === "") {
            alert("답변 제목을 입력해주세요.");
            return;
        }

        if (replyContent.trim() === "") {
            alert("답변 내용을 입력해주세요.");
            return;
        }

        // 답변을 서버에 보내는 요청
        let xhrReply = new XMLHttpRequest();
        xhrReply.open("POST", `/api/q_a/${questionId}/reply`, true);  // 질문 ID 포함 (URL 경로)
        xhrReply.setRequestHeader("Content-Type", "application/json");
        xhrReply.onload = function() {
            if (xhrReply.status === 201) {
                alert("답변이 제출되었습니다.");
                window.location.href = `/q1.html?id=${questionId}`;
            } else {
                alert("답변 제출에 실패했습니다.");
            }
        };
        xhrReply.onerror = function() {
            alert("네트워크 오류가 발생했습니다.");
        };

        // 제목과 내용을 JSON으로 서버에 전송
        xhrReply.send(JSON.stringify({ title: replyTitle, content: replyContent }));
    });
});
