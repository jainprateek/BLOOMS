// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ScoreCalculator.java

package edu.wright.cs.knoesis.blooms.scoring;

import edu.wright.cs.knoesis.datastructure.IBloomsNode;
import java.util.*;

public class ScoreCalculator
{

    public ScoreCalculator(HashMap commonNodeMap, int sourceTreeSize)
    {
        this.commonNodeMap = null;
        this.sourceTreeSize = 0;
        this.commonNodeMap = commonNodeMap;
        this.sourceTreeSize = sourceTreeSize;
    }

    public ScoreCalculator(HashMap commonNodeMap, int sourceTreeSize, int logBase)
    {
        this.commonNodeMap = null;
        this.sourceTreeSize = 0;
        this.commonNodeMap = commonNodeMap;
        this.sourceTreeSize = sourceTreeSize;
        LOG_BASE = Integer.valueOf(logBase);
    }

    private Double calculateDenominator()
    {
        double logValue = Math.log10(2 * sourceTreeSize) / Math.log10(LOG_BASE.intValue());
        return Double.valueOf(logValue);
    }

    private Double sumOfExponential()
    {
        Iterator nodeItr = commonNodeMap.keySet().iterator();
        double sum;
        Integer level;
        for(sum = 0.0D; nodeItr.hasNext(); sum += calculateExponential(level.intValue()).doubleValue())
        {
            IBloomsNode node = (IBloomsNode)nodeItr.next();
            level = (Integer)commonNodeMap.get(node);
        }

        return Double.valueOf(sum);
    }

    private Double calculateExponential(int level)
    {
        Double d = Double.valueOf((double)(1 - level) / (double)level);
        return Double.valueOf(1.0D + Math.exp(d.doubleValue()));
    }

    private Double calculateLog(Double d)
    {
        double logValue = Math.log10(d.doubleValue()) / Math.log10(LOG_BASE.intValue());
        return Double.valueOf(logValue);
    }

    public Double computeRelationshipStrength()
    {
        Double numerator = calculateLog(sumOfExponential());
        Double denominator = calculateDenominator();
        Double relationshipStrength = Double.valueOf(numerator.doubleValue() / denominator.doubleValue());
        return relationshipStrength;
    }

    static Integer LOG_BASE = Integer.valueOf(2);
    HashMap commonNodeMap;
    int sourceTreeSize;

}
