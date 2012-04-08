//

$(document).ready(function () {
    $("a.calloutLink").each(function () {
        var className = $(this).text().replace(/\.scala/g, "");
        var $callout = $("pre#" + className);
        $callout.hide();
        $(this).click(function (event) {
            $callout.toggle();
            event.preventDefault();
        })
    });
});


// Toggle