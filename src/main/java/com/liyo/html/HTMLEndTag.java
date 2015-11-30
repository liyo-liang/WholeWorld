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

// HTMLParser Library v0.82 - A java-based parser for HTML
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

/**
 * HTMLEndTag can identify closing tags, like &lt;/A&gt;, &lt;/FORM&gt;, etc.
 */
public class HTMLEndTag implements HTMLNode {
    /**
     * Holds the contents of the end tag
     */
    String tagContents;
    /**
     * Holds the beginning position of the tag
     */
    int tagBegin;
    /**
     * Holds the end position of the tag
     */
    int tagEnd;

    /**
     * Constructor takes 3 arguments to construct an HTMLEndTag object.
     *
     * @param tagBegin Beginning position of the end tag
     * @param tagEnd Ending position of the end tag
     * @param tagContents Text contents of the tag
     */
    public HTMLEndTag(int tagBegin, int tagEnd, String tagContents) {
        // Initialize member variables with data provided inside the constructor
        this.tagBegin = tagBegin;
        this.tagEnd = tagEnd;
        this.tagContents = tagContents;
    }

    /**
     * Return the contents of the tag text
     */
    public String getContents() {
        return tagContents;
    }

    /**
     * Return the beginning position of the tag
     */
    public int elementBegin() {
        return tagBegin;
    }

    /**
     * Return the ending position of the tag
     */
    public int elementEnd() {
        return tagEnd;
    }

    /**
     * Locate the end tag withing the input string, by parsing from the given position
     *
     * @param input Input String
     * @param position Position to start parsing from
     */
    public static HTMLNode find(String input, int position) {
        int state = 0;
        String tagContents = "";
        int tagBegin = 0;
        int tagEnd = 0;
        for (int i = position; (i < input.length() && state != 3); i++) {
            if (input.charAt(i) == '>' && state == 2) {
                state = 3;
                tagEnd = i;
            }
            if (state == 2) {
                tagContents += input.charAt(i);
            }
            if (state == 1) {
                if (input.charAt(i) == '/') {
                    state = 2;
                } else {
                    return null;
                }
            }
            if (state == 4) {
                tagContents = "";
            }
            if (input.charAt(i) == '<' && (state == 0 || state == 4)) {
                // Transition from State 0 to State 1 - Record data till > is encountered
                tagBegin = i;
                state = 1;
            }

        }
        if (state == 3) {
            return new HTMLEndTag(tagBegin, tagEnd, tagContents);
        } else {
            return null;
        }
    }

    /**
     * Print the contents of the end tag
     */
    public void print() {
        System.out.println("EndTag : " + tagContents + "; begins at : " + elementBegin() + "; ends at : " + elementEnd());
    }
}