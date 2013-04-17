// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SpreadSheetReader.java

package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator;

import java.io.*;

// Referenced classes of package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator:
//            LogGenerator, PatternLocator

public class SpreadSheetReader
{

    public SpreadSheetReader()
    {
        searchPattern = "";
    }

    public static void main(String args[])
    {
        SpreadSheetReader reader = new SpreadSheetReader();
        reader.readExcelSheet();
    }

    void readExcelSheet()
    {
        try
        {
            BufferedReader in = new BufferedReader(new FileReader("search_pattern_101_103.csv"));
            String str;
            while((str = in.readLine()) != null) 
                process(str);
            in.close();
            LogGenerator.ceaseWriting();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    void process(String str)
    {
        String strArray[] = str.split(",");
        int arrayLength = strArray.length;
        if(arrayLength == 5)
        {
            if(strArray[0].trim().length() > 0)
                searchPattern = strArray[0];
            String url = strArray[3];
            PatternLocator pLocator = new PatternLocator(url, searchPattern);
            pLocator.foundPattern();
        }
    }

    String searchPattern;
}
