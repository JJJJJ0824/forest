document.addEventListener('DOMContentLoaded', function () {
  const submitButton = document.getElementById("submit");
  const resultSection = document.getElementById("resultSection");
  const resultText = document.getElementById("resultText");

  submitButton.addEventListener('click', function (event) {
      event.preventDefault();
      const formData = new FormData(document.getElementById("checklistForm"));
      const answers = {};

      formData.forEach((value, key) => {
          if (!answers[key]) {
              answers[key] = [];
          }
          answers[key].push(value);
      });

      fetch('/api/checklist/submit', {
          method: 'POST',
          headers: {
              'Content-Type': 'application/json'
          },
          body: JSON.stringify(answers)
      })
      .then(response => response.json())
      .then(data => {
          resultText.textContent = "체크리스트가 제출되었습니다. 결과를 확인해주세요!";
          resultSection.style.display = "block";
      })
      .catch(error => {
          console.error('체크리스트 제출 실패:', error);
          resultText.textContent = "체크리스트 제출에 실패했습니다.";
          resultSection.style.display = "block";
      });
  });
});