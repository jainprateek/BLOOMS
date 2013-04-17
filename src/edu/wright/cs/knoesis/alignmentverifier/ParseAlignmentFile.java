// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ParseAlignmentFile.java

package edu.wright.cs.knoesis.alignmentverifier;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import edu.wright.cs.knoesis.blooms.searchengine.resultvalidator.*;
import edu.wright.cs.knoesis.ontologyoperations.OntologyProcessor;
import edu.wright.cs.knoesis.stringoperations.NormalizeClassName;
import fr.inrialpes.exmo.align.parser.AlignmentParser;
import java.io.*;
import java.net.URI;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.semanticweb.owl.align.*;
import org.xml.sax.SAXException;

public class ParseAlignmentFile
{
	
	
    String fileName;
    BufferedWriter out;
    URI ont1URI;
    URI ont2URI;
    OntModel ont1Model;
    OntModel ont2Model;
    OntologyProcessor op1;
    OntologyProcessor op2;
    HashMap<String, ArrayList<OntClass>> ont1SubClasses;
    HashMap<String, ArrayList<OntClass>> ont2SubClasses;
    boolean process;
	

    public ParseAlignmentFile(String fileName)
    {
        this.fileName = "dbpedia_foaf.rdf";
        out = null;
        ont1URI = null;
        ont2URI = null;
        ont1Model = null;
        ont2Model = null;
        op1 = null;
        op2 = null;
        ont1SubClasses = new HashMap<String, ArrayList<OntClass>>();
        ont2SubClasses = new HashMap<String, ArrayList<OntClass>>();
        process = false;
        this.fileName = fileName;
        try
        {
            out = new BufferedWriter(new FileWriter((new StringBuilder(String.valueOf(this.fileName))).append(".csv").toString()));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public ParseAlignmentFile()
    {
        fileName = "dbpedia_foaf.rdf";
        out = null;
        ont1URI = null;
        ont2URI = null;
        ont1Model = null;
        ont2Model = null;
        op1 = null;
        op2 = null;
        ont1SubClasses = new HashMap<String, ArrayList<OntClass>>();
        ont2SubClasses = new HashMap<String, ArrayList<OntClass>>();
        process = false;
        fileName = "foaf_foaf.rdf";
        try
        {
            out = new BufferedWriter(new FileWriter("search_foaf_foaf.csv"));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String args[])
    {
        ParseAlignmentFile alignmentParser = new ParseAlignmentFile();
        alignmentParser.parse();
    }

    public void parse()
    {
    	boolean hasElements=false;
        Alignment a = null;
        AlignmentParser alignmentParser = new AlignmentParser(5);
        try
        {
            a = alignmentParser.parse(fileName);
            ont1URI = a.getFile1();
            ont2URI = a.getFile2();
            populateSubClassHashMap();
            Cell c;
            
            Enumeration cellEnum = a.getElements();
            
            while(cellEnum.hasMoreElements()){
            	c = (Cell)cellEnum.nextElement();
            	constructSearchPattern(c.getObject1AsURI(), c.getObject2AsURI(), c.getRelation().getRelation());
            	hasElements=true;
            }
            	 
            if(!hasElements){
            	System.out.println("No relationships were identified between the concepts of the two ontologies.");
            	System.exit(0);
            }
            else{
            	System.out.println("Verifying generated relationships");
            }
            
 /*           	 
            	for(Enumeration cellEnum = a.getElements(); cellEnum.hasMoreElements(); constructSearchPattern(c.getObject1AsURI(), c.getObject2AsURI(), c.getRelation().getRelation()))
                    c = (Cell)cellEnum.nextElement();*/
            
            

            out.close();
        }
        catch(AlignmentException e)
        {
            e.printStackTrace();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
 
    void populateSubClassHashMap()
    {
        op1 = new OntologyProcessor(ont1URI.toString());
        op2 = new OntologyProcessor(ont2URI.toString());
        ArrayList model1ClassesList = op1.getOntologyClassList();
        for(int i = 0; i < model1ClassesList.size(); i++)
        {
            OntClass className = (OntClass)model1ClassesList.get(i);
            ArrayList<OntClass> subClassList = op1.getSubClasses(className.toString());
            ont1SubClasses.put(className.toString(), subClassList);
        }

        ArrayList model2ClassesList = op2.getOntologyClassList();
        for(int i = 0; i < model2ClassesList.size(); i++)
        {
            OntClass className = (OntClass)model2ClassesList.get(i);
            ArrayList<OntClass> subClassList = op2.getSubClasses(className.toString());
            ont2SubClasses.put(className.toString(), subClassList);
        }

    }

    void constructSearchPattern(URI uri1, URI uri2, String relation)
    {
        String localName1 = uri1.toString();
        String localName2 = uri2.toString();
        int index1 = uri1.toString().lastIndexOf("/");
        int index3 = uri1.toString().lastIndexOf("#");
        if(index3 > index1)
            index1 = index3;
        int index2 = uri2.toString().lastIndexOf("/");
        int index4 = uri2.toString().lastIndexOf("#");
        if(index4 > index2)
            index2 = index4;
        localName1 = localName1.substring(index1 + 1, localName1.length());
        localName2 = localName2.substring(index2 + 1, localName2.length());
        process = true;
        if(process)
            if(relation.equals("<"))
                constructSubClassSearchPattern(uri1.toString(), uri2.toString());
            else
            if(relation.equals("="))
                constructEquivalentClassSearchPattern(uri1.toString(), uri2.toString());
    }

    void constructSubClassSearchPattern(String uri1, String uri2)
    {
        String localName1 = uri1.toString();
        String localName2 = uri2.toString();
        int index1 = uri1.toString().lastIndexOf("/");
        int index3 = uri1.toString().lastIndexOf("#");
        if(index3 > index1)
            index1 = index3;
        int index2 = uri2.toString().lastIndexOf("/");
        int index4 = uri2.toString().lastIndexOf("#");
        if(index4 > index2)
            index2 = index4;
        localName1 = localName1.substring(index1 + 1, localName1.length());
        localName2 = localName2.substring(index2 + 1, localName2.length());
        ArrayList subClasses1 = ont1SubClasses.get(uri1);
        ArrayList subClasses2 = ont2SubClasses.get(uri2);
        int counterA = 0;
        int counterB = 0;
        NormalizeClassName stringObj = new NormalizeClassName(localName1);
        String searchTerm1 = stringObj.normalize();
        NormalizeClassName stringObj1 = new NormalizeClassName(localName2);
        String searchTerm2 = stringObj1.normalize();
        String searchString1 = (new StringBuilder(String.valueOf(findArticle(searchTerm1)))).append(" ").append(searchTerm1).append(" is a type of ").append(searchTerm2).toString();
        counterA = verifyRelationship(searchString1);
        String searchString2 = (new StringBuilder(String.valueOf(findArticle(searchTerm2)))).append(" ").append(searchTerm2).append(" is a type of ").append(searchTerm1).toString();
        counterB = verifyRelationship(searchString2);
        String searchString3 = (new StringBuilder(String.valueOf(findArticle(searchTerm1)))).append(" ").append(searchTerm1).append(" is a kind of ").append(searchTerm2).toString();
        counterA += verifyRelationship(searchString3);
        String searchString4 = (new StringBuilder(String.valueOf(findArticle(searchTerm2)))).append(" ").append(searchTerm2).append(" is a kind of ").append(searchTerm1).toString();
        counterB += verifyRelationship(searchString4);
        String searchString5 = (new StringBuilder(String.valueOf(findArticle(searchTerm2)))).append(" ").append(searchTerm2).append(" is ").append(findArticle(searchTerm1)).append(" ").append(searchTerm1).toString();
        counterB += verifyRelationship(searchString5);
        String searchString6 = (new StringBuilder(String.valueOf(findArticle(searchTerm1)))).append(" ").append(searchTerm1).append(" is ").append(findArticle(searchTerm2)).append(" ").append(searchTerm2).toString();
        counterA += verifyRelationship(searchString6);
        String searchString7 = searchTerm1+" is "+searchTerm2;
        counterB += verifyRelationship(searchString7);
        String searchString8 = searchTerm2+" is "+ searchTerm1;
        counterA += verifyRelationship(searchString8);
        
        if(counterA + counterB > 0)
        {
            LogGenerator.writeVerifiedResultToFile((new StringBuilder(String.valueOf(searchTerm1))).append(" < ").append(searchTerm2).append(",").append(counterA).append(",").append(searchTerm2).append(" < ").append(searchTerm1).append(",").append(counterB).toString());
        } else
        {
            if(counterA == 0 && counterB == 0)
            {
                for(int i = 0; i < subClasses1.size(); i++)
                {
                    String searchTerm3 = (String)subClasses1.get(i);
                    for(int k = 0; k < subClasses2.size(); k++)
                    {
                        String searchTerm4 = (String)subClasses2.get(k);
                        if(searchTerm3.equals(searchTerm4))
                        {
                            if(searchTerm3.equals(searchTerm1))
                                counterA = 10;
                            else
                                counterB = 10;
                            break;
                        }
                        searchString1 = (new StringBuilder(String.valueOf(findArticle(searchTerm3)))).append(" ").append(searchTerm1).append(" is a type of ").append(searchTerm4).toString();
                        counterA = verifyRelationship(searchString1);
                        searchString2 = (new StringBuilder(String.valueOf(findArticle(searchTerm4)))).append(" ").append(searchTerm2).append(" is a type of ").append(searchTerm3).toString();
                        counterB = verifyRelationship(searchString2);
                        searchString3 = (new StringBuilder(String.valueOf(findArticle(searchTerm3)))).append(" ").append(searchTerm1).append(" is a kind of ").append(searchTerm4).toString();
                        counterA += verifyRelationship(searchString3);
                        searchString4 = (new StringBuilder(String.valueOf(findArticle(searchTerm4)))).append(" ").append(searchTerm4).append(" is a kind of ").append(searchTerm3).toString();
                        counterB += verifyRelationship(searchString4);
                        searchString5 = (new StringBuilder(String.valueOf(findArticle(searchTerm4)))).append(" ").append(searchTerm4).append(" is ").append(findArticle(searchTerm3)).append(" ").append(searchTerm3).toString();
                        counterB += verifyRelationship(searchString5);
                        searchString6 = (new StringBuilder(String.valueOf(findArticle(searchTerm3)))).append(" ").append(searchTerm3).append(" is ").append(findArticle(searchTerm4)).append(" ").append(searchTerm4).toString();
                        counterA += verifyRelationship(searchString6);
                        if(counterA + counterB > 0)
                            break;
                    }

                    if(counterA + counterB > 0)
                        break;
                }

            }
            LogGenerator.writeVerifiedResultToFile((new StringBuilder(String.valueOf(searchTerm1))).append(" < ").append(searchTerm2).append(",").append(counterA).append(",").append(searchTerm2).append(" < ").append(searchTerm1).append(",").append(counterB).append(",Inferred").toString());
        }
    }

    String findArticle(String word)
    {
        String article = "A";
        word = word.toLowerCase();
        if(word.startsWith("a") || word.startsWith("e") || word.startsWith("i") || word.startsWith("o") || word.startsWith("u"))
            article = "An";
        return article;
    }

    void constructEquivalentClassSearchPattern(String uri1, String uri2)
    {
        String localName1 = uri1.toString();
        String localName2 = uri2.toString();
        int index1 = uri1.toString().lastIndexOf("/");
        int index3 = uri1.toString().lastIndexOf("#");
        if(index3 > index1)
            index1 = index3;
        int index2 = uri2.toString().lastIndexOf("/");
        int index4 = uri2.toString().lastIndexOf("#");
        if(index4 > index2)
            index2 = index4;
        localName1 = localName1.substring(index1 + 1, localName1.length());
        localName2 = localName2.substring(index2 + 1, localName2.length());
        NormalizeClassName stringObj = new NormalizeClassName(localName1);
        String searchTerm1 = stringObj.normalize();
        NormalizeClassName stringObj1 = new NormalizeClassName(localName2);
        String searchTerm2 = stringObj1.normalize();
        String searchString = (new StringBuilder(String.valueOf(searchTerm1))).append(" is same as ").append(searchTerm2).toString();
        verifyRelationship(searchString);
        LogGenerator.writeVerifiedResultToFile((new StringBuilder(String.valueOf(searchTerm1))).append(" = ").append(searchTerm2).append(",").append(10).append(",").append(searchTerm2).append(" = ").append(searchTerm1).append(",").append(10).toString());
    }

    void writeToCSVFile(String pattern, String title, String description, String url, String lastCrawled)
    {
        try
        {
            out.write((new StringBuilder(String.valueOf(pattern))).append(",").append(title.replace(",", "*")).append(",").append(description.replace(",", "*")).append(",").append(url).append(",").append(lastCrawled).append("\n").toString());
            out.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    int verifyRelationship(String searchPattern)
    {
    	System.out.println(searchPattern);
        int counter = 0;
        WebSample bingSearch = new WebSample(searchPattern);
        try
        {
            SearchResult searchObj = bingSearch.search();
            if(searchObj.getResultList() != null)
            {
                ArrayList<WebResults> resultList = searchObj.getResultList();
                for(int i = 0; i < resultList.size(); i++)
                {
                	WebResults results = resultList.get(i);
                    writeToCSVFile(searchPattern, results.getTitle(), results.getDescription(), results.getUrl(), results.getDisplayUrl());
                    PatternLocator pLocator = new PatternLocator(searchPattern,results.getDescription());
                    //PatternLocator pLocator = new PatternLocator(results.getUrl(), searchPattern);
                    counter += pLocator.foundPattern();
                }

            }
            
            
            //Making the thread sleep for 20 seconds to not overload the server
            try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        searchPattern = "";
        return counter;
    }


}
