// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BreadthFirstSearch.java

package edu.wright.cs.knoesis.datastructure;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

// Referenced classes of package edu.wright.cs.knoesis.datastructure:
//            BloomsNode

public class BreadthFirstSearch
{

    public int getNodeCount()
    {
        return nodeCount;
    }

    private void setNodeCount(int nodeCount)
    {
        this.nodeCount = nodeCount;
    }

    public BreadthFirstSearch(BloomsNode rootNode)
    {
        this.rootNode = null;
        leaves = null;
        nodeCount = 0;
        this.rootNode = rootNode;
    }

    public static void main(String args1[])
    {
    }

    public ArrayList printBFSTraversal()
    {
        int nodeCounter = 0;
        ArrayList nodeLabelList = new ArrayList();
        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
        queue.add(rootNode);
        nodeCounter++;
        if(!nodeLabelList.contains(rootNode.getNodeName()))
            nodeLabelList.add(rootNode.getNodeName());
        while(!queue.isEmpty()) 
        {
            BloomsNode n = (BloomsNode)queue.poll();
            for(Enumeration bfsEnum = n.children(); bfsEnum.hasMoreElements();)
            {
                BloomsNode node = (BloomsNode)bfsEnum.nextElement();
                if(node != null)
                {
                    if(!nodeLabelList.contains(node.getNodeName()))
                        nodeLabelList.add(node.getNodeName());
                    queue.add(node);
                    nodeCounter++;
                }
            }

        }
        setNodeCount(nodeCounter);
        return nodeLabelList;
    }

    public List getLeaves()
    {
        leaves = new ArrayList();
        int depth = rootNode.getDepth();
        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
        queue.add(rootNode);
        while(!queue.isEmpty()) 
        {
            BloomsNode n = (BloomsNode)queue.poll();
            for(Enumeration bfsEnum = n.children(); bfsEnum.hasMoreElements();)
            {
                BloomsNode node = (BloomsNode)bfsEnum.nextElement();
                if(node != null)
                {
                    if(node.getLevel() == depth)
                        leaves.add(node);
                    queue.add(node);
                }
            }

        }
        return leaves;
    }

    BloomsNode rootNode;
    ArrayList leaves;
    private int nodeCount;
}
