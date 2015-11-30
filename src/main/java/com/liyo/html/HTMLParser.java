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

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the class that the user will use, either to get an iterator into
 * the html page or to directly parse the page and print the results
 */
public class HTMLParser {
    /**
     * The URL or filename to be parsed.
     */
    protected String resourceUrl;
    /**
     * The html reader associated with this parser
     */
    protected HTMLReader reader;
    /**
     * The last read HTML node.
     */
    protected HTMLNode node;
    /**
     * Keeps track of whether the first reading has been performed.
     */
    protected boolean readFlag = false;

    /**
     * 当前解析的数据输入流
     */
    private InputStreamReader inputStreamReader;

    /**
     * 输入流编码格式
     */
    private String charSetName = "SJIS";
    /**
     * Creates a HTMLParser object with the location of the resource (URL or file)
     *
     * @param resourceUrl Either the URL or the filename (autodetects)
     */
    public HTMLParser(String resourceUrl) {
        this.resourceUrl = resourceUrl;
        openConnection();
    }

    public HTMLParser(InputStream inputStream, String url, String charSet){
        this.resourceUrl= url;
        if(StringUtils.isNotBlank(charSet)){
            this.charSetName = charSet;
        }
        try {
            this.inputStreamReader = new InputStreamReader(inputStream, charSetName);
            this.resourceUrl = removeEscapeCharacters(resourceUrl);
            this.resourceUrl = checkEnding(resourceUrl);
            reader = new HTMLReader(inputStreamReader, resourceUrl);
        }catch (IOException e){
            System.err.println("I/O Exception occured while reading " + resourceUrl);        }
    }

    /**
     * Opens the connection with the resource to begin reading, by creating a HTML reader
     * object.
     */
    private void openConnection() {
        try {
            if (resourceUrl.indexOf("http") != -1 || resourceUrl.indexOf("www.") != -1) {
                // Its a web address
                resourceUrl = removeEscapeCharacters(resourceUrl);
                resourceUrl = checkEnding(resourceUrl);
                URL url = new URL(resourceUrl);
                URLConnection uc = url.openConnection();
                reader = new HTMLReader(new InputStreamReader(uc.getInputStream(), charSetName), resourceUrl);
            } else {
                reader = new HTMLReader(new FileReader(resourceUrl), resourceUrl);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error! File " + resourceUrl + " not found!");
        } catch (MalformedURLException e) {
            System.err.println("Error! URL " + resourceUrl + " Malformed!");
        } catch (IOException e) {
            System.err.println("I/O Exception occured while reading " + resourceUrl);
        }
    }

    /**
     * Returns an iterator (enumeration) to the html nodes. Each node can be a tag/endtag/
     * string/link/image
     */
    public Enumeration elements() {
        return new Enumeration() {
            public boolean hasMoreElements() {
                if (reader == null) return false;
                try {
                    node = reader.readElement();
                    readFlag = true;
                    if (node == null) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (IOException e) {
                    System.err.println("I/O Exception occured while reading " + resourceUrl);
                    return false;
                }
            }

            public Object nextElement() {
                try {
                    if (!readFlag) node = reader.readElement();
                    return node;
                } catch (IOException e) {
                    System.err.println("I/O Exception occured while reading " + resourceUrl);
                    return null;
                }
            }
        };
    }

    /**
     * Parse the given resource, using the filter provided
     */
    public void parse(String filter) {
        HTMLNode node;
        for (Enumeration e = elements(); e.hasMoreElements(); ) {
            node = (HTMLNode) e.nextElement();
            if (node != null) {
                if (filter == null) {
                    node.print();
                } else {
                    // There is a filter. Find if the associated filter of this node
                    // matches the specified filter
                    if (!(node instanceof HTMLTag)) continue;
                    HTMLTag tag = (HTMLTag) node;
                    HTMLTagScanner scanner = tag.getThisScanner();
                    if (scanner == null) continue;
                    String tagFilter = scanner.getFilter();
                    if (tagFilter == null) continue;
                    if (tagFilter.equals(filter)) {
                        node.print();
                    }
                }
            } else {
                System.out.println("Node is null");
            }
        }

    }

    public static String checkEnding(String link) {
        // Check if the link ends in html, htm, or /. If not, add a slash
        int l1 = link.indexOf("html");
        int l2 = link.indexOf("htm");
        if (l1 == -1 && l2 == -1) {
            if (link.charAt(link.length() - 1) != '/') {
                link += "/index.html";
            }
            return link;
        } else {
            return link;
        }
    }

    public static String removeEscapeCharacters(String link) {
        int state = 0;
        String temp = "", retVal = "";
        for (int i = 0; i < link.length(); i++) {
            char ch = link.charAt(i);
            if (state == 4) {
                state = 0;
            }
            if (ch == '#' && state == 0) {
                state = 1;
                continue;
            }
            if (state == 1) {
                if (ch == '3') {
                    state = 2;
                    continue;
                } else {
                    state = 0;
                    retVal += temp;
                }
            }
            if (state == 2) {
                if (ch == '8') {
                    state = 3;
                    continue;
                } else {
                    state = 0;
                    retVal += temp;
                }
            }
            if (state == 3) {
                if (ch == ';') {
                    state = 4;
                    continue;
                } else {
                    state = 0;
                    retVal += temp;
                }
            }
            if (state == 0) {
                retVal += ch;
            } else {
                temp += ch;
            }
        }
        return retVal;
    }

    /*
     * The main program, which can be executed from the command line
     */
    public static void main(String[] args) {
        new HTMLLinkScanner("-l");
        new HTMLImageScanner("-i");
        if (args.length < 1 || args[0].equals("-help")) {
            System.out.println("java -jar Parse.jar <resourceLocn/website> -l");
            System.out.println("   <resourceLocn> the name of the file to be parsed (with complete path if not in current directory)");
            System.out.println("   -l Show only the link tags extracted from the document");
            System.out.println("   -i Show only the image tags extracted from the document");
            System.out.println("   -help This screen");
            System.exit(-1);
        }
        if (args[0].indexOf("http") != -1 || args[0].indexOf("www.") != -1) {
            System.out.println("Parsing website " + args[0]);
        } else {
            System.out.println("Parsing file " + args[0] + "...");
        }
        HTMLParser parser = new HTMLParser(args[0]);
        if (args.length == 2) {
            parser.parse(args[1]);
        } else {
            parser.parse(null);
        }
    }

}