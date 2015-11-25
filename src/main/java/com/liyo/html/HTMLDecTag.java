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
// Class HTMLDecTag written by :
// Allen L. Fogleson
// Senior Project Manager Crunchy Technologies
// 2111 Wilson Boulevard Suite 350
// Arlington, Va 22201
// Email : afogleson@crunchytech.com
// Voice : (703) 469-2032
// Pager : (800) 826-3181
 
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
// 
// Class HTMLDecTag written by :
// Allen L. Fogleson
// Senior Project Manager Crunchy Technologies
// 2111 Wilson Boulevard Suite 350
// Arlington, Va 22201
// Email : afogleson@crunchytech.com
// Voice : (703) 469-2032
// Pager : (800) 826-3181
 
/**
 * The HTML Document Declaration Tag can identify &lt;!DOCTYPE&gt; tags
 */
public class HTMLDecTag implements HTMLNode
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
	 * The HTMLDecTag is constructed by providing the beginning posn, ending posn
	 * and the tag contents.
	 * @param tagBegin beginning position of the tag
	 * @param tagEnd ending position of the tag
	 * @param tagContents contents of the remark tag
	 */
	public HTMLDecTag(int tagBegin, int tagEnd, String tagContents)
	{
		this.tagBegin = tagBegin;
		this.tagEnd = tagEnd;
		this.tagContents = tagContents;
	}
	/**
	 * Returns the text contents of the comment tag.
	 */
	public String getText()
	{
		return this.tagContents;
	}
	/**
	 * Returns the beginning position of the tag.
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
	 * Locate the remark tag withing the input string, by parsing from the given position
	 * @param reader HTML reader to be provided so as to allow reading of next line
	 * @param input Input String
	 * @param position Position to start parsing from
	 */
	public static HTMLDecTag find(HTMLReader reader,String input,int position)
	{
		int state = 0;
		StringBuffer tagContent = new StringBuffer();
		int tagBegin=0;
		int tagEnd=0;
		int i=position;
		while (i<input.length() && state!=7)
		{
			if (i>0 && input.charAt(i)=='/' && input.charAt(i-1)=='<' )
			{
		 state = 8;
		}
	  if (state==2 && input.charAt(i)=='<')
	  {
	  	state = 8;
	  }
			if (state==3 && input.charAt(i)=='>')
			{
				state=7;
			  tagEnd=i;
			}
			if (state==3)
			{
		tagContent.append(input.charAt(i));
			}
			if (state==8)
			{
		return null;
			}
			if (state==2 && input.charAt(i)!='-')
			{
		state=3;
		tagContent.append(input.charAt(i));
			}
			if (state==1 && input.charAt(i)=='!')
			{
		state=2;
			}
			if (input.charAt(i)=='<' && state==0)
			{
		// Transition from State 0 to State 1 - Record data till > is encountered
		tagBegin = i;
		state = 1;
			}
			if (state>1 && state!=7 && i==input.length()-1)
			{
		// We need to continue parsing to the next line
		input = reader.getNextLine();
		i=-1;
			}
			i++;
		}
		if (state==7)
		    return new HTMLDecTag(tagBegin,tagEnd,tagContent.toString());
		else
		return null;
	}
	/**
	 * Print the contents of the remark tag.
	 */
	public void print()
	{
		System.out.println("Declaration Tag : "+tagContents+"; begins at : "+elementBegin()+"; ends at : "+elementEnd());
	}
}