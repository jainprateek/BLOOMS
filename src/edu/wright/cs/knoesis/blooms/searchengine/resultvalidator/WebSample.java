// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WebSample.java

package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import com.google.gson.Gson;

public class WebSample
{
	
	  String AppId = "Insert your Bing Search App ID here";
	  String searchParams = ""; 
	  
	  
    public WebSample(String searchParams)
    {
        this.searchParams = "";
        this.searchParams = (new StringBuilder("\"")).append(searchParams).append("\"").toString();
    }

    public static void main(String args[])
    {
        WebSample obj = new WebSample("A Judge is a Person");
        try {
			obj.search();
		}  catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
   
    

    public SearchResult search()
        throws IOException
    {
        String requestURL = BuildSearchRequest();
        
        byte[] accountKeyBytes = Base64.encodeBase64((AppId+":"+AppId).getBytes());
        String accountKeyEnc = new String(accountKeyBytes);
        URL url = new URL(requestURL);
        HttpURLConnection conn=(HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
        System.out.println(conn.getResponseCode());
        
        
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer sb = new StringBuffer();
        while ((inputLine = in.readLine()) != null){
        	sb.append(inputLine);
        }
        in.close();
        System.out.println( sb.toString());
  
        Container myD = new Gson().fromJson(sb.toString(), Container.class);
        ResponseResults results=myD.d;
        ArrayList<MetadataContainer> metaDataContainer=results.results;
        List<WebResults> webResults=metaDataContainer.get(0).Web;
        
        
        SearchResult searchResults = new SearchResult(null);
        
        for(int i=0;i<webResults.size();i++){
        	searchResults.addSearchResult(webResults.get(i));
        	System.out.println(webResults.get(i).toString());
        }
        
        return searchResults;
    }

 

    public String BuildSearchRequest()
    {
        String requestString = "";
        try
        {
            requestString = (new StringBuilder("https://api.datamarket.azure.com/Bing/Search/v1/Composite?Sources=%27web%27")).append("&Query='").append(URLEncoder.encode(searchParams, "UTF-8")).append("'&Market='en-us'").append("&Adult='Moderate'").append("&$top=50").append("&$format=JSON").toString();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return requestString;
    }

    
 
    
}
