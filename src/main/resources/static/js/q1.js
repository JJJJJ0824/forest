// q1.js

document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const questionId = urlParams.get('id');  

    if (!questionId) {
        alert("잘못된 접근입니다.");
        return;
    }

    let xhr = new XMLHttpRequest();
    xhr.open("GET", `/api/q_a/${questionId}`, true); 

    xhr.onload = function() {
        if (xhr.status === 200) {
            const question = JSON.parse(xhr.responseText); 

            
            document.getElementById("question-title").textContent = question.title;
            document.getElementById("question-content").textContent = question.content;
        } else {
            console.error("질문 세부 정보를 불러오는데 실패했습니다.");
        }
    };

    xhr.onerror = function() {
        console.error("네트워크 오류가 발생했습니다.");
    };

    xhr.send();
});
