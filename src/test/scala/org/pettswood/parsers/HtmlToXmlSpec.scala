package org.pettswood.parsers

import org.specs2.mutable.Specification

class HtmlToXmlSpec extends Specification {
  "dodgy HTML is converted to well-formed XML" in {
    val dodgyHtml = "" +
      "<html>" +
      "    <head>" +
      "        <link href=\"css/pettswood.css\" rel=\"stylesheet\" type=\"text/css\">" +
      "    </head>" +
      "    <body>&larr;&rarr; <br></body>" +
      "</html>"

    HtmlToXml(dodgyHtml) must ==/ (
      <html>
        <head>
          <link href="css/pettswood.css" type="text/css" rel="stylesheet"></link>
        </head>
        <body>
          ←→
          <br></br>
        </body>
      </html>
    )
  }
}