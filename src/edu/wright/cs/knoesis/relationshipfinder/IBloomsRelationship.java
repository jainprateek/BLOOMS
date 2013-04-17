// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IBloomsRelationship.java

package edu.wright.cs.knoesis.relationshipfinder;

import edu.wright.cs.knoesis.datastructure.BloomsTree;
import fr.inrialpes.exmo.align.impl.BasicRelation;

public interface IBloomsRelationship
{

    public abstract BasicRelation identifyRelationship(BloomsTree bloomstree, BloomsTree bloomstree1);

    public abstract BasicRelation identifyEquivalanceRelationship(BloomsTree bloomstree, BloomsTree bloomstree1);

    public abstract BasicRelation identifySubClassRelationship(BloomsTree bloomstree, BloomsTree bloomstree1);

    public abstract Double getRelationshipStrength();

    public abstract void setRelationshipStrength(Double double1);

    public abstract BasicRelation createRelationshipObject(String s);
}
