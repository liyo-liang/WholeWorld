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
 * Identifies a link tag 
 */
public class HTMLLinkNode extends HTMLTag
{
	/**
	 * The URL where the link points to
	 */	
	protected String link;
	/**
	 * The text of of the link tag
	 */
	protected String linkText;
	/**
	 * The accesskey existing inside this link.
	 */
	protected String accessKey;
	/**
	 * Constructor creates an HTMLLinkNode object, which basically stores the location
	 * where the link points to, and the text it contains.
	 * @link The URL to which the link points to
	 * @linkText The text which is stored inside this link tag
	 * @linkBegin The beginning position of the link tag
	 * @linkEnd The ending position of the link tag
	 * @accessKey The accessKey element of the link tag (valid for Compact HTML - IMODE devices)
	 */
	public HTMLLinkNode(String link,String linkText,int linkBegin, int linkEnd, String accessKey)
	{
		super(linkBegin,linkEnd,"");
		this.link = link;
		this.linkText = linkText;
		this.accessKey = accessKey;
	}
	/**
	 * Returns the accesskey element if any inside this link tag
   */
	public String getAccessKey()
	{
		return accessKey;
	}
	/**
	 * Returns the url as a string, to which this link points
	 */
	public String getLink()
	{
		return link;
	}
	/**
	 * Returns the text contained inside this link tag
	 */
	public String getLinkText()
	{
		return linkText;
	}
	/**
	 * Print the contents of this Link Node
	 */
	public void print()
	{
		System.out.print("Link to : "+link + "; titled : "+linkText+"; begins at : "+elementBegin()+"; ends at : "+elementEnd()+ ", AccessKey=");
		if (accessKey==null) System.out.println("null");
		else System.out.println(accessKey);
	}
	
}