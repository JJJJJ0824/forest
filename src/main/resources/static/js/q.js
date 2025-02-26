document.addEventListener("DOMContentLoaded", function () {
    const submitButton = document.querySelector(".submit-button");
    const titleInput = document.getElementById("title");
    const contentInput = document.getElementById("content");
    const errorMessage = document.createElement('p'); 

    submitButton.addEventListener("click", function (event) {
        event.preventDefault();

        const title = titleInput.value.trim();
        const content = contentInput.value.trim();

        if (!title || !content) {
            errorMessage.textContent = "제목과 내용을 모두 입력해주세요.";
            errorMessage.style.color = "red";
            document.querySelector(".write-container").appendChild(errorMessage);
            return;
        }

        let xhr = new XMLHttpRequest();
        xhr.open("POST", "/api/q_a/ask", true);
        xhr.setRequestHeader("Content-Type", "application/json");

        xhr.onload = function() {
            if (xhr.status === 201) { 
                const response = JSON.parse(xhr.responseText);
                alert("질문이 성공적으로 등록되었습니다.");
                window.location.href = "qa.html";
            } else {
                errorMessage.textContent = "질문 등록에 실패했습니다. 다시 시도해주세요.";
                errorMessage.style.color = "red";
                document.querySelector(".write-container").appendChild(errorMessage);
            }
        };

        xhr.onerror = function() {
            errorMessage.textContent = "네트워크 오류가 발생했습니다. 다시 시도해주세요.";
            errorMessage.style.color = "red";
            document.querySelector(".write-container").appendChild(errorMessage);
        };

        const questionData = {
            title: title,
            content: content
        };

        xhr.send(JSON.stringify(questionData));
    });
});
