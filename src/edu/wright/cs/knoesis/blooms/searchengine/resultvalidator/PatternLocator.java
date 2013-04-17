// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   PatternLocator.java

package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Referenced classes of package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator:
//            LogGenerator

public class PatternLocator
{
	
	String searchEngineSnippet="";

    /*
	public PatternLocator(String url, String pattern)
    {
        this.url = "";
        this.pattern = "";
        resultCounter = 0;
        this.url = url;
        this.pattern = pattern;
    }
    */
    
    
	/**
	 * 
	 * 
	 * @param pattern pattern to be located
	 * @param searchEngineSnippet Snippet returned by search engine for the patterns
 	 */
    public PatternLocator(String pattern,String searchEngineSnippet)
    {
        resultCounter = 0;
        this.searchEngineSnippet = searchEngineSnippet;
        this.pattern = pattern;
    }
    

    public int foundPattern()
    {
        //boolean functional = websiteFunctional();
        boolean search = false;
        search = searchForPattern();
        LogGenerator.writeToFile((new StringBuilder(String.valueOf(pattern))).append(" ").append(url).append(" ").append("Functional:").append("true").append(" Pattern Found:").append(search).toString());
        return resultCounter;
    }
    
    //Commenting it to find patterns in the snippet of search engine results
    /*public int foundPattern()
    {
        boolean functional = websiteFunctional();
        boolean search = false;
        if(functional)
        {
            search = searchForPattern();
            LogGenerator.writeToFile((new StringBuilder(String.valueOf(pattern))).append(" ").append(url).append(" ").append("Functional:").append(functional).append(" Pattern Found:").append(search).toString());
        }
        return resultCounter;
    }*/
    
    
    
    
    boolean searchForPattern()
    {
        String pattern = this.pattern;
        boolean contains = false;
        String content = this.searchEngineSnippet;
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
            
            //Making sure if there is no period or comma the system doesn't gives up
            if(index!=-1){
            	String patternPresent = output.substring(index, endIndex);
                int phraseIndex = output.indexOf(".", startIndex - 20);
                int phraseEndIndex = output.indexOf(".", endIndex);
                String text = "";
                /*if(phraseIndex != -1)
                    text = output.substring(phraseIndex, phraseEndIndex);
                else
                if(phraseEndIndex != -1)
                    text = output.substring(0, phraseEndIndex);*/
                
                if(phraseIndex != -1 && phraseEndIndex != -1)
                	text = output.substring(phraseIndex, phraseEndIndex);
                
                patternPresent = patternPresent.replaceAll("  ", " ");
                patternPresent = patternPresent.replaceAll("\n", "");
                
                //Testing to see if this makes a difference
               /* if(patternPresent.startsWith(".An") || patternPresent.startsWith(".a") || patternPresent.startsWith(".A") || patternPresent.startsWith(". A") || patternPresent.startsWith(". An") || patternPresent.startsWith(", a") || patternPresent.startsWith(", An") || patternPresent.trim().startsWith("A") || patternPresent.trim().startsWith(". a"))
                {
                    contains = output.toLowerCase().contains(pattern.toLowerCase());
                    if(contains)
                        resultCounter = resultCounter + 1;
                }*/
                
                contains = output.toLowerCase().contains(pattern.toLowerCase());
                if(contains)
                    resultCounter = resultCounter + 1;
                
            }
            
        }
        return contains;
    }
    
    
    /* Again commenting it to check for just patterns in snippet
    boolean searchForPattern()
    {
        String pattern = this.pattern;
        boolean contains = false;
        if(!url.endsWith("ppt") && !url.endsWith("doc") && !url.endsWith("pdf"))
        {
            String content = "";
            if(!url.startsWith("http://www.nyrealestatelawblog.com"))
                content = getContent();
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
                    text = output.substring(phraseIndex, phraseEndIndex);
                else
                if(phraseEndIndex != -1)
                    text = output.substring(0, phraseEndIndex);
                patternPresent = patternPresent.replaceAll("  ", " ");
                patternPresent = patternPresent.replaceAll("\n", "");
                if(patternPresent.startsWith(".An") || patternPresent.startsWith(".a") || patternPresent.startsWith(".A") || patternPresent.startsWith(". A") || patternPresent.startsWith(". An") || patternPresent.startsWith(", a") || patternPresent.startsWith(", An") || patternPresent.trim().startsWith("A") || patternPresent.trim().startsWith(". a"))
                {
                    contains = output.toLowerCase().contains(pattern.toLowerCase());
                    if(contains)
                        resultCounter = resultCounter + 1;
                }
            }
        }
        return contains;
    }*/

    String getContent()
    {
        String line = "";
        StringBuilder stringBuilder = null;
        try
        {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US; rv:1.9.2.6) Gecko/20100625 Firefox/3.6.6");
            connection.setConnectTimeout(0x2bf20);
            connection.setReadTimeout(0x2bf20);
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            stringBuilder = new StringBuilder();
            for(String str = ""; (str = in.readLine()) != null;)
                if(stringBuilder.length() + str.length() > 0 && stringBuilder.length() + str.length() < 0x7fffffff)
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

    private boolean websiteFunctional()
    {
        boolean functional = false;
        try
        {
            URL url = new URL(this.url);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(15000);
            boolean flag = true;
            for(int i = 0; flag; i++)
            {
                String headerName = conn.getHeaderFieldKey(i);
                String headerValue = conn.getHeaderField(i);
                if(headerName == null)
                {
                    flag = false;
                    LogGenerator.writeToFile(headerValue);
                    if(headerValue != null && headerValue.equals("HTTP/1.1 200 OK"))
                        functional = true;
                }
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return functional;
    }

    int findPreviousIndex(char str, char str1, String line, int index)
    {
        int i = 0;
        for(i = index - 1; i > 0; i--)
        {
            char c = line.charAt(i);
            if(c != str && c != str1 && c != ':')
                continue;
            if(c == ':')
                i++;
            break;
        }

        return i;
    }

    public static void main(String args1[])
    {
    }

    String url;
    String pattern;
    int resultCounter;
}
