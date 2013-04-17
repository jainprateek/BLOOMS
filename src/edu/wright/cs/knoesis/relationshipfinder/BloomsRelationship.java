// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BloomsRelationship.java

package edu.wright.cs.knoesis.relationshipfinder;

import fr.inrialpes.exmo.align.impl.BasicRelation;

public class BloomsRelationship
{

    public BloomsRelationship()
    {
    }

    public static void main(String args1[])
    {
    }

    public static BasicRelation getEquivalenceRelation()
    {
        BasicRelation rel = new BasicRelation("=");
        return rel;
    }

    public static BasicRelation getSubClassRelation()
    {
        BasicRelation rel = new BasicRelation("<");
        return rel;
    }
}
