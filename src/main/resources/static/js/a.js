document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const questionId = urlParams.get('id');  // URL에서 'id' 파라미터를 추출

    if (!questionId) {
        alert("잘못된 접근입니다. 질문 ID가 없습니다.");
        return;
    }

    // 질문 ID가 유효하다면, 서버에 답변을 요청하거나, 답변을 제출하는 처리를 진행
    document.querySelector('.submit-reply').addEventListener('click', function() {
        const replyContent = document.getElementById('reply-textarea').value;

        if (replyContent.trim() === "") {
            alert("답변 내용을 입력해주세요.");
            return;
        }

        // 답변을 서버에 보내는 요청
        let xhr = new XMLHttpRequest();
        xhr.open("POST", `/api/q_a/${questionId}/reply`, true);  // 질문 ID 포함 (URL 경로)
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onload = function() {
            if (xhr.status === 200) {
                // 성공적으로 제출되면, 답변 리스트를 갱신하거나 이전 페이지로 돌아가기
                alert("답변이 제출되었습니다.");
                window.location.href = `/q1.html?id=${questionId}`;
            } else {
                alert("답변 제출에 실패했습니다.");
            }
        };
        xhr.onerror = function() {
            alert("네트워크 오류가 발생했습니다.");
        };

        xhr.send(JSON.stringify({ content: replyContent }));
    });
});
