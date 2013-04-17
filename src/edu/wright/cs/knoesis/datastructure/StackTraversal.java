// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   StackTraversal.java

package edu.wright.cs.knoesis.datastructure;

import java.io.PrintStream;
import java.util.Stack;

// Referenced classes of package edu.wright.cs.knoesis.datastructure:
//            IBloomsNode, BloomsNode

public class StackTraversal
{

    public StackTraversal()
    {
    }

    public void traverse(IBloomsNode rootNode)
    {
        Stack stack = new Stack();
        for(BloomsNode node = (BloomsNode)rootNode.getFirstChild(); node != null;)
        {
            System.out.println((new StringBuilder(String.valueOf(node.getNodeName()))).append("=").append(node.getLevel()).toString());
            if(node.hasChildNodes())
            {
                if(node.getNextSibling() != null)
                    stack.push(node.getNextSibling());
                node = node.getFirstChild();
            } else
            {
                node = (BloomsNode)node.getNextSibling();
                if(node == null && !stack.isEmpty())
                    node = (BloomsNode)stack.pop();
            }
        }

    }
}
