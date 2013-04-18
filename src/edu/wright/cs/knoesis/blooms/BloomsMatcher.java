// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   BloomsMatcher.java

package edu.wright.cs.knoesis.blooms;

import edu.wright.cs.knoesis.alignmentverifier.ParseAlignmentFile;
import edu.wright.cs.knoesis.blooms.module.serializer.SerializeVerifiedResults;
import edu.wright.cs.knoesis.blooms.module.wikipedia.main.WikipediaBLOOMS;
import edu.wright.cs.knoesis.blooms.module.wordnet.main.WordnetBLOOMS;
import edu.wright.cs.knoesis.blooms.searchengine.resultvalidator.LogGenerator;
import java.io.File;
import java.io.PrintStream;
import java.net.URI;

public class BloomsMatcher
{
	
	
    static URI onto1 = null;
    static URI onto2 = null;
    static double tHold = 0.0D;
    String alignmentSource;
    Double threshold;
    String outputFile;
    String sourceSchema;
    String targetSchema;
    String wikipediaCategoryFile="wikipedia-category.xml";
    

    /*public BloomsMatcher(BloomsConfigParameters configObject)
    {
        this.configObject = null;
        alignmentSource = "";
        threshold = Double.valueOf(0.0D);
        outputFile = "";
        sourceSchema = "";
        targetSchema = "";
        this.configObject = configObject;
        alignmentSource = this.configObject.getSource();
        threshold = new Double(this.configObject.getThreshold().doubleValue());
        outputFile = this.configObject.getOutputFile();
        sourceSchema = this.configObject.getSourceSchema();
        targetSchema = this.configObject.getTargetSchema();
    }*/

    public BloomsMatcher(String alignmentSource, Double threshold, String outputFile, String sourceSchema, String targetSchema)
    {
//        configObject = null;
        this.alignmentSource = "";
        this.threshold = Double.valueOf(0.0D);
        this.outputFile = "";
        this.sourceSchema = "";
        this.targetSchema = "";
        this.alignmentSource = alignmentSource;
        this.threshold = threshold;
        this.outputFile = outputFile;
        this.sourceSchema = sourceSchema;
        this.targetSchema = targetSchema;
    }

    public void align()
    {
        try
        {
            onto1 = new URI(sourceSchema);
            onto2 = new URI(targetSchema);
            tHold = threshold.doubleValue();
            if(alignmentSource.equalsIgnoreCase("wordnet"))
            {
                WordnetBLOOMS wnBlooms = new WordnetBLOOMS(outputFile);
                wnBlooms.match(onto1, onto2, Double.valueOf(tHold));
            } else
            if(alignmentSource.equalsIgnoreCase("wikipedia"))
            {
                WikipediaBLOOMS wikipediaBlooms = new WikipediaBLOOMS(outputFile);
                wikipediaBlooms.match(onto1, onto2, Double.valueOf(tHold));
            }
            ParseAlignmentFile parseFile = new ParseAlignmentFile(outputFile);
            parseFile.parse();
            String currentDir = System.getProperty("user.dir");
            System.out.println(currentDir);
            SerializeVerifiedResults resultSerializer = new SerializeVerifiedResults((new StringBuilder(String.valueOf(currentDir))).append(File.separator).append("results").append(File.separator).append("verified_result.csv").toString(), (new StringBuilder(String.valueOf(currentDir))).append(File.separator).append("results").append(File.separator).append("alignment.rdf").toString(), sourceSchema, targetSchema);
            String filePath1 = resultSerializer.serialize();
            //deleteTempFile();
           
            File input = new File(filePath1);
            File output = new File(outputFile);
            LogGenerator.copy(input, output);
            LogGenerator.ceaseWriting();
            LogGenerator.deleteTempFiles();
            deleteFile(filePath1);
            deleteTempFile(outputFile);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void align(String args[])
    {
        try
        {
            onto1 = new URI(args[0]);
            onto2 = new URI(args[1]);
            tHold = Double.parseDouble(args[4]);
            if(args[2].equalsIgnoreCase("wordnet"))
            {
                WordnetBLOOMS wnBlooms = new WordnetBLOOMS(args[3]);
                wnBlooms.match(onto1, onto2, Double.valueOf(tHold));
            } else
            if(args[2].equalsIgnoreCase("wikipedia"))
            {
                WikipediaBLOOMS wikipediaBlooms = new WikipediaBLOOMS(args[3]);
                wikipediaBlooms.match(onto1, onto2, Double.valueOf(tHold));
            }
            ParseAlignmentFile parseFile = new ParseAlignmentFile(args[3]);
            parseFile.parse();
            String currentDir = System.getProperty("user.dir");
            File path=new File(currentDir+File.separator+"results");
            SerializeVerifiedResults resultSerializer = new SerializeVerifiedResults((new StringBuilder(String.valueOf(currentDir))).append(File.separator).append("results").append(File.separator).append("verified_result.csv").toString(), (new StringBuilder(String.valueOf(currentDir))).append(File.separator).append("results").append(File.separator).append("alignment.rdf").toString(), args[0], args[1]);
            String filePath1 = resultSerializer.serialize();
            File input = new File(filePath1);
            File output = new File(args[3]);
            LogGenerator.copy(input, output);
            LogGenerator.ceaseWriting();
            LogGenerator.deleteDirectory(path);
            deleteFile(filePath1);
            deleteTempFile(args[3]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    void deleteTempFile(String fileName)
    {
    	String currentDir = System.getProperty("user.dir");
        File deleteFile = new File((new StringBuilder(String.valueOf(currentDir))).append(File.separator).append("wikipedia-category.xml").toString());
        deleteFile.delete();
        File deleteFile1 = new File((new StringBuilder(String.valueOf(currentDir))).append(File.separator).append(fileName).append(".csv").toString());
        deleteFile1.delete();
    }

    void deleteFile(String path)
    {
        File deleteFile = new File(path);
        deleteFile.delete();
    }

    public static void main(String args[])
    {
        BloomsMatcher matcher = new BloomsMatcher();
        if(args.length == 5)
            matcher.align(args);
        else
            System.out.println("5 Arguments Required: <Source Ontology> <Target Ontology> <Source for Alignment specify as wordnet/wikipedia> <outputfile> <threshold>");
    }

    public BloomsMatcher()
    {
        alignmentSource = "";
        threshold = Double.valueOf(0.0D);
        outputFile = "";
        sourceSchema = "";
        targetSchema = "";
    }

    public URI getOntology1()
    {
        return onto1;
    }

    public URI getOntology2()
    {
        return onto2;
    }



}
