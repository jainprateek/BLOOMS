// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WikipediaBLOOMS.java

package edu.wright.cs.knoesis.blooms.module.wikipedia.main;

import com.hp.hpl.jena.ontology.OntClass;
import edu.wright.cs.knoesis.blooms.module.wikipedia.wikipediaop.WikipediaOperations;
import edu.wright.cs.knoesis.datastructure.*;
import edu.wright.cs.knoesis.ontologyoperations.OntologyProcessor;
import edu.wright.cs.knoesis.relationshipfinder.BloomsRelationship;
import edu.wright.cs.knoesis.relationshipfinder.BloomsRelationshipFinder;
import edu.wright.cs.knoesis.stringoperations.NormalizeClassName;
import fr.inrialpes.exmo.align.impl.*;
import fr.inrialpes.exmo.align.impl.renderer.RDFRendererVisitor;
import fr.inrialpes.exmo.ontowrap.OntowrapException;
import fr.inrialpes.exmo.ontowrap.jena.JENAOntology;
import fr.inrialpes.exmo.ontowrap.jena.JENAOntologyFactory;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import org.semanticweb.owl.align.*;

public class WikipediaBLOOMS extends BasicAlignment
    implements AlignmentProcess
{

    public WikipediaBLOOMS(String outputFile)
    {
        this.outputFile = "";
        ontology1 = null;
        ontology2 = null;
        ont1NameMapper = new HashMap();
        ont2NameMapper = new HashMap();
        ont1ConceptToTreeMap = new HashMap();
        ont2ConceptToTreeMap = new HashMap();
        this.outputFile = outputFile;
    }

    public URI getOntology1()
    {
        return ontology1;
    }

    public void setOntology1(URI ontology1)
    {
        this.ontology1 = ontology1;
    }

    public URI getOntology2()
    {
        return ontology2;
    }

    public void setOntology2(URI ontology2)
    {
        this.ontology2 = ontology2;
    }

    public WikipediaBLOOMS(URI ontology1, URI ontology2)
    {
        outputFile = "";
        ont1NameMapper = new HashMap();
        ont2NameMapper = new HashMap();
        ont1ConceptToTreeMap = new HashMap();
        ont2ConceptToTreeMap = new HashMap();
        this.ontology1 = ontology1;
        this.ontology2 = ontology2;
    }

    public static void main(String args[])
    {
        WikipediaBLOOMS wikiBlooms = null;
        try
        {
            wikiBlooms = new WikipediaBLOOMS(new URI("http://xmlns.com/foaf/spec/index.rdf"), new URI("http://xmlns.com/foaf/spec/index.rdf"));
            wikiBlooms.match(new URI("http://xmlns.com/foaf/spec/index.rdf"), new URI("http://xmlns.com/foaf/spec/index.rdf"), Double.valueOf(0.5D));
        }
        catch(URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public void match(URI ontology1, URI ontology2, Double tHold)
    {
        OntologyProcessor op = new OntologyProcessor(ontology1.toString());
        ArrayList ontology1ClassList = op.getOntologyClasses();
        ont1NameMapper = op.getLocalNameOntClassMapper();
        URI ontology1URI = op.getOntology();
        OntologyProcessor op1 = new OntologyProcessor(ontology2.toString());
        ArrayList ontology2ClassList = op1.getOntologyClasses();
        ont2NameMapper = op1.getLocalNameOntClassMapper();
        URI ontology2URI = op1.getOntology();
        NormalizeClassName ncn = new NormalizeClassName(ontology1ClassList);
        ArrayList ont1NormalizedNameList = ncn.normalizeStringList();
        NormalizeClassName ncn1 = new NormalizeClassName(ontology2ClassList);
        ArrayList ont2NormalizedNameList = ncn1.normalizeStringList();
        for(int i = 0; i < ont1NormalizedNameList.size(); i++)
        {
            String className = (String)ont1NormalizedNameList.get(i);
            if(className != null)
            {
                BloomsTree wikiTree = createBloomsTree(className);
                BreadthFirstSearch bfsSearch = new BreadthFirstSearch((BloomsNode)wikiTree.getRoot());
                bfsSearch.printBFSTraversal();
                ont1ConceptToTreeMap.put(className, wikiTree);
            }
        }

        for(int i = 0; i < ont2NormalizedNameList.size(); i++)
        {
            String className = (String)ont2NormalizedNameList.get(i);
            if(className != null)
            {
                BloomsTree wikiTree = createBloomsTree(className);
                BreadthFirstSearch bfsSearch = new BreadthFirstSearch((BloomsNode)wikiTree.getRoot());
                ont2ConceptToTreeMap.put(className, wikiTree);
            }
        }

        BasicAlignment result = new BasicAlignment();
        JENAOntologyFactory factory1 = new JENAOntologyFactory();
        JENAOntology jenaOntology1 = null;
        JENAOntology jenaOntology2 = null;
        try
        {
            jenaOntology1 = factory1.loadOntology(ontology1);
            jenaOntology2 = factory1.loadOntology(ontology2);
        }
        catch(OntowrapException e2)
        {
            e2.printStackTrace();
        }
        try
        {
            jenaOntology1.setURI(ontology1URI);
            jenaOntology2.setURI(ontology2URI);
            result.init(jenaOntology1, jenaOntology2);
            result.setOntology1(jenaOntology1);
            result.setOntology2(jenaOntology2);
            result.setFile1(jenaOntology1.getFile());
            result.setFile2(jenaOntology2.getFile());
        }
        catch(AlignmentException e1)
        {
            e1.printStackTrace();
        }
        for(Iterator ont1ConceptItr = ont1ConceptToTreeMap.keySet().iterator(); ont1ConceptItr.hasNext();)
        {
            String concept1 = (String)ont1ConceptItr.next();
            BloomsTree sourceTree = (BloomsTree)ont1ConceptToTreeMap.get(concept1);
            int tree1ChildCount = sourceTree.getRoot().getChildCount();
            for(Iterator ont2ConceptItr = ont2ConceptToTreeMap.keySet().iterator(); ont2ConceptItr.hasNext();)
            {
                String concept2 = (String)ont2ConceptItr.next();
                BloomsTree targetTree = (BloomsTree)ont2ConceptToTreeMap.get(concept2);
                int tree2ChildCount = targetTree.getRoot().getChildCount();
                BloomsRelationshipFinder wtrf = new BloomsRelationshipFinder();
                BasicRelation rel = wtrf.identifyRelationship(sourceTree, targetTree);
                String arg1 = ((OntClass)ont1NameMapper.get(concept1)).toString();
                String arg2 = ((OntClass)ont2NameMapper.get(concept2)).toString();
                try
                {
                    URI uri1 = new URI(arg1);
                    URI uri2 = new URI(arg2);
                    if(rel != null)
                    {
                        double strength = wtrf.getRelationshipStrength().doubleValue();
                        if(strength >= tHold.doubleValue())
                            result.addAlignCell(uri1, uri2, rel.getRelation(), strength);
                    }
                }
                catch(URISyntaxException e)
                {
                    e.printStackTrace();
                }
                catch(AlignmentException e)
                {
                    e.printStackTrace();
                }
            }

        }

        try
        {
            File file = new File(outputFile);
            PrintWriter writer = new PrintWriter(file);
            org.semanticweb.owl.align.AlignmentVisitor renderer = new RDFRendererVisitor(writer);
            result.render(renderer);
            writer.flush();
            writer.close();
        }
        catch(AlignmentException e)
        {
            e.printStackTrace();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    void findRelationships(ArrayList ont1NormalizedNameList, ArrayList ont2NormalizedNameList)
    {
        for(int i = 0; i < ont1NormalizedNameList.size(); i++)
        {
            String class1 = (String)ont1NormalizedNameList.get(i);
            for(int j = 0; j < ont2NormalizedNameList.size(); j++)
            {
                String class2 = (String)ont2NormalizedNameList.get(j);
                match(class1, class2);
            }

        }

    }

    BloomsTree createBloomsTree(String phrase)
    {
        WikipediaOperations wikiOp = new WikipediaOperations(phrase);
        List articleList = wikiOp.getArticles();
        BloomsNode rootNode = new BloomsNode(phrase, true);
        BloomsTree categoryTree = new BloomsTree(rootNode, true);
        for(int m = 0; m < articleList.size(); m++)
        {
            String articleName = (String)articleList.get(m);
            BloomsNode articleNode = new BloomsNode(articleName, true);
            articleNode.setParent(rootNode);
            BloomsTree articleTree = new BloomsTree(articleNode, true);
            articleNode = constructCategoryTree(articleNode);
            for(int i = 0; i < 1; i++)
            {
                List bloomsNodeList = articleTree.getLeaves();
                for(int k = 0; k < bloomsNodeList.size(); k++)
                {
                    BloomsNode n = (BloomsNode)bloomsNodeList.get(k);
                    constructCategoryTree(n);
                }

            }

        }

        return categoryTree;
    }

    BloomsNode constructCategoryTree(BloomsNode node)
    {
        String phrase = (String)node.getUserObject();
        constructCategoryTree(phrase, node);
        return node;
    }

    void constructCategoryTree(String phrase, BloomsNode n)
    {
        WikipediaOperations wikiOp = new WikipediaOperations(phrase);
        List categoryList = wikiOp.getWikipediaCategory();
        addChildrenToParent(categoryList, n);
    }

    void addChildrenToParent(List categoryList, BloomsNode n)
    {
        for(int i = 0; i < categoryList.size(); i++)
        {
            String category = (String)categoryList.get(i);
            BloomsNode node = new BloomsNode(category, true);
            node.setParent(n);
        }

    }

    public void align(Alignment alignment, Properties properties)
        throws AlignmentException
    {
    }

    public Cell match(String o1, String o2)
    {
        BasicCell c = null;
        String s1;
        String s2;
        s1 = o1;
        s2 = o2;
        if(s1 == null || s2 == null)
            return c;
        if(s1.toLowerCase().equals(s2.toLowerCase()))
        {
            String cellIDStr = Integer.toString(cellID);
            if(ont1NameMapper.get(s1) != null && ont2NameMapper.get(s2) != null)
            {
                String arg1 = ((OntClass)ont1NameMapper.get(s1)).toString();
                String arg2 = ((OntClass)ont2NameMapper.get(s2)).toString();
                URI uri1 = null;
                URI uri2 = null;
				try {
					uri2 = new URI(arg2);
					uri1 = new URI(arg1);
	                double strength = 1.0D;
	                c = new BasicCell(cellIDStr, uri1, uri2, BloomsRelationship.getEquivalenceRelation(), strength);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AlignmentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            return c;
        }
        try
        {
            return c;
        }
        catch(Exception owex)
        {
            owex.printStackTrace();
        }
        return c;
    }

    

    String outputFile;
    static int cellID = 1;
    URI ontology1;
    URI ontology2;
    HashMap ont1NameMapper;
    HashMap ont2NameMapper;
    HashMap ont1ConceptToTreeMap;
    HashMap ont2ConceptToTreeMap;

}
