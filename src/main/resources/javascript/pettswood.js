//

$(document).ready(function () {
    new Toggler($("h1"));
    new Editor($("h2"));
});

function Toggler($toggleElement) {
    $toggleElement.click(function() {
        var url = window.location.href;
        window.location = swap(url);
    });

    function swap(url) {
        if (url.match(new RegExp("src/test/resources"))) return url.replace("src/test/resources", "target/pettswood");
        if (url.match(new RegExp("target/pettswood"))) return url.replace("target/pettswood", "src/test/resources");
        throw "This document appears to be neither the source nor the result of a Pettswood test"
    }
}

function Editor($saveElement) {
    /*
    Generated HTML needs the following fixes:
        Tags to close:
            Close all <link> tags in <head>
            Close all <br> tags
     */
    var allHtml = '<!DOCTYPE HTML>\n<html>\n' + $("html").html() + '\n</html>';
    console.log(allHtml);
    $saveElement.click(function() {
        var uriContent = "data:application/octet-stream," + encodeURIComponent(allHtml);
//        var uriContent = "data:application/octet-stream," + encodeURIComponent(allHtml);
        window.open(uriContent, 'Save');
        event.preventDefault();
    } )
}