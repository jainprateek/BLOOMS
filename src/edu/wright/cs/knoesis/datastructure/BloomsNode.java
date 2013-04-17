// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BloomsNode.java

package edu.wright.cs.knoesis.datastructure;

import edu.mit.jwi.item.IWord;
import edu.wright.cs.knoesis.blooms.module.wordnet.exception.ChildNotAllowedException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

// Referenced classes of package edu.wright.cs.knoesis.datastructure:
//            IBloomsNode, EmptyEnumeration, BreadthFirstEnumeration, BreadthFirstSearch

public class BloomsNode
    implements IBloomsNode, Serializable
{

    public Object getUserObject()
    {
        return userObject;
    }

    public BloomsNode()
    {
        allowsChildren = true;
        children = new Vector();
        parent = null;
        userObject = null;
    }

    public BloomsNode(Object userObject)
    {
        allowsChildren = true;
        children = new Vector();
        parent = null;
        this.userObject = userObject;
    }

    public BloomsNode(Object userObject, boolean allowsChildren)
    {
        this.allowsChildren = allowsChildren;
        if(allowsChildren)
            children = new Vector();
        parent = null;
        this.userObject = userObject;
    }

    public void insert(MutableTreeNode arg0, int arg1)
    {
        BloomsNode node = (BloomsNode)arg0;
        node.parent = null;
        node.parent = this;
        children.insertElementAt(node, arg1);
    }

    public void remove(int arg0)
    {
        children.remove(arg0);
    }

    public void remove(MutableTreeNode arg0)
    {
        for(int i = 0; i < children.size(); i++)
        {
            BloomsNode node = (BloomsNode)children.get(i);
            if(node.toString().equals(arg0.toString()))
                node.parent = null;
            children.remove(i);
        }

    }

    public void removeFromParent()
    {
        if(parent != null)
            parent = null;
    }

    public void setParent(MutableTreeNode arg0)
    {
        BloomsNode obj = (BloomsNode)arg0;
        if(obj.allowsChildren)
        {
            parent = null;
            parent = arg0;
            obj.insert(this, obj.children.size());
        } else
        {
            try
            {
                throw new ChildNotAllowedException();
            }
            catch(ChildNotAllowedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void setUserObject(Object arg0)
    {
        userObject = arg0;
    }

    public Enumeration children()
    {
        if(children == null)
            return new EmptyEnumeration();
        else
            return children.elements();
    }

    public boolean getAllowsChildren()
    {
        return allowsChildren;
    }

    public TreeNode getChildAt(int arg0)
    {
        return (TreeNode)children.get(arg0);
    }

    public int getChildCount()
    {
        if(children != null)
            return children.size();
        else
            return 0;
    }

    public int getIndex(TreeNode arg0)
    {
        int index = -1;
        for(int i = 0; i < children.size(); i++)
        {
            TreeNode node = (TreeNode)children.get(i);
            if(node.toString().equals(arg0.toString()))
                return i;
        }

        return index;
    }

    public TreeNode getParent()
    {
        return parent;
    }

    public boolean isLeaf()
    {
        return getChildCount() <= 0;
    }

    public BloomsNode clone() throws CloneNotSupportedException
    {
        Object userObj = userObject;
        BloomsNode newObj = new BloomsNode(userObj);
        newObj.allowsChildren = allowsChildren;
        newObj.children = (Vector)children.clone();
        return (BloomsNode)super.clone();
    }

    public String toString()
    {
        if(!(this instanceof IWord))
            return getNodeName();
        else
            return ((IWord)this).getLemma();
    }

    public int getDepth()
    {
        Object last = null;
        for(Enumeration enum_ = breadthFirstEnumeration(); enum_.hasMoreElements();)
            last = enum_.nextElement();

        if(last == null)
            throw new Error("nodes should be null");
        else
            return ((BloomsNode)last).getLevel() - getLevel();
    }

    public int getLevel()
    {
        int levels = 0;
        for(TreeNode ancestor = this; (ancestor = ancestor.getParent()) != null;)
            levels++;

        return levels;
    }

    public Enumeration breadthFirstEnumeration()
    {
        return new BreadthFirstEnumeration(this);
    }

    public BloomsNode getFirstChild()
    {
        return (BloomsNode)children.get(0);
    }

    public String getNodeName()
    {
        if(getUserObject() instanceof IWord)
            return ((IWord)getUserObject()).getLemma();
        else
            return (String)getUserObject();
    }

    public boolean hasChildNodes()
    {
        boolean hasChild = false;
        if(children != null && children.size() > 0)
            hasChild = true;
        return hasChild;
    }

    public Object getNextSibling()
    {
        BloomsNode myParent = (BloomsNode)getParent();
        BloomsNode retval;
        if(myParent == null)
            retval = null;
        else
            retval = (BloomsNode)myParent.getChildAfter(this);
        if(retval != null && !isNodeSibling(retval))
            throw new Error("child of parent is not a sibling");
        else
            return retval;
    }

    private boolean isNodeSibling(BloomsNode anotherNode)
    {
        boolean retval;
        if(anotherNode == null)
            retval = false;
        else
        if(anotherNode == this)
        {
            retval = true;
        } else
        {
            TreeNode myParent = getParent();
            retval = myParent != null && myParent == anotherNode.getParent();
            if(retval && !((BloomsNode)getParent()).isNodeChild(anotherNode))
                throw new Error("sibling has different parent");
        }
        return retval;
    }

    private boolean isNodeChild(BloomsNode aNode)
    {
        boolean retval;
        if(aNode == null)
            retval = false;
        else
        if(getChildCount() == 0)
            retval = false;
        else
            retval = aNode.getParent() == this;
        return retval;
    }

    public TreeNode getChildAfter(TreeNode aChild)
    {
        if(aChild == null)
            throw new IllegalArgumentException("argument is null");
        int index = getIndex(aChild);
        if(index == -1)
            throw new IllegalArgumentException("node is not a child");
        if(index < getChildCount() - 1)
            return getChildAt(index + 1);
        else
            return null;
    }

    public HashMap commonNodes(IBloomsNode target)
    {
        HashMap commonNodesHMap = new HashMap();
        HashMap commonNodesStrHMap = new HashMap();
        BreadthFirstSearch bfsSearch = new BreadthFirstSearch(this);
        List srcTreeNodes = bfsSearch.printBFSTraversal();
        BloomsNode node = (BloomsNode)target;
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
        return commonNodesHMap;
    }

    public int getTreeSize()
    {
        BreadthFirstSearch bfs = new BreadthFirstSearch(this);
        bfs.printBFSTraversal();
        int treeSize = bfs.getNodeCount();
        return treeSize - 1;
    }

    /*public volatile Object clone()
        throws CloneNotSupportedException
    {
        return clone();
    }

    public volatile IBloomsNode getFirstChild()
    {
        return getFirstChild();
    }*/

    protected boolean allowsChildren;
    protected Vector children;
    protected MutableTreeNode parent;
    protected Object userObject;
}
