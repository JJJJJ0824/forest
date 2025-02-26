document.addEventListener('DOMContentLoaded', function() {
    // 1. URL에서 questionId를 받아옴
    const urlParams = new URLSearchParams(window.location.search);
    const questionId = urlParams.get('id');  // URL에서 'id' 파라미터를 추출

    // 2. 'id'가 없으면 경고 메시지 출력
    if (!questionId) {
        alert("잘못된 접근입니다. 질문 ID가 없습니다.");
        return;
    }

    // 3. 서버에서 해당 질문 정보를 가져오는 AJAX 요청
    let xhr = new XMLHttpRequest();
    xhr.open("GET", `/api/q_a/${questionId}`, true);  // 질문 ID를 포함한 GET 요청
    xhr.onload = function() {
        if (xhr.status === 200) {
            const data = JSON.parse(xhr.responseText); // 서버 응답 데이터 파싱
            document.getElementById('question-title').textContent = data.title;  // 질문 제목 표시
            document.getElementById('question-content').textContent = data.content;  // 질문 내용 표시

            // 4. 답변 리스트 가져오기
            const answerList = data.answers;  // 응답 데이터
            const answerListContainer = document.getElementById('answer-list');

            // 5. 답변이 없을 경우 처리
            if (answerList.length === 0) {
                const noAnswerItem = document.createElement('li');
                noAnswerItem.textContent = '답변이 아직 없습니다.';  // 답변이 없을 경우 메시지 표시
                answerListContainer.appendChild(noAnswerItem);
            } else {
                // 6. 답변이 있을 경우 리스트에 추가
                answerList.forEach(answer => {
                    const answerItem = document.createElement('li');
                    answerItem.textContent = `${answer.author}: ${answer.content}`;  // 답변자와 내용 표시
                    answerListContainer.appendChild(answerItem);
                });
            }
        } else {
            alert("질문 데이터를 가져오는 데 실패했습니다.");
        }
    };
    xhr.onerror = function() {
        alert("네트워크 오류가 발생했습니다.");
    };

    xhr.send();

    // 7. '답변하기' 버튼의 링크 수정
    const answerButton = document.querySelector('.button-container a');  // 답변하기 버튼
    if (answerButton && questionId) {
        answerButton.href = `a.html?id=${questionId}`;  // 버튼에 id 값 포함시켜서 링크 설정
    }
});
