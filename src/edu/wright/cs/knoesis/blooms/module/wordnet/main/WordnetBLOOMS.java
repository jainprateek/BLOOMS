// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   WordnetBLOOMS.java

package edu.wright.cs.knoesis.blooms.module.wordnet.main;

import com.hp.hpl.jena.ontology.OntClass;
import edu.mit.jwi.item.IWord;
import edu.wright.cs.knoesis.blooms.module.wordnet.wordnetop.WordnetTester;
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

public class WordnetBLOOMS extends BasicAlignment
    implements AlignmentProcess
{

    public WordnetBLOOMS()
    {
        outputFile = "";
        ontology1 = null;
        ontology2 = null;
        ont1NameMapper = new HashMap();
        ont2NameMapper = new HashMap();
        ont1ConceptToTreeMap = new HashMap();
        ont2ConceptToTreeMap = new HashMap();
        outputFile = "alignment.rdf";
    }

    public WordnetBLOOMS(String outputFile)
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

    public WordnetBLOOMS(URI ontology1, URI ontology2)
    {
        outputFile = "";
        this.ontology1 = null;
        this.ontology2 = null;
        ont1NameMapper = new HashMap();
        ont2NameMapper = new HashMap();
        ont1ConceptToTreeMap = new HashMap();
        ont2ConceptToTreeMap = new HashMap();
        this.ontology1 = ontology1;
        this.ontology2 = ontology2;
    }

    public static void main(String args[])
    {
        WordnetBLOOMS wnBlooms = null;
        try
        {
            wnBlooms = new WordnetBLOOMS(new URI("http://xmlns.com/foaf/spec/index.rdf"), new URI("http://xmlns.com/foaf/spec/index.rdf"));
        }
        catch(URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public void match(URI ontology1, URI ontology2, Double tHold)
    {
        System.out.println((new StringBuilder("Ontology 1 Schema:")).append(ontology1.getScheme()).toString());
        System.out.println((new StringBuilder("Ontology 2 Schema:")).append(ontology2.getScheme()).toString());
        if(ontology1.getScheme() == null)
            try
            {
                ontology1 = new URI((new StringBuilder("file:///")).append(ontology1.toString()).toString());
            }
            catch(URISyntaxException e)
            {
                e.printStackTrace();
            }
        if(ontology2.getScheme() == null)
            try
            {
                ontology2 = new URI((new StringBuilder("file:///")).append(ontology2.toString()).toString());
            }
            catch(URISyntaxException e)
            {
                e.printStackTrace();
            }
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
        WordnetTester wnTester = new WordnetTester();
        for(int i = 0; i < ont1NormalizedNameList.size(); i++)
        {
            String className = (String)ont1NormalizedNameList.get(i);
            IWord iword = wnTester.getIWordForPhrase(className);
            if(iword != null)
            {
                BloomsTree hypernymTree = createBloomsTree(iword);
                BreadthFirstSearch bfsSearch = new BreadthFirstSearch((BloomsNode)hypernymTree.getRoot());
                ont1ConceptToTreeMap.put(className, hypernymTree);
            }
        }

        for(int i = 0; i < ont2NormalizedNameList.size(); i++)
        {
            String className = (String)ont2NormalizedNameList.get(i);
            IWord iword = wnTester.getIWordForPhrase(className);
            if(iword != null)
            {
                BloomsTree hypernymTree = createBloomsTree(iword);
                BreadthFirstSearch bfsSearch = new BreadthFirstSearch((BloomsNode)hypernymTree.getRoot());
                ont2ConceptToTreeMap.put(className, hypernymTree);
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
            for(Iterator ont2ConceptItr = ont2ConceptToTreeMap.keySet().iterator(); ont2ConceptItr.hasNext();)
            {
                String concept2 = (String)ont2ConceptItr.next();
                BloomsTree targetTree = (BloomsTree)ont2ConceptToTreeMap.get(concept2);
                BloomsRelationshipFinder wtrf = new BloomsRelationshipFinder();
                BasicRelation rel = wtrf.identifyRelationship(sourceTree, targetTree);
                System.out.println((new StringBuilder("Concept-1:")).append(concept1).toString());
                System.out.println((new StringBuilder("Concept-2:")).append(concept2).toString());
                HashMap commonNodeHMap = sourceTree.commonNodes(targetTree);
                BloomsNode n;
                for(Iterator nodeItr = commonNodeHMap.keySet().iterator(); nodeItr.hasNext(); System.out.println((new StringBuilder("Common Node:")).append(n.getNodeName()).append(" Level:").append(n.getLevel()).toString()))
                    n = (BloomsNode)nodeItr.next();

                String arg1 = ((OntClass)ont1NameMapper.get(concept1)).toString();
                String arg2 = ((OntClass)ont2NameMapper.get(concept2)).toString();
                try
                {
                    URI uri1 = new URI(arg1);
                    URI uri2 = new URI(arg2);
                    if(rel != null)
                    {
                        double strength = wtrf.getRelationshipStrength().doubleValue();
                        if(strength > tHold.doubleValue())
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

    void printHypernyms(String phrase, List hypernymList)
    {
        for(int i = 0; i < hypernymList.size(); i++)
        {
            IWord iword = (IWord)hypernymList.get(i);
        }

    }

    BloomsTree createBloomsTree(IWord phrase)
    {
        BloomsNode rootNode = new BloomsNode(phrase, true);
        BloomsTree hypernymTree = new BloomsTree(rootNode, true);
        constructHypernymTree(rootNode);
        for(int i = 0; i < 3; i++)
        {
            List bloomsNodeList = hypernymTree.getLeaves();
            for(int k = 0; k < bloomsNodeList.size(); k++)
            {
                BloomsNode n = (BloomsNode)bloomsNodeList.get(k);
                constructHypernymTree(n);
            }

        }

        return hypernymTree;
    }

    BloomsNode constructHypernymTree(BloomsNode node)
    {
        IWord phrase = (IWord)node.getUserObject();
        constructHypernymTree(phrase.getLemma(), node);
        return node;
    }

    void constructHypernymTree(String phrase, BloomsNode n)
    {
        WordnetTester wnTester = new WordnetTester();
        List hypernymList = wnTester.getHypernym(phrase);
        addChildrenToParent(hypernymList, n);
    }

    void addChildrenToParent(List hypernymList, BloomsNode n)
    {
        for(int i = 0; i < hypernymList.size(); i++)
        {
            IWord hypernym = (IWord)hypernymList.get(i);
            BloomsNode node = new BloomsNode(hypernym, true);
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
                URI uri1;
				try {
					uri1 = new URI(arg1);
					URI uri2 = new URI(arg2);
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
