//

$(document).ready(function () {
    new Toggler($("h1"));
});

function Toggler($toggleElement) {
    $toggleElement.click(function() {
        var url = window.location.href;
        window.location = swap(url);
    });

    function swap(url) {
        var srcDir = new RegExp("src/[a-zA-Z]*/resources");
        if (url.match(srcDir)) return url.replace(srcDir, "target");
        if (url.match(new RegExp("/target/"))) return url.replace("/target/", "/src/test/resources/");
        throw "This document appears to be neither the source nor the result of a Pettswood test"
    }
}
