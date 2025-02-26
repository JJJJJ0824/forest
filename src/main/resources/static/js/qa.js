// qa.js
document.addEventListener('DOMContentLoaded', function() {
    const qaList = document.getElementById("qaList");

    if (!qaList) {
        console.error("qaList 요소를 찾을 수 없습니다.");
        return; 
    }
  
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "/api/q_a/all", true); 

    xhr.onload = function() {
        if (xhr.status === 200) {
            let questions = JSON.parse(xhr.responseText);

            questions.forEach((question) => {
                const listItem = document.createElement("li");
                listItem.classList.add("qna-item");
                
                listItem.innerHTML = `
                    <a href="q1.html?id=${question.id}">${question.title}</a>
                    <span class="date">${new Date(question.createdAt).toLocaleDateString()}</span>
                `;
                
                // 생성된 li 요소를 qaList에 추가
                qaList.appendChild(listItem);
            });
        } else {
            console.error("질문 목록을 불러오는 데 실패했습니다.");
        }
    };

    // 네트워크 오류 처리
    xhr.onerror = function() {
        console.error("네트워크 오류가 발생했습니다.");
    };

    // 요청을 보냄
    xhr.send();
});
