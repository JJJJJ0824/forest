function getCourseIdFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
}

function addToCart(courseId) {
    if (!courseId) {
        alert("강의 ID가 없습니다.");
        return;
    }

    console.log("장바구니에 추가하는 강의 ID:", courseId);

    const courseIdNumber = parseInt(courseId, 10);
    if (isNaN(courseIdNumber)) {
        alert("유효한 강의 ID가 아닙니다.");
        return;
    }

    const requestData = {
        courseId: courseIdNumber,
        purchase_status: 0, 
        category_name: "자유여행" 
    };

    console.log("전송할 데이터:", requestData);

    fetch('/api/cart/add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify(requestData)
    })
    .then(response => {
        console.log("서버 응답 상태:", response.status);
        if (!response.ok) {
            console.error("장바구니에 추가 실패:", response.statusText);
            throw new Error("장바구니에 담을 수 없습니다.");
        }
        return response.json();
    })
    .then(data => {
        console.log("서버로부터의 응답 데이터:", data);
        alert('강의가 장바구니에 추가되었습니다.');
        if (document.body.classList.contains("mypage")) {
            getCartCourses();
        }
    })
    .catch(error => {
        console.error("장바구니 추가 오류:", error);
        alert("장바구니에 추가하는 데 오류가 발생했습니다.");
    });
}

function getCartCourses() {
    const courseId = getCourseIdFromUrl(); 
    if (!courseId) {
        fetch('/api/cart/mycart', {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json'
            },
            credentials: 'include'  
        })
        .then(response => response.json()) 
        .then(cartItems => {
            console.log("카트 항목:", cartItems); 
            if (Array.isArray(cartItems)) {
                renderCartItems(cartItems); 
            } else {
                console.error("카트 항목이 배열이 아닙니다.", cartItems);
                alert("카트 정보가 잘못된 형식입니다.");
            }
        })
        .catch(error => {
            console.error("카트 정보 불러오기 실패:", error);
            alert("카트 정보를 불러오는 중 오류가 발생했습니다.");
        });
    } else {
        fetch('/api/cart/mycart', {
            method: 'GET',
            headers: { 
                'Content-Type': 'application/json'
            },
            credentials: 'include'  
        })
        .then(response => response.json())
        .then(cartItems => {
            const filteredItems = cartItems.filter(item => item.courseId == courseId);
            renderCartItems(filteredItems); 
        })
        .catch(error => {
            console.error("카트 정보 불러오기 실패:", error);
            alert("카트 정보를 불러오는 중 오류가 발생했습니다.");
        });
    }
}

function getImageName(categoryName) {
    switch (categoryName) {
        case "자유여행": return "activity";
        case "패키지여행": return "package";
        case "가족여행": return "hotel";
        case "공통": return "moneyChange";
        default: return "logo";  
    }
}

function renderCartItems(cartItems) {
    const cartItemsList = document.querySelector('.cart-items-list');

    if (!cartItemsList) {
        console.error("카트 항목을 표시할 요소를 찾을 수 없습니다.");
        return;  
    }

    cartItemsList.innerHTML = '';  

    if (cartItems.length === 0) {
        cartItemsList.innerHTML = "<p>장바구니에 강의가 없습니다.</p>";
        return;
    }

    cartItems.forEach(item => {
        const cartItemElement = document.createElement('div');
        cartItemElement.classList.add('cart-item');
        cartItemElement.innerHTML = `
            <img src="/img/${getImageName(item.categoryName)}.png" alt="${item.title}">
            <p><strong>${item.title}</strong></p>
            <p>가격: ${item.price} 포인트</p>
            <p>강의 설명: ${item.description}</p>
        `;
        cartItemsList.appendChild(cartItemElement);
    });
}

document.addEventListener("DOMContentLoaded", () => {
    const addToCartBtn = document.getElementById("addToCartBtn");
    if (addToCartBtn) {
        addToCartBtn.addEventListener("click", () => {
            const courseId = getCourseIdFromUrl();
            if (courseId) {
                addToCart(courseId); 
            } else {
                alert("강의 ID가 없습니다.");
            }
        });
    }

    if (document.body.classList.contains("mypage")) {
        getCartCourses();
    }
});
