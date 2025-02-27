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
            console.log(questions);
            
            questions.forEach((question) => {
                const listItem = document.createElement("li");
                listItem.classList.add("qna-item");

                listItem.innerHTML = `
                    <p class="title">${question.title}</p>
                    <span class="date">${new Date(question.createdAt).toLocaleDateString()}</span>
                `;
                
                const link = document.createElement("a");
                link.href = `q1.html?id=${question.id}`;
                
                link.appendChild(listItem);

                qaList.appendChild(link);
            });
        } else if(xhr.status === 400) {
            alert("Q&A 게시판은 로그인 이후에 사용 가능합니다.");
            window.location.href = "login.html"
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
