let currentPage = 1; // 현재 페이지
let filteredCourses = []; // 필터링된 강의 배열 (카테고리별 강의)
const itemsPerPage = 6; // 한 페이지에 표시할 강의 개수
let currentCategory = 'all'; // 현재 선택된 카테고리 (기본값은 전체)

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

            currentCategory = category; // 현재 카테고리 변경
            currentPage = 1; // 카테고리 변경 시 첫 페이지로 초기화
            if (category === "all") {
                fetchCourses(); // 전체 강의 리스트
            } else {
                fetchCoursesByCategory(category); // 카테고리별 강의
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
            filteredCourses = data;  // 전체 강의 리스트
            renderCourses(); // 강의 렌더링
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
            filteredCourses = data;  // 카테고리별 강의 리스트
            renderCourses(); // 강의 렌더링
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

    // 페이지에 맞는 강의만 추출 (pagination)
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
                <img src="/img/${getImageName(course.categoryName)}.png" alt="${course.title}">
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

    // 페이지네이션 버튼 렌더링
    renderPagination();
}

function renderPagination() {
    const totalItems = filteredCourses.length;
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    const paginationContainer = document.getElementById("pagination-container");
    paginationContainer.innerHTML = "";

    // 이전 버튼
    const prevBtn = document.createElement("button");
    prevBtn.innerText = "‹";
    prevBtn.disabled = currentPage === 1;
    prevBtn.addEventListener("click", () => {
        if (currentPage > 1) {
            currentPage--;
            renderCourses(); // 강의 목록 갱신
        }
    });
    paginationContainer.appendChild(prevBtn);

    // 페이지 버튼들
    for (let i = 1; i <= totalPages; i++) {
        const pageBtn = document.createElement("button");
        pageBtn.innerText = i;
        pageBtn.classList.toggle("active", i === currentPage);
        pageBtn.addEventListener('click', () => {
            currentPage = i;
            renderCourses(); // 강의 목록 갱신
        });
        paginationContainer.appendChild(pageBtn);
    }

    // 다음 버튼
    const nextBtn = document.createElement("button");
    nextBtn.innerText = "›";
    nextBtn.disabled = currentPage === totalPages;
    nextBtn.addEventListener("click", () => {
        if (currentPage < totalPages) {
            currentPage++;
            renderCourses(); // 강의 목록 갱신
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
        <img src="/img/${getImageName(course.categoryName)}.png" alt="${course.title}">
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

function getImageName(categoryName) {
    switch (categoryName) {
        case "자유여행": return "activity";
        case "패키지여행": return "package";
        case "가족여행": return "hotel";
        case "공통": return "moneyChange";
        default: return "logo";
    }
}
