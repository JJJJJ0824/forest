// 스크롤 버튼 표시/숨김
window.onscroll = function() {scrollFunction()};

function scrollFunction() {
    if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
        document.getElementById("topBtn").style.display = "block"; // 스크롤 20px 이상일 때 버튼 표시
    } else {
        document.getElementById("topBtn").style.display = "none"; // 그렇지 않으면 숨기기
    }
}

// 스크롤을 맨 위로 이동하는 함수
function scrollToTop() {
    document.body.scrollTop = 0; // Safari에서 스크롤 맨 위로
    document.documentElement.scrollTop = 0; // 다른 브라우저에서 스크롤 맨 위로
}