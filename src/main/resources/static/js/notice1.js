document.addEventListener('DOMContentLoaded', function() {
    const title = document.querySelector(".notice-title");
    const date = document.querySelector(".notice-date");
    const content = document.querySelector(".notice-content > p");
  
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get("id");
    
    let xhr = new XMLHttpRequest();
    xhr.open("GET", "/api/notice/" + id, true); 
  
    xhr.onload = function() {
        if (xhr.status === 200||xhr.status === 400) {
            let notice = JSON.parse(xhr.responseText);
            console.log(notice);
            title.textContent = notice.title;
            date.textContent = notice.createdAt;
            content.textContent = notice.content;
            
        } else {
            console.error("공지 사항을 불러오는 데 실패했습니다.");
        }
    };
  
    xhr.onerror = function() {
        console.error("네트워크 오류가 발생했습니다.");
    };
  
    xhr.send();
  });
