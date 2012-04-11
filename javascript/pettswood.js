//

$(document).ready(function () {
    $("h1").click(function() {
        var url = window.location.href;
        window.location = swap(url);
    });

    function swap(url) {
        if (url.match(new RegExp("src/test/resources"))) return url.replace("src/test/resources", "target/pettswood");
        if (url.match(new RegExp("target/pettswood"))) return url.replace("target/pettswood", "src/test/resources");
        throw "This document appears to be neither the source nor the result of a Pettswood test"
    }
});