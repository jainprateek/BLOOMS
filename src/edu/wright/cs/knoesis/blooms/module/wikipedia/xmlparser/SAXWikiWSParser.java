// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SAXWikiWSParser.java

package edu.wright.cs.knoesis.blooms.module.wikipedia.xmlparser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXWikiWSParser extends DefaultHandler
{

    public SAXWikiWSParser(String content)
    {
        wikipediaCatList = new ArrayList();
        this.content = "";
        this.content = content;
    }

    private void writeToFile()
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new FileWriter("wikipedia-category.xml"));
            out.write(content);
            out.close();
        }
        catch(IOException ioexception) { }
    }

    public List parse()
    {
        writeToFile();
        parseDocument();
        List catList = getWikipediaCatList();
        return catList;
    }

    private void parseDocument()
    {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try
        {
            SAXParser sp = spf.newSAXParser();
            sp.parse("wikipedia-category.xml", this);
        }
        catch(SAXException se)
        {
            se.printStackTrace();
        }
        catch(ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch(IOException ie)
        {
            ie.printStackTrace();
        }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException
    {
        if(qName.equalsIgnoreCase("cl") || qName.equalsIgnoreCase("p"))
        {
            String category = attributes.getValue("title");
            wikipediaCatList.add(category);
        }
    }

    public void characters(char ac[], int i, int j)
        throws SAXException
    {
    }

    public void endElement(String s, String s1, String s2)
        throws SAXException
    {
    }

    public static void main(String args[])
    {
        SAXWikiWSParser spe = new SAXWikiWSParser("Wikipedia_Page.xml");
        spe.parse();
    }

    public ArrayList getWikipediaCatList()
    {
        return wikipediaCatList;
    }

    ArrayList wikipediaCatList;
    String content;
}
