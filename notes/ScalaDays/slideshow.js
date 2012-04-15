//

$(document).ready(function () {
    var slideIndex = 0;
    var slides = $("iframe");
    show();

    $("#timer").click(function() {
        colour();
    });

    function colour() {
        var uncoloured = $("#timer").find("td").filter(function() { return !$(this).hasClass("gone"); });
        if (uncoloured && uncoloured.length > 0) {
            uncoloured.first().addClass("gone");
            setTimeout(colour, 1000 * 60 * 5);
        }
    }

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