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
            renderCourses(data);
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
            renderCourses(data);
        })
        .catch(error => console.error("카테고리 강의 불러오기 실패:", error));
}

function renderCourses(courses) {
    const courseContainer = document.getElementById("course-container");
    courseContainer.innerHTML = "";

    if (courses.length === 0) {
        courseContainer.innerHTML = "<p>해당 강의가 없습니다.</p>";
        return;
    }

    courses.forEach(course => {
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

        <!-- 🔥 가격 요소는 API로 받아서 채움 -->
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
