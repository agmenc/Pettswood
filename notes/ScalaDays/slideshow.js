//

$(document).ready(function () {
    var slideIndex = 0;
    var slides = $("iframe");
    show();

    $('body').keydown(function($event) {
        var key = $event.which;
        if (key === 37 || key === 39) {
            if (key === 37) slideIndex--; else slideIndex++;
            show();
        }
    });

    function show() {
        slides.each(function(index) {
            if (index === slideIndex) $(this).show(); else $(this).hide();
        });
        $("#counter").text(slideIndex + 1);
    }
});