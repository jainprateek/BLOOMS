// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SearchResult.java

package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator;

import java.util.ArrayList;

// Referenced classes of package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator:
//            SearchResultBean

public class SearchResult
{

    public SearchResult(String searchPattern)
    {
        this.searchPattern = "";
        resultList = null;
        sizeOfResult = 0;
        this.searchPattern = searchPattern;
    }

    public ArrayList<WebResults> getResultList()
    {
        return resultList;
    }

    public static void main(String args1[])
    {
    }

    void addSearchResult(WebResults webResults)
    {
        if(resultList == null)
        {
            resultList = new ArrayList<WebResults>();
            resultList.add(webResults);
        } else
        {
            resultList.add(webResults);
        }
    }

    String searchPattern;
    ArrayList<WebResults> resultList;
    int sizeOfResult;
}
