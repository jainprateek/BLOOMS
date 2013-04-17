// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GoogleAJAXSearchAPI.java

package edu.wright.cs.knoesis.test;

import java.io.*;
import java.net.*;

public class GoogleAJAXSearchAPI
{

    public GoogleAJAXSearchAPI()
    {
    }

    public static void main(String args[])
        throws Exception
    {
        System.out.println(endpointURL);
        URLConnection uc = (new URL(endpointURL)).openConnection();
        HttpURLConnection connection = (HttpURLConnection)uc;
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.connect();
        InputStream inputStream = null;
        try
        {
            inputStream = connection.getInputStream();
        }
        catch(IOException e)
        {
            inputStream = connection.getErrorStream();
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = rd.readLine()) != null) 
            System.out.println(line);
    }

    private static String endpointURL = "http://www.google.com/uds/GwebSearch?callback=GwebSearch.RawCompletion&context=0&lstkp=0&rsz=small&hl=en&sig=8656f49c146c5220e273d16b4b6978b2&q=Axis2&key=ABQIAAAATykxFtfO4e8pu-MxWooqihTHWbfg8oJPYT9RAs_9zd1UFnKSqRSuNfJrB6eNDipuOy4nX7NTIwl-gA&v=1.0";

}
