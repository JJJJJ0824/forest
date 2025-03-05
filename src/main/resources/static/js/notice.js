document.addEventListener('DOMContentLoaded', function() {
  const noticeList = document.getElementById("notice-list");

  if (!noticeList) {
      console.error("noticeList 요소를 찾을 수 없습니다.");
      return; 
  }

  let xhr = new XMLHttpRequest();
  xhr.open("GET", "/api/notice/all", true); 
  xhr.onload = function() {
      if (xhr.status === 200||xhr.status === 400) {
          let notices = JSON.parse(xhr.responseText);
          console.log(notices);
          
          notices.forEach((notice) => {
              const listItem = document.createElement("li");
              listItem.classList.add("notice-item");

              listItem.innerHTML = `
                  <p class="title">${notice.title}</p>
                  <span class="date">${new Date(notice.createdAt).toLocaleDateString()}</span>
              `;
              
              const link = document.createElement("a");
              link.href = `notice1.html?id=${notice.id}`;
              
              link.appendChild(listItem);

              noticeList.appendChild(link);
          });
      } else {
          console.error("공지 목록을 불러오는 데 실패했습니다.");
      }
  };

  xhr.onerror = function() {
      console.error("네트워크 오류가 발생했습니다.");
  };

  xhr.send();
});
