// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WikipediaCategorySearchServiceWrapper.java

package edu.wright.cs.knoesis.blooms.module.wikipedia.wikipediaop;

import edu.wright.cs.knoesis.blooms.module.wikipedia.xmlparser.SAXWikiWSParser;
import edu.wright.cs.knoesis.constants.WikipediaSearchFormats;
import edu.wright.cs.knoesis.constants.WikipediaURLS;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

// Referenced classes of package edu.wright.cs.knoesis.blooms.module.wikipedia.wikipediaop:
//            WikipediaWebServiceWrapperI, InvokeWikipediaWebService

public class WikipediaCategorySearchServiceWrapper
    implements WikipediaWebServiceWrapperI
{

    public WikipediaCategorySearchServiceWrapper(String url, String term, String format, int resultLimit, boolean includeHiddenCat)
    {
        this.url = "";
        this.format = "";
        this.resultLimit = 500;
        this.term = "";
        this.includeHiddenCat = false;
        this.url = url;
        this.term = term;
        this.format = format;
        this.resultLimit = resultLimit;
        this.includeHiddenCat = includeHiddenCat;
    }

    public static void main(String args[])
    {
        WikipediaCategorySearchServiceWrapper obj = new WikipediaCategorySearchServiceWrapper(WikipediaURLS.CATEGORY_SEARCH_URL, "Category:People by political orientation", WikipediaSearchFormats.XML_FORMAT, 10, false);
        obj.invokeService();
    }

    public List invokeService()
    {
        String hidden = "!hidden";
        if(includeHiddenCat)
            hidden = "hidden";
        try
        {
            term = URLEncoder.encode(term, "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        String serviceURL = (new StringBuilder(String.valueOf(url))).append(term).append("&").append(format).append("&cllimit=").append(resultLimit).append("&clshow=").append(hidden).toString();
        InvokeWikipediaWebService invokeService = new InvokeWikipediaWebService(serviceURL);
        invokeService.invokeWebService();
        String catContent = invokeService.getContent();
        SAXWikiWSParser parser = new SAXWikiWSParser(catContent);
        List catList = parser.parse();
        return catList;
    }

    String url;
    String format;
    int resultLimit;
    String term;
    boolean includeHiddenCat;
}
