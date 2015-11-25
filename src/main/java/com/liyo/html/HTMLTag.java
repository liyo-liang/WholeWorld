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
import java.util.Vector;
import java.util.Enumeration;
import java.io.IOException;

/**
 * HTMLTag represents a generic tag. This class allows users to register specific
 * tag scanners, which can identify links, or image references. This tag asks the
 * scanners to run over the text, and identify. It can be used to dynamically 
 * configure a parser.
 */
public class HTMLTag implements HTMLNode
{
	/**
	 * Tag contents will have the contents of the comment tag.
   */
	String tagContents;
	/** 
	 * The beginning position of the tag in the line
	 */		
	int tagBegin;
	/**
	 * The ending position of the tag in the line
	 */	
	int tagEnd;
	/**
	 * Scanners registered for this tag
	 */
	protected static Vector scanners=new Vector();
	/**
	 * Scanner associated with this tag (useful for extraction of filtering data from a
	 * HTML node)
	 */
	protected HTMLTagScanner thisScanner=null;
	/**
	 * Set the HTMLTag with the beginning posn, ending posn and tag contents
	 * @param tagBegin Beginning position of the tag
	 * @param tagEnd Ending positiong of the tag
	 * @param tagContents The contents of the tag
	 */
	public HTMLTag(int tagBegin, int tagEnd, String tagContents)
	{
		this.tagBegin = tagBegin;
		this.tagEnd = tagEnd;
		this.tagContents = tagContents;
	}
	/**
	 * Set the scanner associated with this tag
	 */
	public void setThisScanner(HTMLTagScanner scanner)
	{
		thisScanner = scanner;
	}
	/**
	 * Return the scanner associated with this tag.
	 */
	public HTMLTagScanner getThisScanner()
	{
		return thisScanner;
	}
	/**
	 * Return the text contained in this tag
	 */
	public String getText()
	{
		return tagContents;
	}
  /**
	 * Returns the beginning position of the string.
	 */	
	public int elementBegin()
	{
		return tagBegin;
	}
	/**
	 * Returns the ending position fo the tag
	 */		
	public int elementEnd()
	{
		return tagEnd;
	}
	/**
	 * Locate the tag withing the input string, by parsing from the given position
	 * @param reader HTML reader to be provided so as to allow reading of next line
	 * @param input Input String
	 * @param position Position to start parsing from
	 */			
	public static HTMLTag find(HTMLReader reader,String input,int position)
	{
		int state = 0;
		String tagContents = "";
		int tagBegin=0;
		int tagEnd=0;
		int i=position;
		while (i<input.length() && state!=2)
		{
			if (input.charAt(i)=='/' && input.charAt(i-1)=='<')
			{
				state = 3;
			}
			if (input.charAt(i)=='>' && state==1)
			{
				state = 2;
				tagEnd = i;
			}				
			if (state==1)
			{
				tagContents+=input.charAt(i);		
			}
			if (state==3)
			{
				return null;
			}
			if (input.charAt(i)=='<' && (state==0 || state==3))
			{
				// Transition from State 0 to State 1 - Record data till > is encountered
				tagBegin = i;
				state = 1;
			}
			if (state==1 && i==input.length()-1)
			{
				// We need to continue parsing to the next line
				input = reader.getNextLine();
				i=-1;
			}		
			i++;
		}
		if (state==2)
		return new HTMLTag(tagBegin,tagEnd,tagContents);
		else
		return null;	
	}
	/**
	 * Print the contents of the tag
	 */
	public void print()
	{
		System.out.println("Begin Tag : "+tagContents+"; begins at : "+elementBegin()+"; ends at : "+elementEnd());
	}
	/**
	 * Add a HTMLTagScanner
	 */
	public static void addScanner(HTMLTagScanner scanner)
	{
		scanners.add(scanner);
	}
	/**
	 * Scan the tag to see using the registered scanners, and attempt identification.
	 * @param url URL at which HTML page is located
	 * @param reader The HTMLReader that is to be used for reading the url
	 */
	public HTMLNode scan(String url,HTMLReader reader) throws IOException
	{
		boolean found=false;
		HTMLNode retVal=null;
		for (Enumeration e=scanners.elements();(e.hasMoreElements() && !found);)
		{
			HTMLTagScanner scanner = (HTMLTagScanner)e.nextElement();
			if (scanner.evaluate(tagContents))
			{
				found=true;
				retVal=scanner.scan(this,url,reader);
			}
		}
		if (!found) return this;
		else return retVal;
	}
}