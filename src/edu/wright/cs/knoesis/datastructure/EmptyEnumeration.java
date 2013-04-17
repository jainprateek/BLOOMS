// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EmptyEnumeration.java

package edu.wright.cs.knoesis.datastructure;

import java.util.Enumeration;

public class EmptyEnumeration
    implements Enumeration
{

    public EmptyEnumeration()
    {
    }

    public static Enumeration getInstance()
    {
        return enumeration;
    }

    public boolean hasMoreElements()
    {
        return false;
    }

    public Object nextElement()
    {
        return null;
    }

    protected static Enumeration enumeration = new EmptyEnumeration();

}
