// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   LogGenerator.java

package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator;

import java.io.*;

public class LogGenerator
{

    public LogGenerator()
    {
    }

    public static void main(String args1[])
    {
    	
    }

    public static void deleteTempFiles()
    {
        String currentDir = System.getProperty("user.dir");
        File deleteFile1 = new File((new StringBuilder(currentDir).append(File.separator).append("results").append(File.separator).append("search_engine.log").toString()));
        deleteFile1.delete();
        File deleteFile2 = new File((new StringBuilder(currentDir).append(File.separator).append("results").append(File.separator).append("verified_result.csv").toString()));
        deleteFile2.delete();
        File deleteFile3 = new File((new StringBuilder(currentDir).append(File.separator).append("results").toString()));
        deleteFile3.delete();
    }

    static void writeToFile(String str)
    {
        try
        {
            out.write((new StringBuilder(String.valueOf(str))).append("\n").toString());
            out.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void writeVerifiedResultToFile(String str)
    {
        try
        {
            out1.write((new StringBuilder(String.valueOf(str))).append("\n").toString());
            out1.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    
    /**
     * Method to close the streams in order to delete the files.
     * 
     */
    public static void ceaseWriting()
    {
        try
        {
            out.flush();
            out.close();
            out1.flush();
            out1.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void copy(File src, File dst)
        throws IOException
    {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte buf[] = new byte[1024];
        int len;
        while((len = in.read(buf)) >= 0) 
            out.write(buf, 0, len);
        out.flush();
        in.close();
        out.close();
    }

    static String fileName = "/Users/prateekjain/Documents/workspace/Blooms/results/search_engine.log";
    static BufferedWriter out = null;
    static BufferedWriter out1 = null;
    static String verifiedResultFileName = "/Users/prateekjain/Documents/workspace/Blooms/results/verified_result.csv";

    
    static public boolean deleteDirectory(File path) {
    	
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }
    
    
    static 
    {
        try
        {
            String currentDir = System.getProperty("user.dir");
        	File path=new File(currentDir+File.separator+"results");
            boolean deleted=LogGenerator.deleteDirectory(path);
            
            if(deleted)
            	System.out.println("Deleted results Folder");
            else
            	System.out.println("Cannot delete results folder");
            
            boolean success = (new File(currentDir+File.separator+"results")).mkdirs();
            if(success){
                out = new BufferedWriter(new FileWriter((new StringBuilder(String.valueOf(currentDir))).append(File.separator).append("results").append(File.separator).append("search_engine.log").toString(), true));
                out1 = new BufferedWriter(new FileWriter((new StringBuilder(String.valueOf(currentDir))).append(File.separator).append("results").append(File.separator).append("verified_result.csv").toString(), true));
            }
            else
            {
            	System.err.println("Could not create directory for temporary results in this location.. Program now exiting");
            	System.exit(0);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
