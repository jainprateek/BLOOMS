// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   IBloomsNode.java

package edu.wright.cs.knoesis.datastructure;

import java.util.HashMap;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

public interface IBloomsNode
    extends Cloneable, MutableTreeNode, TreeNode
{

    public abstract IBloomsNode getFirstChild();

    public abstract HashMap commonNodes(IBloomsNode ibloomsnode);
}
