// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BloomsTree.java

package edu.wright.cs.knoesis.datastructure;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

// Referenced classes of package edu.wright.cs.knoesis.datastructure:
//            IBloomsTree, BloomsNode, BreadthFirstSearch, IBloomsNode

public class BloomsTree
    implements IBloomsTree, Serializable
{

    private ArrayList getNodeLabels()
    {
        return nodeLabels;
    }

    private void setNodeLabels(ArrayList nodeLabels)
    {
        this.nodeLabels = nodeLabels;
    }

    public BloomsTree(BloomsNode root)
    {
        this(root, false);
    }

    public BloomsTree(BloomsNode root, boolean asksAllowsChildren)
    {
        rootNode = null;
        leaves = null;
        nodeLabels = null;
        if(root == null)
            root = new BloomsNode();
        rootNode = root;
        this.asksAllowsChildren = asksAllowsChildren;
    }

    public BloomsTree copyBloomsTree()
    {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bin);
            return (BloomsTree)ois.readObject();
        }
        catch(IOException e)
        {
            System.out.println((new StringBuilder("Exception in ObjectCloner = ")).append(e).toString());
            e.printStackTrace();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public IBloomsNode getRoot()
    {
        return rootNode;
    }

    public void addTreeModelListener(TreeModelListener arg0)
    {
        System.out.println("Method returns null");
    }

    public Object getChild(Object parent, int index)
    {
        return ((TreeNode)parent).getChildAt(index);
    }

    public int getChildCount(Object parent)
    {
        return ((TreeNode)parent).getChildCount();
    }

    public int getIndexOfChild(Object parent, Object child)
    {
        if(parent == null || child == null)
            return -1;
        else
            return ((TreeNode)parent).getIndex((TreeNode)child);
    }

    public boolean isLeaf(Object node)
    {
        if(asksAllowsChildren)
            return !((TreeNode)node).getAllowsChildren();
        else
            return ((TreeNode)node).isLeaf();
    }

    public void removeTreeModelListener(TreeModelListener arg0)
    {
        System.out.println("Method Returns Null");
    }

    public void valueForPathChanged(TreePath arg0, Object arg1)
    {
        System.out.println("Method Returns Null");
    }

    public void setRoot(IBloomsNode node)
    {
        rootNode = node;
    }

    public TreeNode[] getPathToRoot(TreeNode aNode)
    {
        return getPathToRoot(aNode, 0);
    }

    protected TreeNode[] getPathToRoot(TreeNode aNode, int depth)
    {
        TreeNode retNodes[];
        if(aNode == null)
        {
            if(depth == 0)
                return null;
            retNodes = new TreeNode[depth];
        } else
        {
            depth++;
            if(aNode == rootNode)
                retNodes = new TreeNode[depth];
            else
                retNodes = getPathToRoot(aNode.getParent(), depth);
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    public List getLeaves()
    {
        leaves = new ArrayList();
        int depth = ((BloomsNode)rootNode).getDepth();
        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
        queue.add((BloomsNode)rootNode);
        while(!queue.isEmpty()) 
        {
            BloomsNode n = (BloomsNode)queue.poll();
            for(Enumeration bfsEnum = n.children(); bfsEnum.hasMoreElements();)
            {
                BloomsNode node = (BloomsNode)bfsEnum.nextElement();
                if(node != null)
                {
                    if(node.isLeaf())
                        leaves.add(node);
                    queue.add(node);
                }
            }

        }
        return leaves;
    }

    public int getTreeSize()
    {
        BreadthFirstSearch bfs = new BreadthFirstSearch((BloomsNode)getRoot());
        bfs.printBFSTraversal();
        int treeSize = bfs.getNodeCount();
        return treeSize - 1;
    }

    public HashMap commonNodes(IBloomsTree target)
    {
        HashMap commonNodesHMap = new HashMap();
        HashMap commonNodesStrHMap = new HashMap();
        BreadthFirstSearch bfsSearch = new BreadthFirstSearch((BloomsNode)getRoot());
        List srcTreeNodes = bfsSearch.printBFSTraversal();
        BloomsNode node = (BloomsNode)target.getRoot();
        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
        queue.add(node);
        if(srcTreeNodes.contains(node.getNodeName()) && !commonNodesStrHMap.containsKey(node.getNodeName()))
        {
            commonNodesHMap.put(node, Integer.valueOf(node.getLevel()));
            commonNodesStrHMap.put(node.getNodeName(), Integer.valueOf(node.getLevel()));
        }
        while(!queue.isEmpty()) 
        {
            BloomsNode n = (BloomsNode)queue.poll();
            for(Enumeration bfsEnum = n.children(); bfsEnum.hasMoreElements();)
            {
                BloomsNode nextNode = (BloomsNode)bfsEnum.nextElement();
                if(nextNode != null)
                {
                    if(srcTreeNodes.contains(nextNode.getNodeName()) && !commonNodesStrHMap.containsKey(nextNode.getNodeName()))
                    {
                        commonNodesHMap.put(nextNode, Integer.valueOf(nextNode.getLevel()));
                        commonNodesStrHMap.put(nextNode.getNodeName(), Integer.valueOf(nextNode.getLevel()));
                    }
                    queue.add(nextNode);
                }
            }

        }
        commonNodesStrHMap = null;
        return commonNodesHMap;
    }

    /*
    public volatile Object getRoot()
    {
        return getRoot();
    }

    public volatile IBloomsTree copyBloomsTree()
    {
        return copyBloomsTree();
    }*/

    protected IBloomsNode rootNode;
    protected ArrayList leaves;
    protected boolean asksAllowsChildren;
    ArrayList nodeLabels;
}
