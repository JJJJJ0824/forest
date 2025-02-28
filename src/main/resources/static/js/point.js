document.addEventListener("DOMContentLoaded", function () {
  console.log("point.js 로드됨!");

  const currentPointsElement = document.getElementById("currentPoints");
  const pointHistoryList = document.getElementById("pointHistoryList");
  const chargeBtn = document.querySelector('.charge-btn');
  const chargeAmountInput = document.getElementById('chargeAmount');
  const addPointsBtn = document.querySelector('.add-points-btn');
  const subtractPointsBtn = document.querySelector('.subtract-points-btn');
  const allPointsBtn = document.querySelector('.all-points-btn'); 

  let fullPointHistory = [];

  function getUserPoints() {
      fetch('/api/point/all', {
          method: 'GET',
          headers: {
              'Content-Type': 'application/json',
          },
          credentials: 'include'
      })
      .then(response => {
          if (!response.ok) {
              throw new Error("서버에서 응답을 받지 못했습니다.");
          }
          return response.json();
      })
      .then(data => {
          console.log("받은 데이터:", data);

          if (data && data.length > 0) {
              fullPointHistory = data;

              const totalPoints = data.reduce((sum, point) => sum + point.points, 0);
              currentPointsElement.textContent = totalPoints;

              renderPointHistory(fullPointHistory);
          } else {
              pointHistoryList.innerHTML = "<li>포인트 내역이 없습니다.</li>";
          }
      })
      .catch(error => {
          console.error("포인트 정보를 가져오는 중 오류 발생:", error);
          alert("포인트 정보를 로드하는 데 문제가 발생했습니다.");
      });
  }

  function renderPointHistory(history) {
      pointHistoryList.innerHTML = ""; 
      if (history.length === 0) {
          pointHistoryList.innerHTML = "<li>포인트 내역이 없습니다.</li>";
      } else {
          history.forEach(point => {
              const li = document.createElement('li');
              li.innerHTML = `
                  ${point.points > 0 ? "✅ 적립" : "❌ 차감"}
                  <strong>${Math.abs(point.points)} 포인트</strong>
                  <span>(${point.eventDate}) - ${point.actionType}</span>
              `;
              pointHistoryList.appendChild(li);
          });
      }
  }

  if (chargeBtn) {
      chargeBtn.addEventListener('click', function () {
          const chargeAmount = parseInt(chargeAmountInput.value);

          if (!chargeAmount || chargeAmount <= 0) {
              alert("충전할 포인트 금액을 입력해주세요.");
              return;
          }

          fetch('/api/point/add', {
              method: 'POST',
              headers: {
                  'Content-Type': 'application/json',
              },
              credentials: 'include',
              body: JSON.stringify({
                  points: chargeAmount,
                  actionType: '충전', 
              }),
          })
          .then(response => response.json())
          .then(data => {
              if (data.date !== "") {
                  alert("포인트 충전이 완료되었습니다.");
                  getUserPoints(); 
                  chargeAmountInput.value = ""; 
              } else {
                  alert("포인트 충전에 실패했습니다.");
              }
          })
          .catch(error => {
              console.error('포인트 충전 중 오류 발생:', error);
              alert("포인트 충전 중 오류가 발생했습니다.");
          });
      });
  } else {
      console.error("charge-btn 요소를 찾을 수 없습니다.");
  }

  addPointsBtn.addEventListener("click", function () {
      const chargedPoints = fullPointHistory.filter(point => point.points > 0);
      renderPointHistory(chargedPoints);
  });

  subtractPointsBtn.addEventListener("click", function () {
      const usedPoints = fullPointHistory.filter(point => point.points < 0);
      renderPointHistory(usedPoints);
  });

  allPointsBtn.addEventListener("click", function () {
      renderPointHistory(fullPointHistory); 
  });

  getUserPoints();
});
