// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ChildNotAllowedException.java

package edu.wright.cs.knoesis.blooms.module.wordnet.exception;

import java.io.PrintStream;

public class ChildNotAllowedException extends Exception
{

    public ChildNotAllowedException()
    {
        System.err.println("Node is not allowed to have children. The allowsChildren attribute of the intended parent is false");
    }

    private static final long serialVersionUID = 1L;
}
