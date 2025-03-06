let currentPage = 1; 
let filteredCourses = []; 
const itemsPerPage = 6; 
let currentCategory = 'all';

document.addEventListener("DOMContentLoaded", () => {
    console.log("course.js 로드됨!");

    const courseId = getCourseIdFromUrl();
    if (courseId) {
        handleCourseDetailPage();
    } else {
        handleCourseListPage(); 
    }
});

function getCourseIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return params.get("id");
}

function handleCourseListPage() {
    fetchCourses(); 

    const categoryButtons = document.querySelectorAll(".category-nav a");
    categoryButtons.forEach(button => {
        button.addEventListener("click", (event) => {
            event.preventDefault();
            const category = button.getAttribute("data-category");
            console.log("클릭한 카테고리:", category);

            currentCategory = category; 
            currentPage = 1; 
            if (category === "all") {
                fetchCourses(); 
            } else {
                fetchCoursesByCategory(category); 
            }
        });
    });
}

function handleCourseDetailPage() {
    const courseId = getCourseIdFromUrl();
    console.log("URL에서 가져온 ID:", courseId);

    if (courseId) {
        fetchCourseDetail(courseId);
    } else {
        alert("강의 ID가 없습니다. 목록으로 돌아갑니다.");
        window.location.href = "/course.html";
    }
}

function fetchCourses() {
    fetch('/api/course/all')
        .then(response => {
            if (!response.ok) throw new Error("강의 데이터를 가져올 수 없습니다.");
            return response.json();
        })
        .then(data => {
            console.log("서버에서 받은 전체 강의 데이터:", data);
            filteredCourses = data; 
            renderCourses(); 
        })
        .catch(error => console.error("강의 목록 불러오기 실패:", error));
}

function fetchCoursesByCategory(categoryName) {
    fetch(`/api/course/type/${categoryName}`)
        .then(response => {
            if (!response.ok) throw new Error("카테고리별 강의를 가져올 수 없습니다.");
            return response.json();
        })
        .then(data => {
            console.log(`${categoryName} 카테고리 강의 데이터:`, data);
            filteredCourses = data; 
            renderCourses(); 
        })
        .catch(error => console.error("카테고리 강의 불러오기 실패:", error));
}

function renderCourses() {
    const courseContainer = document.getElementById("course-container");
    courseContainer.innerHTML = "";

    if (filteredCourses.length === 0) {
        courseContainer.innerHTML = "<p>해당 강의가 없습니다.</p>";
        return;
    }

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedCourses = filteredCourses.slice(startIndex, endIndex);

    paginatedCourses.forEach(course => {
        console.log("강의 ID 확인:", course.courseId);
        const categoryName = course.categoryName || "공통";

        const courseCard = document.createElement("div");
        courseCard.className = `content ${getCategoryClass(categoryName)}`;

        courseCard.innerHTML = `
            <div class="image-wrapper">
                <img src="/img/${course.courseId}.jpg" alt="${course.title}">
            </div>
            <div class="text-wrapper">
                <h3>${course.title}</h3>
                <p>${course.description}</p>
                <p><strong>가격:</strong> ${course.price} 포인트</p>
                <a href="/course_family_aboard.html?id=${course.courseId}">자세히 보기</a>
            </div>
        `;
        
        courseContainer.appendChild(courseCard);
    });

    renderPagination();
}

function renderPagination() {
    const totalItems = filteredCourses.length;
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    const paginationContainer = document.getElementById("pagination-container");
    paginationContainer.innerHTML = "";

    const prevBtn = document.createElement("button");
    prevBtn.innerText = "‹";
    prevBtn.disabled = currentPage === 1;
    prevBtn.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            renderCourses(); 
        }
    });
    paginationContainer.appendChild(prevBtn);

    for (let i = 1; i <= totalPages; i++) {
        const pageBtn = document.createElement("button");
        pageBtn.innerText = i;
        pageBtn.classList.toggle("active", i === currentPage);
        pageBtn.addEventListener('click', () => {
            currentPage = i;
            renderCourses();
        });
        paginationContainer.appendChild(pageBtn);
    }

    const nextBtn = document.createElement("button");
    nextBtn.innerText = "›";
    nextBtn.disabled = currentPage === totalPages;
    nextBtn.addEventListener("click", () => {
        if (currentPage < totalPages) {
            currentPage++;
            renderCourses();
        }
    });
    paginationContainer.appendChild(nextBtn);
}

function fetchCourseDetail(id) {
    const loadingMessage = document.getElementById("loading-message");

    fetch(`/api/course/${id}`)
        .then(response => {
            if (!response.ok) {
                console.error("API 응답 상태 코드:", response.status);
                throw new Error("강의 정보를 가져올 수 없습니다.");
            }
            return response.json();
        })
        .then(course => {
            console.log("가져온 강의 상세 데이터:", course);
            
            if (loadingMessage) loadingMessage.style.display = "none";
            
            renderCourseDetail(course);
        })
        .catch(error => {
            console.error("강의 상세 불러오기 실패:", error);
            if (loadingMessage) loadingMessage.innerText = "강의 정보를 불러오는 데 실패했습니다.";
        });
}

function renderCourseDetail(course) {
    const courseContainer = document.getElementById("course-container");
    const priceElement = document.getElementById("priceValue");

    if (!course) {
        courseContainer.innerHTML = "<p>강의 정보를 찾을 수 없습니다.</p>";
        return;
    }

    courseContainer.innerHTML = `
        <h2>${course.title}</h2>
        <img src="/img/${course.courseId}.jpg" alt="${course.title}">
        <p>${course.description}</p>

        <div id="course-content" contenteditable="true">
            <p>${course.content.replace(/\n/g, "<br>")}</p>
        </div>

        <p id="coursePrice"><strong>가격:</strong> <span id="priceValue">${course.price}</span> 포인트</p>
    `;

    console.log("강의 가격 렌더링 완료:", course.price);
}

function getCategoryClass(categoryName) {
    switch (categoryName) {
        case "자유여행": return "free";
        case "패키지여행": return "package";
        case "가족여행": return "family";
        case "공통": return "common";
        default: return "";
    }
}

function getImageName(courseId) {
    switch (courseId) { 
        case 1: return "1";
        case 2: return "2";   // 2.jpg
        case 3: return "3";   // 3.jpg
        case 4: return "4";   // 4.jpg
        case 5: return "5";   // 5.jpg
        case 6: return "6";   // 6.jpg
        case 7: return "7";   // 7.jpg
        case 8: return "8";   // 8.jpg
        case 9: return "9";   // 9.jpg
        case 10: return "10"; // 10.jpg
        case 11: return "11"; // 11.jpg
        case 12: return "12"; // 12.jpg
        case 13: return "13"; // 13.jpg
        case 14: return "14"; // 14.jpg
        case 15: return "15"; // 15.jpg
        case 16: return "16"; // 16.jpg
        case 17: return "17"; // 17.jpg
        case 18: return "18"; // course18.jpg
        default: return "logo"; // 기본 이미지
    }
}
window.getCoursePrice = function(courseId) {
    const priceElement = document.getElementById("priceValue");
    if (!priceElement) {
        console.error("가격 요소를 찾을 수 없습니다.");
        return null;
    }

    const priceText = priceElement.innerText;
    const price = parseInt(priceText.replace(/[^0-9]/g, ''), 10);

    console.log("실시간 강의 가격:", price);
    return price;
};
