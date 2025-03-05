document.addEventListener('DOMContentLoaded', function() {
  const urlParams = new URLSearchParams(window.location.search);
  const noticeId = urlParams.get('id'); // URL에서 id 파라미터 값 가져오기

  if (!noticeId) {
    console.error("공지사항 ID가 URL에 없습니다.");
    return;
  }

  let xhr = new XMLHttpRequest();
  xhr.open("GET", `/api/notice/${noticeId}`, true); // 개별 공지사항 조회 API 호출

  xhr.onload = function() {
    if (xhr.status === 200) {
      let notice = JSON.parse(xhr.responseText);
      console.log(notice);

      // 공지사항 제목과 날짜
      const noticeTitle = document.querySelector('.notice-title');
      const noticeDate = document.querySelector('.notice-date');
      noticeTitle.textContent = notice.title;
      noticeDate.textContent = new Date(notice.createdAt).toLocaleDateString();

      // 공지사항 내용
      const noticeContent = document.querySelector('.notice-content p');
      noticeContent.textContent = notice.content;

      // 첨부 파일
      const noticeFileLink = document.querySelector('.notice-file a');
      if (notice.file) {
        noticeFileLink.textContent = notice.file.name;
        noticeFileLink.href = `/uploads/${notice.file.name}`; // 첨부파일 링크
      } else {
        document.querySelector('.notice-file').style.display = 'none'; // 파일이 없으면 파일 정보 숨기기
      }
    } else {
      console.error("공지사항을 불러오는 데 실패했습니다.");
    }
  };

  xhr.onerror = function() {
    console.error("네트워크 오류가 발생했습니다.");
  };

  xhr.send();
});
