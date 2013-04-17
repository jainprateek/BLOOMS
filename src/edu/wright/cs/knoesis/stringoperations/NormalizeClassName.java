// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   NormalizeClassName.java

package edu.wright.cs.knoesis.stringoperations;

import java.util.ArrayList;

public class NormalizeClassName
{

    public String getNormalizedString()
    {
        return normalizedString;
    }

    public NormalizeClassName(String classname)
    {
        this.classname = "";
        normalizedString = "";
        classnameList = null;
        normalizedStringList = null;
        this.classname = classname;
        normalizedString = classname;
    }

    public NormalizeClassName(ArrayList classname)
    {
        this.classname = "";
        normalizedString = "";
        classnameList = null;
        normalizedStringList = null;
        classnameList = classname;
        normalizedStringList = new ArrayList();
        this.classname = "";
        normalizedString = "";
    }

    public static void main(String args[])
    {
        NormalizeClassName obj = new NormalizeClassName("USAToday");
        String normalizedString = obj.normalize();
    }

    public String normalize()
    {
        replaceUnderScore();
        replaceHyphens();
        splitOnCapitalPhrase();
        trimString();
        return getNormalizedString();
    }

    public ArrayList normalizeStringList()
    {
        for(int i = 0; i < classnameList.size(); i++)
        {
            String className = (String)classnameList.get(i);
            normalizedString = className;
            replaceUnderScore();
            replaceHyphens();
            splitOnCapitalPhrase();
            trimString();
            normalizedStringList.add(getNormalizedString());
        }

        return normalizedStringList;
    }

    private String replaceUnderScore()
    {
        normalizedString = normalizedString.replaceAll("_", " ");
        return normalizedString;
    }

    private String replaceHyphens()
    {
        normalizedString = normalizedString.replaceAll("-", " ");
        return normalizedString;
    }

    private String trimString()
    {
        normalizedString = normalizedString.trim();
        return normalizedString;
    }

    private String splitOnCapitalPhrase()
    {
        normalizedString = normalizedString.replaceAll("([A-Z])(?![A-Z])", " $1");
        normalizedString = normalizedString.trim();
        return normalizedString;
    }

    String classname;
    String normalizedString;
    ArrayList classnameList;
    ArrayList normalizedStringList;
}
