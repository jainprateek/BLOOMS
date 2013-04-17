// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   InvokeWikipediaWebService.java

package edu.wright.cs.knoesis.blooms.module.wikipedia.wikipediaop;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class InvokeWikipediaWebService
{

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public InvokeWikipediaWebService(String serviceURL)
    {
        url = null;
        content = null;
        url = serviceURL;
    }

    public static void main(String args1[])
    {
    }

    public void invokeWebService()
    {
        String text = "";
        try
        {
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String str;
            while((str = reader.readLine()) != null) 
                text = (new StringBuilder(String.valueOf(text))).append(str).toString();
            if(text.trim().length() > 0)
                setContent(text);
            else
                setContent(null);
            conn.disconnect();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    String url;
    String content;
}
