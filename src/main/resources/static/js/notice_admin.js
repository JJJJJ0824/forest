document.querySelector(".submit-button").addEventListener('click', function () {
    const title = document.getElementById('title');
    const content = document.getElementById('content');

    // 전송할 공지사항 데이터 객체
    const sendData = {
        id: 0,
        title: title.value,
        content: content.value
    }

    // 공지사항 데이터를 AJAX로 서버에 POST
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/notice/create', true);  // 공지사항 생성 API
    xhr.setRequestHeader("Content-Type", "application/json");
    
    xhr.onload = function () {
        if (xhr.status === 201) {  // 성공적으로 공지사항 생성 시
            console.log("공지사항 추가 성공");

            // 공지사항 목록 페이지로 이동
            window.location.href = "/notice.html";  // 목록 페이지로 리디렉션
        } else {
            console.error('공지사항 추가 실패');
        }
    };

    xhr.onerror = function () {
        console.error('네트워크 오류가 발생했습니다.');
    };

    // 데이터를 JSON 형태로 보내기
    xhr.send(JSON.stringify(sendData));
});