fetch('/api/course/all')
  .then(response => response.json())
  .then(data => {
    // course-container와 card-container 요소 가져오기
    const courseContainer = document.querySelector('.courses');
    const cardContainer = document.querySelector('.card-container');

    if (!courseContainer || !cardContainer) {
      console.log('course-container 또는 card-container 요소를 찾을 수 없습니다.');
      return;
    };

    // 서버에서 받은 데이터에서 첫 3개 코스를 course-container에 추가
    for (let i = 0; i < 3; i++) {
      const course = data[i];
      const courseElement = document.createElement('div');
      courseElement.classList.add('course'); // 스타일 적용을 위한 클래스

      const imageUrl = `/img/${course.courseId}.jpg`; // id에 맞는 이미지 URL 생성

      courseElement.innerHTML = `
        <a href="course_family_aboard.html?id=${course.courseId}" class="course-link">
          <img src="${imageUrl}" alt="${course.title}" class="course-image" /> <!-- 이미지 추가 -->
          <h3>${course.title}</h3>
          <p>${course.content.slice(0, 30)}...</p>
        </a>
      `;

      courseContainer.appendChild(courseElement);
    }

    // 서버에서 받은 데이터에서 4번째부터 6번째까지 코스를 card-container에 추가
    for (let i = 3; i < 6; i++) {
      const course = data[i];
      const cardElement = document.createElement('div');
      cardElement.classList.add('card'); // 스타일 적용을 위한 클래스

      const imageUrl = `/img/${course.courseId}.jpg`; // id에 맞는 이미지 URL 생성

      // 제목과 짧은 내용 추가
      cardElement.innerHTML = `
        <a href="course_family_aboard.html?id=${course.courseId}" class="course-link">
          <img src="${imageUrl}" alt="${course.title}" class="course-image" /> <!-- 이미지 추가 -->
          <h3>${course.title}</h3>
          <p>${course.content.slice(0, 30)}...</p>
        </a>
      `;

      cardContainer.appendChild(cardElement);
    }
   // 만약 현재 페이지가 `course_family_aboard.html`이면, courseId에 맞는 코스를 추가
   const urlParams = new URLSearchParams(window.location.search);
   const currentCourseId = urlParams.get('id');

   if (currentCourseId) {
     const course = data.find(item => item.courseId === parseInt(currentCourseId));

     if (course) {
       const courseTitle = document.querySelector('.course-title');
       const courseDescription = document.querySelector('.course-description');
       const courseContent = document.querySelector('.course-content');
       const courseImage = document.querySelector('.course-image');

       if (courseTitle && courseDescription && courseContent) {
         courseTitle.textContent = course.title;
         courseDescription.textContent = course.description;
         courseContent.textContent = course.content;
         courseImage.alt = course.title;
       }
     } else {
       console.error('해당 courseId에 대한 코스를 찾을 수 없습니다.');
     }
   }
 })
 .catch(error => {
   console.error('데이터를 가져오는 중 오류가 발생했습니다:', error);
 });