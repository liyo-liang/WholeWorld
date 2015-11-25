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
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This is the class that the user will use, either to get an iterator into 
 * the html page or to directly parse the page and print the results
 */
public class HTMLParser
{
	/**
	 * The URL or filename to be parsed.
	 */
	protected String resourceLocn;
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
	 * Creates a HTMLParser object with the location of the resource (URL or file)
	 * @param resourceLocn Either the URL or the filename (autodetects)
	 */
	public HTMLParser(String resourceLocn)
	{
		this.resourceLocn = resourceLocn;
		openConnection();
	}
	/**
	 * Opens the connection with the resource to begin reading, by creating a HTML reader
	 * object.
	 */
	private void openConnection()
	{
		try
		{
			if (resourceLocn.indexOf("http")!=-1 || resourceLocn.indexOf("www.")!=-1)
			{
				// Its a web address
				resourceLocn=removeEscapeCharacters(resourceLocn);
				resourceLocn=checkEnding(resourceLocn);
				URL url = new URL(resourceLocn);
				URLConnection uc = url.openConnection();
				reader = new HTMLReader(new InputStreamReader(uc.getInputStream(),"SJIS"),resourceLocn);
			}
			else
			reader = new HTMLReader(new FileReader(resourceLocn),resourceLocn);			
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Error! File "+resourceLocn+" not found!");
		}
		catch (MalformedURLException e)
		{
			System.err.println("Error! URL "+resourceLocn+" Malformed!");
		}			
		catch (IOException e)
		{
			System.err.println("I/O Exception occured while reading "+resourceLocn);
		}
	}
	/**
	 * Returns an iterator (enumeration) to the html nodes. Each node can be a tag/endtag/
	 * string/link/image
	 */
	public Enumeration elements()
	{
		return new Enumeration()
		{
			public boolean hasMoreElements()
			{
				if (reader==null) return false;
				try
				{
					node = reader.readElement();
					readFlag=true;
				   if (node==null)
						return false;
					else
						return true;
				}
				catch (IOException e)
				{
					System.err.println("I/O Exception occured while reading "+resourceLocn);
					return false;
				}
			}
			public Object nextElement()
			{
				try
				{
					if (!readFlag) node = reader.readElement();
					return node;
				}
				catch (IOException e)
				{
					System.err.println("I/O Exception occured while reading "+resourceLocn);
					return null;
				}
			}
		};
	}
	/**
	 * Parse the given resource, using the filter provided
	 */
	public void parse(String filter)
	{
		HTMLNode node;
		for (Enumeration e=elements();e.hasMoreElements();)
		{
			node = (HTMLNode)e.nextElement();
	  	  	if (node!=null)
			{
			 	if (filter==null)
				node.print(); 
				else
				{
					// There is a filter. Find if the associated filter of this node
					// matches the specified filter
					if (!(node instanceof HTMLTag)) continue;
					HTMLTag tag=(HTMLTag)node;
					HTMLTagScanner scanner = tag.getThisScanner();
					if (scanner==null) continue;
					String tagFilter = scanner.getFilter();
					if (tagFilter==null) continue;
					if (tagFilter.equals(filter))
							node.print();
				}		
			}
			else System.out.println("Node is null");
		}

	}
	public static String checkEnding(String link)
	{
		// Check if the link ends in html, htm, or /. If not, add a slash
		int l1 = link.indexOf("html");
		int l2 = link.indexOf("htm");
		if (l1==-1 && l2==-1)
		{
			if (link.charAt(link.length()-1)!='/')
			{
				link+="/index.html";
			} 
			return link;
		} else return link;
	}
	public static String removeEscapeCharacters(String link)
	{
		int state = 0;
		String temp = "",retVal="";
		for (int i=0;i<link.length();i++)
		{
			char ch = link.charAt(i);
			if (state==4) 
			{
				state=0;
			}
			if (ch=='#' && state==0) 
			{
				state=1;
				continue;
			}
			if (state==1)
			{
				if (ch=='3')
				{
					state=2; 
					continue;
				}
				else
				{
					state=0;
					retVal+=temp;
				}
			}		
			if (state==2)
			{
				if (ch=='8')
				{
				 	state=3;
					continue;
				}
				else
				{
					state=0;
					retVal+=temp;
				}
			}	
			if (state==3)
			{
				if (ch==';')
				{
					state=4;
					continue;
				}
				else
				{
					state=0;
					retVal+=temp;
				}
			}				
			if (state==0) retVal+=ch; else temp+=ch;
		}
		return retVal;
	}
	/*
	 * The main program, which can be executed from the command line
	 */
	public static void main(String [] args)
	{
		new HTMLLinkScanner("-l");
		new HTMLImageScanner("-i");
		if (args.length<1 || args[0].equals("-help"))
		{
			System.out.println("java -jar Parse.jar <resourceLocn/website> -l");
			System.out.println("   <resourceLocn> the name of the file to be parsed (with complete path if not in current directory)");
			System.out.println("   -l Show only the link tags extracted from the document");
			System.out.println("   -i Show only the image tags extracted from the document");
			System.out.println("   -help This screen");
			System.exit(-1);
		}
		if (args[0].indexOf("http")!=-1 || args[0].indexOf("www.")!=-1)
			System.out.println("Parsing website "+args[0]);
		else	
		System.out.println("Parsing file "+args[0]+"...");
		HTMLParser parser = new HTMLParser(args[0]);
		if (args.length==2)
		{
			parser.parse(args[1]);
		} else
		parser.parse(null);
	}
	
}