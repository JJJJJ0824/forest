let slideIndex = 0;

function showSlide(index) {
    const slides = document.querySelectorAll('.slide');
    if (index >= slides.length) slideIndex = 0;
    if (index < 0) slideIndex = slides.length - 1;
    
    slides.forEach((slide, i) => {
        slide.style.display = (i === slideIndex) ? 'block' : 'none';
    });
}

function moveSlide(n) {
    showSlide(slideIndex += n);
}

document.addEventListener("DOMContentLoaded", function() {
    showSlide(slideIndex);

    setInterval(() => {
        moveSlide(1);
    }, 5000); 
});

