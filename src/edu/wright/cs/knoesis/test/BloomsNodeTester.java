// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BloomsNodeTester.java

package edu.wright.cs.knoesis.test;

import edu.wright.cs.knoesis.datastructure.BloomsNode;
import java.io.PrintStream;
import java.util.Enumeration;

public class BloomsNodeTester
{

    public BloomsNodeTester()
    {
    }

    public static void main(String args[])
    {
        BloomsNode node = new BloomsNode("Root Node");
        System.out.println((new StringBuilder("Node is:")).append(node.toString()).toString());
        System.out.println((new StringBuilder("Node Parent:")).append(node.getParent()).toString());
        System.out.println((new StringBuilder("Number of Children:")).append(node.getChildCount()).toString());
        System.out.println((new StringBuilder("Allows Children:")).append(node.getAllowsChildren()).toString());
        BloomsNode node1 = new BloomsNode("First Child");
        System.out.println((new StringBuilder("\nNode is:")).append(node1.toString()).toString());
        System.out.println((new StringBuilder("Node Parent:")).append(node1.getParent()).toString());
        System.out.println((new StringBuilder("Number of Children:")).append(node1.getChildCount()).toString());
        System.out.println((new StringBuilder("Allows Children:")).append(node1.getAllowsChildren()).toString());
        node1.setParent(node);
        System.out.println("\nAfter Insertion of Child");
        System.out.println((new StringBuilder("Node is:")).append(node.toString()).toString());
        System.out.println((new StringBuilder("Node Parent:")).append(node.getParent()).toString());
        System.out.println((new StringBuilder("Number of Children:")).append(node.getChildCount()).toString());
        System.out.println((new StringBuilder("Allows Children:")).append(node.getAllowsChildren()).toString());
        BloomsNode node2 = new BloomsNode("Second Child", false);
        System.out.println((new StringBuilder("\nNode is:")).append(node2.toString()).toString());
        System.out.println((new StringBuilder("Node Parent:")).append(node2.getParent()).toString());
        System.out.println((new StringBuilder("Number of Children:")).append(node2.getChildCount()).toString());
        System.out.println((new StringBuilder("Allows Children:")).append(node2.getAllowsChildren()).toString());
        node2.setParent(node);
        System.out.println("\nAfter Insertion of Child");
        System.out.println((new StringBuilder("Node is:")).append(node.toString()).toString());
        System.out.println((new StringBuilder("Node Parent:")).append(node.getParent()).toString());
        System.out.println((new StringBuilder("Number of Children:")).append(node.getChildCount()).toString());
        System.out.println((new StringBuilder("Allows Children:")).append(node.getAllowsChildren()).toString());
        BloomsNode node3 = new BloomsNode("Third Child");
        node3.setParent(node1);
        System.out.println((new StringBuilder("\nNode is:")).append(node3.toString()).toString());
        System.out.println((new StringBuilder("Node Parent:")).append(node3.getParent()).toString());
        System.out.println((new StringBuilder("Number of Children:")).append(node3.getChildCount()).toString());
        System.out.println((new StringBuilder("Allows Children:")).append(node3.getAllowsChildren()).toString());
        Enumeration e = node1.children();
        System.out.println("Printing Children of Node1");
        BloomsNode object;
        for(; e.hasMoreElements(); System.out.println((new StringBuilder("Object:")).append(object.toString()).toString()))
            object = (BloomsNode)e.nextElement();

        int depth = node.getDepth();
        System.out.println((new StringBuilder("Depth of Root Node:")).append(depth).toString());
    }
}
