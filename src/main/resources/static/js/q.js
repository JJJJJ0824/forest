document.addEventListener("DOMContentLoaded", function () {
    const submitButton = document.querySelector(".submit-button"); // 질문 제출 버튼
    const titleInput = document.getElementById("title"); // 제목 입력
    const contentInput = document.getElementById("content"); // 내용 입력
    const errorMessage = document.createElement('p'); // 에러 메시지를 위한 요소

    // 질문 제출 버튼 클릭 시 이벤트 처리
    submitButton.addEventListener("click", function (event) {
        event.preventDefault(); // 폼 기본 제출 동작 방지

        // 제목과 내용 가져오기
        const title = titleInput.value.trim();
        const content = contentInput.value.trim();

        // 제목과 내용이 비어있으면 에러 메시지 출력
        if (!title || !content) {
            errorMessage.textContent = "제목과 내용을 모두 입력해주세요.";
            errorMessage.style.color = "red";
            document.querySelector(".write-container").appendChild(errorMessage);
            return;
        }

        // travelerName을 세션에서 가져오는 방법 (세션에 저장된 값으로 가정)
        const travelerName = sessionStorage.getItem("travelerName"); // 세션에서 가져오기

        // 서버로 보낼 데이터 객체 생성
        const questionData = {
            traveler_name: travelerName, // 세션에서 가져온 travelerName 값
            title: title,
            content: content,
            createdAt: new Date().toISOString(),  // 현재 시간
            qaReadDTO: {}  // 필요시 빈 객체로 설정
        };

        // Ajax 요청 준비
        let xhr = new XMLHttpRequest();
        xhr.open("POST", "/api/q_a/ask", true); // 질문 작성 API로 요청 전송
        xhr.setRequestHeader("Content-Type", "application/json");

        // 서버 응답 처리
        xhr.onload = function() {
            if (xhr.status === 201) { // 질문이 성공적으로 생성된 경우
                const response = JSON.parse(xhr.responseText);
                alert("질문이 성공적으로 등록되었습니다.");
                window.location.href = "qa.html"; // 질문 목록 페이지로 리디렉트
            } else {
                errorMessage.textContent = "질문 등록에 실패했습니다. 다시 시도해주세요.";
                errorMessage.style.color = "red";
                document.querySelector(".write-container").appendChild(errorMessage);
            }
        };

        // 네트워크 오류 처리
        xhr.onerror = function() {
            errorMessage.textContent = "네트워크 오류가 발생했습니다. 다시 시도해주세요.";
            errorMessage.style.color = "red";
            document.querySelector(".write-container").appendChild(errorMessage);
        };

        // Ajax로 데이터 전송
        xhr.send(JSON.stringify(questionData));
    });
});
