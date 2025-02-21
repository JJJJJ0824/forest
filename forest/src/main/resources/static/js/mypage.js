const buttons = document.querySelectorAll('button');
const leftSide = document.getElementById('left-side');
const center = document.getElementById('center');
const rightSide = document.getElementById('right-side');

// 버튼 클릭 시 active 클래스 토글
buttons.forEach(button => {
    button.addEventListener('click', function() {
        // 모든 버튼에서 'active' 클래스 제거
        buttons.forEach(btn => btn.classList.remove('active'));
        
        // 클릭된 버튼에 'active' 클래스 추가
        this.classList.add('active');
        
        // 모든 섹션 숨기기
        leftSide.classList.remove('active');
        center.classList.remove('active');
        rightSide.classList.remove('active');
        
        // 클릭된 버튼에 해당하는 섹션만 보이게 하기
        if (this.id === 'btn-info') {
            leftSide.classList.add('active');
        } else if (this.id === 'btn-checklist') {
            center.classList.add('active');
        } else if (this.id === 'btn-courses') {
            rightSide.classList.add('active');
        }
    });
});
