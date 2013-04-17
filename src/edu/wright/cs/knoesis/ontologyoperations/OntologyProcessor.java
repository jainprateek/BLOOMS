// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   OntologyProcessor.java

package edu.wright.cs.knoesis.ontologyoperations;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import edu.wright.cs.knoesis.stringoperations.NormalizeClassName;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

public class OntologyProcessor
{

    public URI getOntology()
    {
        URI u = null;
        try
        {
            u = new URI(ontology);
        }
        catch(URISyntaxException e)
        {
            e.printStackTrace();
        }
        return u;
    }

    public void setOntology(String ontology)
    {
        this.ontology = ontology;
    }

    public OntologyProcessor(String ontologyURI)
    {
        this.ontologyURI = "";
        localNameOntClassMapper = null;
        ontology = "";
        this.ontologyURI = ontologyURI;
    }

    public static void main(String args[])
    {
        OntologyProcessor obj = new OntologyProcessor("http://knoesis1.cs.wright.edu/students/prateek/dbpedia.owl");
        obj.getSubClasses("http://www.w3.org/2002/07/owl#Thing");
    }

    public OntModel loadRDFOntology()
    {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
        InputStream in = FileManager.get().open(ontologyURI);
        if(in == null)
        {
            throw new IllegalArgumentException((new StringBuilder("File: ")).append(ontologyURI).append(" not found").toString());
        } else
        {
            setOntology(ontologyURI);
            model.read(in, null);
            return model;
        }
    }

    private OntModel loadOWLOntology()
    {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        InputStream in = FileManager.get().open(this.ontologyURI);
        if(in == null)
            throw new IllegalArgumentException((new StringBuilder("File: ")).append(this.ontologyURI).append(" not found").toString());
        model.read(in, null);
        ExtendedIterator ontItr = model.listOntologies();
        String ontologyURI;
        Ontology o;
        for(ontologyURI = ""; ontItr.hasNext(); ontologyURI = o.getURI())
            o = (Ontology)ontItr.next();

        setOntology(ontologyURI);
        return model;
    }

    private ArrayList getOntologyClasses(OntModel model)
    {
        ExtendedIterator ontClassItr = model.listClasses();
        ArrayList ontClassList = new ArrayList();
        OntClass c;
        for(; ontClassItr.hasNext(); ontClassList.add(c))
            c = (OntClass)ontClassItr.next();

        return ontClassList;
    }

    public ArrayList getOntologyClassList()
    {
        OntModel model = loadOWLOntology();
        ExtendedIterator ontClassItr = model.listClasses();
        ArrayList ontClassList = new ArrayList();
        OntClass c;
        for(; ontClassItr.hasNext(); ontClassList.add(c))
            c = (OntClass)ontClassItr.next();

        return ontClassList;
    }

    public ArrayList getOntologyClasses()
    {
        OntModel model = loadOntology();
        System.out.println("\nLoading Ontology");
        ArrayList ontClassList = getOntologyClasses(model);
        ArrayList classNamesList = null;
        if(ontClassList != null)
        {
            localNameOntClassMapper = new HashMap();
            classNamesList = new ArrayList();
            for(int i = 0; i < ontClassList.size(); i++)
            {
                OntClass c = (OntClass)ontClassList.get(i);
                if(!c.isAnon())
                {
                    String className = c.getLocalName();
                    NormalizeClassName ncn = new NormalizeClassName(className);
                    className = ncn.normalize();
                    System.out.println((new StringBuilder("Class Name:")).append(className.toLowerCase()).toString());
                    classNamesList.add(className.toLowerCase());
                    localNameOntClassMapper.put(className.toLowerCase(), c);
                }
            }

        }
        return classNamesList;
    }

    private OntModel loadOntology()
    {
        OntModel m = loadOWLOntology();
        ExtendedIterator classItr = m.listClasses();
        if(!classItr.hasNext())
            m = loadRDFOntology();
        return m;
    }

    public HashMap getLocalNameOntClassMapper()
    {
        if(localNameOntClassMapper == null)
            getOntologyClasses();
        return localNameOntClassMapper;
    }

    public ArrayList getSubClasses(String className)
    {
        OntModel model = loadOWLOntology();
        ArrayList subClassList = new ArrayList();
        Resource r = model.getOntClass(className);
        if(r instanceof OntClass)
        {
            OntClass o = (OntClass)r;
            NormalizeClassName ncName = new NormalizeClassName(o.getLocalName());
            String strNormalized = ncName.normalize();
            subClassList.add(strNormalized);
            for(ExtendedIterator subClassItr = o.listSubClasses(); subClassItr.hasNext(); subClassList.add(strNormalized))
            {
                OntClass c = (OntClass)subClassItr.next();
                String str = c.getLocalName();
                ncName = new NormalizeClassName(str);
                strNormalized = ncName.normalize();
            }

        }
        return subClassList;
    }

    String ontologyURI;
    HashMap localNameOntClassMapper;
    String ontology;
}
