// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WikipediaOperations.java

package edu.wright.cs.knoesis.blooms.module.wikipedia.wikipediaop;

import edu.wright.cs.knoesis.constants.WikipediaSearchFormats;
import edu.wright.cs.knoesis.constants.WikipediaURLS;
import java.util.List;

// Referenced classes of package edu.wright.cs.knoesis.blooms.module.wikipedia.wikipediaop:
//            WikipediaArticleSearchServiceWrapper, WikipediaCategorySearchServiceWrapper

public class WikipediaOperations
{

    public WikipediaOperations(String term)
    {
        this.term = "";
        this.term = term;
    }

    public static void main(String args1[])
    {
    }

    public List getArticles()
    {
        WikipediaArticleSearchServiceWrapper obj = new WikipediaArticleSearchServiceWrapper(WikipediaURLS.ARTICLE_SEARCH_URL, term, WikipediaSearchFormats.XML_FORMAT, 10);
        List articleList = obj.invokeService();
        return articleList;
    }

    public List getWikipediaCategory()
    {
        WikipediaCategorySearchServiceWrapper obj = new WikipediaCategorySearchServiceWrapper(WikipediaURLS.CATEGORY_SEARCH_URL, term, WikipediaSearchFormats.XML_FORMAT, 500, false);
        List categoryList = obj.invokeService();
        return categoryList;
    }

    String term;
}
