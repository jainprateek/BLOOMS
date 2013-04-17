// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BloomsTreeTester.java

package edu.wright.cs.knoesis.test;

import edu.wright.cs.knoesis.datastructure.*;

public class BloomsTreeTester
{

    public BloomsTreeTester()
    {
    }

    public static void main(String args[])
    {
        BloomsNode node = new BloomsNode("Root Node");
        BloomsNode node1 = new BloomsNode("First Child");
        BloomsNode node2 = new BloomsNode("Second Child");
        BloomsNode node3 = new BloomsNode("Third Child");
        BloomsNode node4 = new BloomsNode("Fourth Child");
        BloomsNode node5 = new BloomsNode("Fifth Child");
        BloomsNode node6 = new BloomsNode("Sixth Child");
        BloomsNode node7 = new BloomsNode("Seventh Node");
        BloomsNode node8 = new BloomsNode("Eighth Child");
        BloomsNode node9 = new BloomsNode("Ninth Child");
        BloomsNode node10 = new BloomsNode("Tenth Child");
        BloomsNode node11 = new BloomsNode("Eleventh Child");
        node1.setParent(node);
        node2.setParent(node);
        node3.setParent(node);
        node4.setParent(node1);
        node5.setParent(node1);
        node6.setParent(node3);
        node7.setParent(node3);
        node8.setParent(node4);
        node9.setParent(node4);
        node10.setParent(node6);
        node11.setParent(node6);
        BloomsTree bTree = new BloomsTree(node, true);
        BreadthFirstSearch bfs = new BreadthFirstSearch(node);
        bfs.printBFSTraversal();
        bfs.getLeaves();
    }
}
