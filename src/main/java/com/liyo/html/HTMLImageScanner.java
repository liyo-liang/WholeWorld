// HTMLParser Library v0.7 - A java-based parser for HTML
// Copyright (C) Dec 31, 2000 Somik Raha
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// For any questions or suggestions, you can write to me at :
// Email :somik@kizna.com
// 
// Postal Address : 
// Somik Raha
// R&D Team
// Kizna Corporation
// 2-1-17-6F, Sakamoto Bldg., Moto Azabu, Minato ku, Tokyo, 106 0046, JAPAN

package com.liyo.html;
//////////////////
// Java Imports //
//////////////////

import java.io.IOException;

/**
 * Scans for the Image Tag. This is a subclass of HTMLTagScanner, and is called using a
 * variant of the template method. If the evaluate() method returns true, that means the
 * given string contains an image tag. Extraction is done by the scan method thereafter
 * by the user of this class.
 */
public class HTMLImageScanner extends HTMLTagScanner {
    /**
     * Overriding the default constructor
     */
    public HTMLImageScanner() {
        super();
    }

    /**
     * Overriding the constructor to accept the filter
     */
    public HTMLImageScanner(String filter) {
        super(filter);
    }

    /**
     * Template Method, used to decide if this scanner can handle the Image tag type. If
     * the evaluation returns true, the calling side makes a call to scan().
     *
     * @param s The complete text contents of the HTMLTag.
     */
    public boolean evaluate(String s) {
        int state = 0;
        s = s.toUpperCase();
        for (int i = 0; (i < s.length() && state < 3); i++) {
            char ch = s.charAt(i);
            if (ch == 'I' && state == 0) {
                state = 1;
                continue;
            }
            if (ch == 'M' && state == 1) {
                state = 2;
                continue;
            }
            if (ch == 'G' && state == 2) {
                state = 3;
                continue;
            }
            if (ch == ' ') continue;
            state = 4;
        }
        if (state == 3) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Extract the location of the image, given the string to be parsed, and the url
     * of the html page in which this tag exists.
     *
     * @param s String to be parsed
     * @param url URL of web page being parsed
     */
    protected String extractImageLocn(String s, String url) {
        String link = "";
        int src = (s.toUpperCase()).indexOf("SRC");
        if (src != -1) {
            // Parse till = is reached
            int i = src + 3;
            char ch;
            do {
                ch = s.charAt(i);
                i++;
            }
            while (ch != '=');
            // Parse till locn of next char is found
            do {
                ch = s.charAt(i);
                if (ch == ' ') {
                    i++;
                }
            }
            while (ch == ' ');

            // Locn i, is the location of the beginning of the link
            s = s.substring(i, s.length());
            // s now contains the url of the image. Remove the inverted commas from the end
            int loc = s.indexOf("\"");
            if (loc != -1) {
                link = s.substring(loc + 1, s.length());
            }
            int endloc = link.indexOf("\"");
            if (endloc != 0) {
                link = link.substring(0, endloc);
            }
        }
        // check if link does not have the full path, then we shall have to
        // append it

        if (link.indexOf("http://") == -1) {
            // Extract domain info
            String temp = url;
            String dir = "";
            int n;
            do {
                n = temp.indexOf("/");
                if (n != -1) {
                    dir += temp.substring(0, n + 1);
                    temp = temp.substring(n + 1, temp.length());
                }
            }
            while (n != -1);

            link = dir + link;
        }

        // Check if there are any escape characters to be filtered out (we are
        // currently removing #38;
        link = HTMLParser.removeEscapeCharacters(link);
        return link;
    }

    /**
     * Scan the tag and extract the information related to the <IMG> tag. The url of the
     * initiating scan has to be provided in case relative links are found. The initial
     * url is then prepended to it to give an absolute link.
     * The HTMLReader is provided in order to do a lookahead operation. We assume that
     * the identification has already been performed using the evaluate() method.
     *
     * @param tag HTML Tag to be scanned for identification
     * @param url The initiating url of the scan (Where the html page lies)
     * @param reader The reader object responsible for reading the html page
     */
    public HTMLNode scan(HTMLTag tag, String url, HTMLReader reader) throws IOException {
        HTMLNode node;
        String link, linkText = "";
        int linkBegin, linkEnd;

        // Yes, the tag is a link
        // Extract the link
        link = extractImageLocn(tag.getText(), url);
        linkBegin = tag.elementBegin();
        linkEnd = tag.elementEnd();
        HTMLImageNode imageNode = new HTMLImageNode(link, linkBegin, linkEnd);
        imageNode.setThisScanner(this);
        return imageNode;
    }

}