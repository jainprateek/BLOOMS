// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   TestPatternLocator.java

package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternLocator
{

    public TestPatternLocator(String url, String pattern)
    {
        this.url = "";
        this.pattern = "";
        resultCounter = 0;
        this.url = url;
        this.pattern = pattern;
    }

    public static void main(String args[])
    {
        TestPatternLocator object = new TestPatternLocator("http://www.famousquotesandauthors.com/authors/ivern_ball_quotes.html", "A Politician is a Person");
        object.searchForPattern();
    }

    String getContent()
    {
        String line = "";
        StringBuilder stringBuilder = null;
        try
        {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.6) Gecko/20100625 Firefox/3.6.6");
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();
            for(String str = ""; (str = in.readLine()) != null;)
            {
                stringBuilder.append(str);
                stringBuilder.append("\n");
            }

            line = stringBuilder.toString();
            in.close();
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return line;
    }

    boolean searchForPattern()
    {
        String pattern = this.pattern;
        boolean contains = false;
        if(!url.endsWith("ppt") && !url.endsWith("doc") && !url.endsWith("pdf"))
        {
            String content = getContent();
            String patternStr = "<[^>]*>";
            Pattern regExpattern = Pattern.compile(patternStr);
            Matcher matcher = regExpattern.matcher(content);
            String output = matcher.replaceAll(".");
            String lowerCaseContent = output.toLowerCase();
            int startIndex = lowerCaseContent.indexOf(pattern.toLowerCase());
            int endIndex = startIndex + pattern.length();
            if(startIndex != -1 && endIndex != -1)
            {
                int index = findPreviousIndex('.', ',', lowerCaseContent, startIndex);
                String patternPresent = output.substring(index, endIndex);
                int phraseIndex = output.indexOf(".", startIndex - 20);
                int phraseEndIndex = output.indexOf(".", endIndex);
                String text = "";
                if(phraseIndex != -1)
                {
                    text = output.substring(phraseIndex, phraseEndIndex);
                    System.out.println(text);
                } else
                if(phraseEndIndex != -1)
                {
                    text = output.substring(0, phraseEndIndex);
                    System.out.println(text);
                }
                patternPresent = patternPresent.replaceAll("  ", " ");
                patternPresent = patternPresent.replaceAll("\n", "");
                if(patternPresent.startsWith(".a") || patternPresent.startsWith(".A") || patternPresent.startsWith(". A") || patternPresent.startsWith(", a") || patternPresent.trim().startsWith("A") || patternPresent.trim().startsWith(". a"))
                {
                    contains = output.toLowerCase().contains(pattern.toLowerCase());
                    if(contains)
                        resultCounter = resultCounter + 1;
                    System.out.println((new StringBuilder("Pattern Present:")).append(patternPresent).toString());
                }
            }
        }
        return contains;
    }

    int findPreviousIndex(char str, char str1, String line, int index)
    {
        int i = 0;
        for(i = index - 1; i > 0; i--)
        {
            char c = line.charAt(i);
            if(c == str || c == str1)
                break;
        }

        return i;
    }

    String url;
    String pattern;
    int resultCounter;
}
