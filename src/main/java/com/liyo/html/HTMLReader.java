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
//
// Incorporation of HTMLJspTag and HTMLDecTag by :
// Allen L. Fogleson
// Senior Project Manager Crunchy Technologies
// 2111 Wilson Boulevard Suite 350
// Arlington, Va 22201
// Email : afogleson@crunchytech.com
// Voice : (703) 469-2032
// Pager : (800) 826-3181

package com.liyo.html;
//////////////////
// Java Imports //
//////////////////

import java.io.*;

/**
 * HTMLReader builds on the BufferedReader, providing methods to read one element
 * at a time
 */
public class HTMLReader extends BufferedReader {
    protected int posInLine = -1;
    protected String line;
    protected HTMLNode node = null;
    protected String url;

    /**
     * The constructor takes in a reader object, and the url to be read.
     */
    public HTMLReader(Reader in, String url) {
        super(in);
        this.url = url;
    }

    /**
     * This constructor basically overrides the existing constructor in the
     * BufferedReader class.
     */

    public HTMLReader(Reader in, int len) {
        super(in, len);
    }

    /**
     * Read the next element
     *
     * @return HTMLNode - The next node
     */
    public HTMLNode readElement() throws IOException {
        if (readNextLine()) {
            //System.out.println("Read Next Line returned true");
            do {
                line = getNextLine();
            }
            while (line != null && line.length() == 0);

        } else {
            posInLine = node.elementEnd() + 1;
        }
        if (line == null) return null;
        node = HTMLStringNode.find(this, line, posInLine);
        if (node != null) return node;
        node = HTMLDecTag.find(this, line, posInLine);
        if (node != null) return node;
        node = HTMLRemarkTag.find(this, line, posInLine);
        if (node != null) return node;
        node = HTMLJspTag.find(this, line, posInLine);
        if (node != null) return node;
        node = HTMLTag.find(this, line, posInLine);
        if (node != null) {
            // See if we can identify the node
            HTMLTag tag = (HTMLTag) node;
            try {
                node = tag.scan(url, this);
                return node;
            } catch (IOException e) {
                System.err.println("Error! I/O Exception occurred while reading " + url);
            }
        }
        // If we couldn't get a string, then it is probably an end tag
        node = HTMLEndTag.find(line, posInLine);
        if (node != null) return node;

        return null;
    }

    /**
     * Do we need to read the next line ?
     *
     * @return true - yes/ false - no
     */
    protected boolean readNextLine() {
        //if (node==null) System.out.println("node=null, posInLine = "+posInLine+" current line = "+line);
        if (posInLine == -1 || node.elementEnd() + 1 == line.length()) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * Read the next line
     * @return String containing the line
     */
    public String getNextLine() {
        try {
            line = readLine();
            posInLine = 0;
            return line;
        } catch (IOException e) {
            System.err.println("I/O Exception occurred while reading!");
        }
        return null;
    }
}