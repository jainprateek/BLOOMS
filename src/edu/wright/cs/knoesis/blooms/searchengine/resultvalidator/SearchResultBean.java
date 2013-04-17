// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SearchResultBean.java

package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator;


public class SearchResultBean
{

    public SearchResultBean(String title, String description, String url, String lastCrawled)
    {
        this.title = "";
        this.description = "";
        this.url = "";
        this.lastCrawled = "";
        this.title = title;
        this.description = description;
        this.url = url;
        this.lastCrawled = lastCrawled;
    }

    public SearchResultBean()
    {
        title = "";
        description = "";
        url = "";
        lastCrawled = "";
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getLastCrawled()
    {
        return lastCrawled;
    }

    public void setLastCrawled(String lastCrawled)
    {
        this.lastCrawled = lastCrawled;
    }

    String title;
    String description;
    String url;
    String lastCrawled;
}
