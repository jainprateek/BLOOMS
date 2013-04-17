// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   APINameSpaceContext.java

package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;

public class APINameSpaceContext
    implements NamespaceContext
{

    public APINameSpaceContext()
    {
    }

    public String getNamespaceURI(String prefix)
    {
        if(prefix == null)
            throw new NullPointerException("Null prefix");
        if("api".equals(prefix))
            return "http://schemas.microsoft.com/LiveSearch/2008/04/XML/element";
        if("web".equals(prefix))
            return "http://schemas.microsoft.com/LiveSearch/2008/04/XML/web";
        else
            return "";
    }

    public String getPrefix(String uri)
    {
        throw new UnsupportedOperationException();
    }

    public Iterator getPrefixes(String arg0)
    {
        throw new UnsupportedOperationException();
    }

    static final String WEB_NAMESPACE = "http://schemas.microsoft.com/LiveSearch/2008/04/XML/web";
    static final String API_NAMESPACE = "http://schemas.microsoft.com/LiveSearch/2008/04/XML/element";
    static final String SPELL_NAMESPACE = "http://schemas.microsoft.com/LiveSearch/2008/04/XML/spell";
    static final String RS_NAMESPACE = "http://schemas.microsoft.com/LiveSearch/2008/04/XML/relatedsearch";
    static final String PB_NAMESPACE = "http://schemas.microsoft.com/LiveSearch/2008/04/XML/phonebook";
    static final String MM_NAMESPACE = "http://schemas.microsoft.com/LiveSearch/2008/04/XML/multimedia";
    static final String AD_NAMESPACE = "http://schemas.microsoft.com/LiveSearch/2008/04/XML/ads";
    static final String IA_NAMESPACE = "http://schemas.microsoft.com/LiveSearch/2008/04/XML/instantanswer";
    static final String NEWS_NAMESPACE = "http://schemas.microsoft.com/LiveSearch/2008/04/XML/news";
    static final String ENCARTA_NAMESPACE = "http://schemas.microsoft.com/LiveSearch/2008/04/XML/encarta";
}
