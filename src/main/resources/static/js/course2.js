document.addEventListener("DOMContentLoaded", () => {
    fetchCourses(); // 기본적으로 전체 강의 로드

    const categoryButtons = document.querySelectorAll(".category-nav a");
    categoryButtons.forEach(button => {
        button.addEventListener("click", (event) => {
            event.preventDefault();
            const category = button.getAttribute("data-category");
            console.log("클릭한 카테고리:", category);
            fetchCourses(category);
        });
    });
});

function fetchCourses() {
    fetch("/api/course/all")
        .then(response => response.json())
        .then(data => {
            console.log("서버에서 받은 데이터:", data);
            data.forEach(course => {
                console.log(`강의 제목: ${course.title}, 카테고리: ${course.categoryName}`);
            });
        })
        .catch(error => console.error("Error loading courses:", error));
}
function renderCourses(courses) {
    const courseContainer = document.getElementById("course-container");
    courseContainer.innerHTML = "";

    if (courses.length === 0) {
        courseContainer.innerHTML = "<p>해당 강의가 없습니다.</p>";
        return;
    }

    courses.forEach(course => {
        const categoryName = course.categoryName || "common"; // 기본값 설정

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
                <a href="#">자세히 보기</a>
            </div>
        `;
        
        courseContainer.appendChild(courseCard);
    });
}


function getImageName(categoryName) {
    switch (categoryName) {
        case "자유여행": return "activity";
        case "패키지여행": return "package";
        case "가족여행": return "hotel";
        case "공통": return "moneyChange";
        default: return "logo"; // 기본 이미지
    }
}

document.querySelectorAll('.category-nav a').forEach(link => {
    link.addEventListener('click', function (event) {
        event.preventDefault(); // 기본 링크 동작 막기
        const category = this.getAttribute('data-category');
        if (category === "all") {
            fetchCourses();
        } else {
            fetchCoursesByCategory(category);
        }
    });
});