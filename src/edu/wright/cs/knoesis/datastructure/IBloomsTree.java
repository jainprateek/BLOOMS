// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IBloomsTree.java

package edu.wright.cs.knoesis.datastructure;

import java.util.HashMap;
import javax.swing.tree.TreeModel;

// Referenced classes of package edu.wright.cs.knoesis.datastructure:
//            IBloomsNode

public interface IBloomsTree
    extends TreeModel
{

    public abstract IBloomsTree copyBloomsTree();

    public abstract IBloomsNode getRoot();

    public abstract HashMap commonNodes(IBloomsTree ibloomstree);
}
