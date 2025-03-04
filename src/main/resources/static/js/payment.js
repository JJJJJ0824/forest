function getSelectedCourseId() {
    const urlParams = new URLSearchParams(window.location.search);
    const courseId = urlParams.get('id');
    console.log("선택된 강의 ID:", courseId);
    return courseId;
}

function getCoursePrice(courseId) {
    const priceElement = document.getElementById('priceValue');
    if (!priceElement) {
        console.error("가격 요소를 찾을 수 없습니다.");
        return null;
    }
    
    const priceText = priceElement.innerText;
    const price = parseInt(priceText.replace(/[^0-9]/g, ''), 10);
    
    console.log("🔍 실시간 강의 가격:", price);
    return price;
}


function applyForCourse(event) {
    if (event) event.preventDefault(); 

    console.log("수강 신청 버튼 클릭됨!"); 

    const courseId = getSelectedCourseId(); 
    const pointsToUse = getCoursePrice(courseId); 

    if (!courseId || !pointsToUse) {
        alert("강의 정보를 불러올 수 없습니다.");
        return;
    }

    const requestData = { 
        courseId: courseId, 
        points: pointsToUse,
        actionType: "강의 구매" 
    };

    console.log("결제 요청 데이터:", requestData);

    fetch('/api/point/use', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(requestData)
    })
    .then(response => {
        console.log("HTTP 응답 상태:", response.status);
        if (!response.ok) {
            throw new Error(`HTTP 오류! 상태 코드: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log("결제 API 응답 데이터:", data);
        alert("결제가 완료되었습니다! ");
    })
    .catch(error => {
        console.error("결제 오류:", error);
        alert("결제 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
    });
}

function updateUserPoints(newPoints) {
    const pointsElement = document.getElementById('currentPoints');
    if (pointsElement) {
        pointsElement.innerText = newPoints + " 포인트";
        console.log("📊 사용자 포인트 업데이트:", newPoints);
    } else {
        console.warn("⚠️ 포인트 요소를 찾을 수 없습니다.");
    }
}
