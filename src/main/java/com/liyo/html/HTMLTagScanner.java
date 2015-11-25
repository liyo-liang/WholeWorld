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
 * HTMLTagScanner is an abstract superclass which is subclassed to create specific 
 * scanners, that operate on a tag's strings, identify it, and can extract data from it.
 */
public abstract class HTMLTagScanner
{
	/**
	 * A filter which is used to associate this tag. The filter contains a string
	 * that is used to match which tags are to be allowed to pass through. This can
	 * be useful when one wishes to dynamically filter out all tags except one type
	 * which may be programmed later than the parser. Is also useful for command line
	 * implementations of the parser.
	 */
	protected String filter;
	/**
	 * Default Constructor, automatically registers the scanner into a static array of 
	 * scanners inside HTMLTag
	 */
	public HTMLTagScanner()
	{
		HTMLTag.addScanner(this);
		this.filter="";
	}
	/**
	 * This constructor automatically registers the scanner, and sets the filter for this
	 * tag. 
	 * @param filter The filter which will allow this tag to pass through.
	 */
	public HTMLTagScanner(String filter)
	{
		HTMLTag.addScanner(this);
		this.filter=filter;
	}
	/** 
	 * Scan the tag and extract the information related to this type. The url of the 
	 * initiating scan has to be provided in case relative links are found. The initial 
	 * url is then prepended to it to give an absolute link.
	 * The HTMLReader is provided in order to do a lookahead operation. We assume that
	 * the identification has already been performed using the evaluate() method.
	 * @param tag HTML Tag to be scanned for identification
	 * @param url The initiating url of the scan (Where the html page lies)
	 * @param reader The reader object responsible for reading the html page
	 */
	public abstract HTMLNode scan(HTMLTag tag,String url,HTMLReader reader) throws IOException;
	/**
	 * Template Method, used to decide if this scanner can handle this tag type. If the
	 * evaluation returns true, the calling side makes a call to scan().
	 * @param s The complete text contents of the HTMLTag.
	 */
	public abstract boolean evaluate(String s);
	/**
	 * Get the filter associated with this node.
	 */
	public String getFilter()
	{
		return filter;
	}
}