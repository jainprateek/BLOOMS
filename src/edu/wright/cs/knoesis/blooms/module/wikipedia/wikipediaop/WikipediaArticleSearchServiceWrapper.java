// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WikipediaArticleSearchServiceWrapper.java

package edu.wright.cs.knoesis.blooms.module.wikipedia.wikipediaop;

import edu.wright.cs.knoesis.blooms.module.wikipedia.xmlparser.SAXWikiWSParser;
import edu.wright.cs.knoesis.constants.WikipediaSearchFormats;
import edu.wright.cs.knoesis.constants.WikipediaURLS;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

// Referenced classes of package edu.wright.cs.knoesis.blooms.module.wikipedia.wikipediaop:
//            WikipediaWebServiceWrapperI, InvokeWikipediaWebService

public class WikipediaArticleSearchServiceWrapper
    implements WikipediaWebServiceWrapperI
{

    public WikipediaArticleSearchServiceWrapper(String url, String term, String format, int resultLimit)
    {
        this.url = "";
        this.format = "";
        this.resultLimit = 10;
        this.term = "";
        this.url = url;
        this.term = term;
        this.format = format;
        this.resultLimit = resultLimit;
    }

    public static void main(String args[])
    {
        WikipediaArticleSearchServiceWrapper obj = new WikipediaArticleSearchServiceWrapper(WikipediaURLS.ARTICLE_SEARCH_URL, "south park", WikipediaSearchFormats.XML_FORMAT, 10);
        obj.invokeService();
    }

    public List invokeService()
    {
        try
        {
            term = URLEncoder.encode(term, "UTF-8");
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        String serviceURL = (new StringBuilder(String.valueOf(url))).append(term).append("&").append(format).append("&srlimit=").append(resultLimit).toString();
        InvokeWikipediaWebService invokeWS = new InvokeWikipediaWebService(serviceURL);
        invokeWS.invokeWebService();
        String content = invokeWS.getContent();
        SAXWikiWSParser saxParser = new SAXWikiWSParser(content);
        List articleList = saxParser.parse();
        return articleList;
    }

    String url;
    String format;
    int resultLimit;
    String term;
}
