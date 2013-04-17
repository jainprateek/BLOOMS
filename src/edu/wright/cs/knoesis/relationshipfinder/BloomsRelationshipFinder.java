// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BloomsRelationshipFinder.java

package edu.wright.cs.knoesis.relationshipfinder;

import edu.wright.cs.knoesis.blooms.module.wordnet.wordnetop.WordnetTester;
import edu.wright.cs.knoesis.blooms.scoring.ScoreCalculator;
import edu.wright.cs.knoesis.constants.RelationshipStrengthConstants;
import edu.wright.cs.knoesis.datastructure.*;
import fr.inrialpes.exmo.align.impl.BasicRelation;
import java.util.HashMap;

// Referenced classes of package edu.wright.cs.knoesis.relationshipfinder:
//            IBloomsRelationship

public class BloomsRelationshipFinder
    implements IBloomsRelationship
{

    public BloomsRelationshipFinder()
    {
        sourceTree = null;
        targetTree = null;
        relationshipStrength = null;
    }

    public static void main(String args1[])
    {
    }

    public BasicRelation identifyRelationship(BloomsTree sourceTree, BloomsTree targetTree)
    {
        BasicRelation rel = null;
        rel = identifyEquivalanceRelationship(sourceTree, targetTree);
        if(rel == null)
            rel = identifySubClassRelationship(sourceTree, targetTree);
        return rel;
    }

    public BasicRelation identifyEquivalanceRelationship(BloomsTree sourceTree, BloomsTree targetTree)
    {
        BasicRelation rel = null;
        for(int i = 0; i < sourceTree.getRoot().getChildCount(); i++)
        {
            String s1 = sourceTree.getRoot().getChildAt(i).toString();
            for(int j = 0; j < targetTree.getRoot().getChildCount(); j++)
            {
                String s2 = targetTree.getRoot().getChildAt(j).toString();
                try
                {
                    if(s1.toLowerCase().equals(s2.toLowerCase()))
                    {
                        rel = null;
                        rel = createRelationshipObject("=");
                    } else
                    {
                        rel = null;
                        WordnetTester obj = new WordnetTester();
                        boolean synoyms = obj.areSynoynyms(s1, s2);
                        if(synoyms)
                            rel = createRelationshipObject("=");
                    }
                }
                catch(Exception owex)
                {
                    owex.printStackTrace();
                }
            }

        }

        if(rel != null)
            setRelationshipStrength(new Double(RelationshipStrengthConstants.MAX_STRENGTH));
        return rel;
    }

    public BasicRelation identifySubClassRelationship(BloomsTree sourceTree, BloomsTree targetTree)
    {
        double rStrength = 0.0D;
        double maxStrength = 0.0D;
        BasicRelation rel = null;
        if(sourceTree != null && targetTree != null)
        {
            for(int i = 0; i < sourceTree.getRoot().getChildCount(); i++)
            {
                BloomsNode sourceNode = (BloomsNode)sourceTree.getRoot().getChildAt(i);
                for(int k = 0; k < targetTree.getRoot().getChildCount(); k++)
                {
                    BloomsNode targetNode = (BloomsNode)targetTree.getRoot().getChildAt(k);
                    HashMap commonNodeMap = sourceNode.commonNodes(targetNode);
                    if(commonNodeMap.size() > 0)
                    {
                        ScoreCalculator score = new ScoreCalculator(commonNodeMap, sourceNode.getTreeSize());
                        rStrength = score.computeRelationshipStrength().doubleValue();
                        if(rStrength > 0.0D && rStrength >= maxStrength)
                        {
                            maxStrength = rStrength;
                            rel = createRelationshipObject("<");
                        }
                    } else
                    {
                        rStrength = 0.0D;
                    }
                }

            }

        }
        setRelationshipStrength(new Double(maxStrength));
        return rel;
    }

    public Double getRelationshipStrength()
    {
        return relationshipStrength;
    }

    public BasicRelation createRelationshipObject(String str)
    {
        BasicRelation rel = null;
        if(str.equals("=") || str.equals("<") || str.equals(">"))
            rel = new BasicRelation(str);
        return rel;
    }

    public void setRelationshipStrength(Double d)
    {
        relationshipStrength = d;
    }

    BloomsTree sourceTree;
    BloomsTree targetTree;
    Double relationshipStrength;
}
