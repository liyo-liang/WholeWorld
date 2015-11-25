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
import java.io.IOException;

/**
 * Scans for the Link Tag. This is a subclass of HTMLTagScanner, and is called using a 
 * variant of the template method. If the evaluate() method returns true, that means the
 * given string contains an image tag. Extraction is done by the scan method thereafter
 * by the user of this class.
 */
public class HTMLLinkScanner extends HTMLTagScanner
{
	/**
	 * Overriding the default constructor
	 */
	public HTMLLinkScanner()
	{
		super();
	}
	/**
	 * Overriding the constructor to accept the filter 
	 */
	public HTMLLinkScanner(String filter)
	{
		super(filter);
	}
	/**
	 * Template Method, used to decide if this scanner can handle the Link tag type. If 
	 * the evaluation returns true, the calling side makes a call to scan().
	 * @param s The complete text contents of the HTMLTag.
	 */	
	public boolean evaluate(String s)
	{
		boolean retVal;
		char ch = s.charAt(0);
		if (ch=='a' || ch=='A') retVal = true; else retVal = false;
		if (retVal)
		{
			if (s.toUpperCase().indexOf("HREF")==-1)
				retVal=false;
		}
		return retVal;
	
	}
  /**
   * Extract the link from the given string. The URL of the actual html page is also 
   * provided.    
   */
	protected String extractLink(String s,String url)
	{
		String link="";
		int href = (s.toUpperCase()).indexOf("HREF");
		if (href!=-1)
		{
			s = s.substring(href+4,s.length());	
			int loc = s.indexOf("\"");
			if (loc!=-1)
			{
				link = s.substring(loc+1,s.length());
			}
			int endloc = link.indexOf("\"");
			if (endloc>0)
			{
				link = link.substring(0,endloc);
			}
		}
		// check if link does not have the full path, then we shall have to 
		// append it
		
		if (link.indexOf("http://")==-1)
		{
			// Extract domain info
			String temp=url;
			String dir=""	;
			int n;
			do
			{
				n = temp.indexOf("/");
				if (n!=-1)
				{
					dir+=temp.substring(0,n+1);
					temp=temp.substring(n+1,temp.length());
				}
			}
			while (n!=-1);
			
			link=dir+link;		
		}
		
		// Check if there are any escape characters to be filtered out (we are
		// currently removing #38;
		link=HTMLParser.removeEscapeCharacters(link);
		return link;
	}
	/**
	 * Extract the access key from the given text.
	 * @param text Text to be parsed to pick out the access key
	 */
	public String getAccessKey(String text)
	{
		// Find the occurence of ACCESSKEY in given 
		String sub = "ACCESSKEY";
		String accessKey=null;
		int n = text.toUpperCase().indexOf(sub);
		if (n!=-1)
		{
			n+=sub.length();
			// Parse the = sign
			char ch;
			do
			{
				ch = text.charAt(n);
				n++;
			}
			while (ch!='=');
			// Start parsing for a number
			accessKey = "";
			do
			{
				ch = text.charAt(n);
				if (ch>='0' && ch<='9') accessKey+=ch;
				n++;
			}
			while (ch>='0' && ch<='9' && n<text.length());
			return accessKey;
		} else return null;
	}
	/**
	 * Scan the tag and extract the information relevant to the link tag.
	 * @param tag HTML Tag to be scanned
	 * @param url The URL of the html page in which this tag is located
	 * @param reader The HTML reader used to read this url 
	 */
	public HTMLNode scan(HTMLTag tag,String url,HTMLReader reader) throws IOException
	{
		HTMLNode node;
		String link,linkText="",accessKey=null;
		int linkBegin, linkEnd;

		// Yes, the tag is a link
		// Extract the link
		link = extractLink(tag.getText(),url);
		accessKey = getAccessKey(tag.getText());
		linkBegin = tag.elementBegin();
		// Get the next element, which is string, till </a> is encountered
		boolean endFlag=false;
		do
		{
			node = reader.readElement();
			if (node instanceof HTMLStringNode)
			{
				linkText+=((HTMLStringNode)node).getText();
			}
			if (node instanceof HTMLEndTag)
			{
				char ch = ((HTMLEndTag)node).getContents().charAt(0);
				if (ch=='a' || ch=='A') endFlag=true; else endFlag=false;
			}
		}
		while (endFlag==false);
		if (node instanceof HTMLEndTag)
		{
			// The link has been completed
			// Create the link object and return it
			linkEnd = node.elementEnd();
			HTMLLinkNode linkNode = new HTMLLinkNode(link,linkText,linkBegin,linkEnd,accessKey);
			linkNode.setThisScanner(this);
			return linkNode;
		}
		return null;
	}

}