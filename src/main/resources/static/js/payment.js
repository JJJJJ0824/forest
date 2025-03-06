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

function getSelectedCourseId() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
}

function checkIfPurchased() {
    const courseId = getSelectedCourseId();
    const applyButton = document.getElementById('applyForCourseBtn');
    const addToCartButton = document.getElementById('addToCartBtn');

    fetch(`/api/completion/complete/${courseId}`, { credentials: 'include' })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP 오류! 상태 코드: ${response.status}`);
            }
            return response.json();
        })
        .then(hasPurchased => {
            console.log("구매 여부 응답:", hasPurchased);
            if (hasPurchased) { 
                if (applyButton) {
                    applyButton.style.display = "none"; 
                    console.log("이미 결제된 강의 - 수강 신청 버튼 숨김!");
                }
                if (addToCartButton) {
                    addToCartButton.style.display = "none"; 
                    console.log("이미 결제된 강의 - 장바구니 버튼 숨김!");
                }
            }
        })
        .catch(error => console.error("구매 확인 오류:", error));
}

function applyForCourse(event) {
    if (event) event.preventDefault(); 

    console.log("수강 신청 버튼 클릭됨!"); 

    const courseId = getSelectedCourseId(); 
    const pointsToUse = getCoursePrice(courseId); 
    const applyButton = document.getElementById('applyForCourseBtn');
    const addToCartButton = document.getElementById('addToCartBtn');

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
        alert("결제가 완료되었습니다!");

        checkIfPurchased();  

        const redirectUrls = {
            1: "/maincourse1.html",
            2: "/maincourse2.html",
            3: "/maincourse3.html",
            4: "/maincourse4.html",
            5: "/maincourse5.html",
            6: "/maincourse6.html",
            7: "/maincourse7.html",
            8: "/maincourse8.html",
            9: "/maincourse9.html",
            10: "/maincourse10.html",
            11: "/maincourse11.html",
            12: "/maincourse12.html",
            13: "/maincourse13.html",
            14: "/maincourse14.html",
            15: "/maincourse15.html",
            16: "/maincourse16.html",
            17: "/maincourse17.html",
            18: "/maincourse18.html"
        };

        const redirectUrl = redirectUrls[courseId] || "/maincourse.html";
        setTimeout(() => {
            window.location.href = redirectUrl;
        }, 500);
    })
    .catch(error => {
        console.error("결제 오류:", error);
        alert("결제 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
    });
}

document.addEventListener("DOMContentLoaded", checkIfPurchased);
