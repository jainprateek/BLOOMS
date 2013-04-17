// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BreadthFirstEnumeration.java

package edu.wright.cs.knoesis.datastructure;

import java.util.*;
import javax.swing.tree.TreeNode;

public class BreadthFirstEnumeration
    implements Enumeration
{
    final class Queue
    {
        final class QNode
        {

            public Object object;
            public QNode next;
            final Queue this$1;

            public QNode(Object object, QNode next)
            {
                super();
                this$1 = Queue.this;
                this.object = object;
                this.next = next;
            }
        }


        public void enqueue(Object anObject)
        {
            if(head == null)
            {
                head = tail = new QNode(anObject, null);
            } else
            {
                tail.next = new QNode(anObject, null);
                tail = tail.next;
            }
        }

        public Object dequeue()
        {
            if(head == null)
                throw new NoSuchElementException("No more elements");
            Object retval = head.object;
            QNode oldHead = head;
            head = head.next;
            if(head == null)
                tail = null;
            else
                oldHead.next = null;
            return retval;
        }

        public Object firstObject()
        {
            if(head == null)
                throw new NoSuchElementException("No more elements");
            else
                return head.object;
        }

        public boolean isEmpty()
        {
            return head == null;
        }

        QNode head;
        QNode tail;
        final BreadthFirstEnumeration this$0;

        Queue()
        {
        	super();
            this$0 = BreadthFirstEnumeration.this;
            
        }
    }


    public BreadthFirstEnumeration(TreeNode rootNode)
    {
        Vector v = new Vector(1);
        v.addElement(rootNode);
        queue = new Queue();
        queue.enqueue(v.elements());
    }

    public boolean hasMoreElements()
    {
        return !queue.isEmpty() && ((Enumeration)queue.firstObject()).hasMoreElements();
    }

    public TreeNode nextElement()
    {
        Enumeration enumer = (Enumeration)queue.firstObject();
        TreeNode node = (TreeNode)enumer.nextElement();
        Enumeration children = node.children();
        if(!enumer.hasMoreElements())
            queue.dequeue();
        if(children.hasMoreElements())
            queue.enqueue(children);
        return node;
    }

    /*
    public volatile Object nextElement()
    {
        return nextElement();
    }*/

    protected Queue queue;
}
