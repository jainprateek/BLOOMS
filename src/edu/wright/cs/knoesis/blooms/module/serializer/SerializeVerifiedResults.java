// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SerializeVerifiedResults.java

package edu.wright.cs.knoesis.blooms.module.serializer;

import com.hp.hpl.jena.ontology.OntClass;
import edu.wright.cs.knoesis.ontologyoperations.OntologyProcessor;
import fr.inrialpes.exmo.align.impl.BasicAlignment;
import fr.inrialpes.exmo.align.impl.renderer.RDFRendererVisitor;
import fr.inrialpes.exmo.ontowrap.jena.JENAOntology;
import fr.inrialpes.exmo.ontowrap.jena.JENAOntologyFactory;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import org.semanticweb.owl.align.AlignmentException;

public class SerializeVerifiedResults
{

    public SerializeVerifiedResults(String fileName, String outputFile, String ont1URI, String ont2URI)
    {
        this.fileName = "";
        this.ont1URI = "";
        this.ont2URI = "";
        ont1Mapper = null;
        ont2Mapper = null;
        this.outputFile = "";
        result = null;
        this.fileName = fileName;
        this.outputFile = outputFile;
        this.ont1URI = ont1URI;
        this.ont2URI = ont2URI;
    }

    public static void main(String args[])
    {
        SerializeVerifiedResults svr = new SerializeVerifiedResults("/Users/prateekjain/Documents/workspace/Blooms/results/verified_result.csv", "alignment.rdf", "http://motools.sourceforge.net/doc/musicontology.rdfs", "http://proton.semanticweb.org/2005/04/protonu");
        svr.serialize();
    }

    void serializeInAlignmentAPIFormat()
    {
        try
        {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            OntologyProcessor op1 = new OntologyProcessor(ont1URI);
            ont1Mapper = op1.getLocalNameOntClassMapper();
            OntologyProcessor op2 = new OntologyProcessor(ont2URI);
            ont2Mapper = op2.getLocalNameOntClassMapper();
            String str;
            while((str = in.readLine()) != null) 
                process(str);
            in.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    void process(String str)
    {
        String elements[] = str.split(",");
        int proofValue1 = changeToNumber(elements[1]);
        int proofValue2 = changeToNumber(elements[3]);
        if(proofValue1 + proofValue2 > 0)
            if(proofValue1 > proofValue2)
                splitClassNames(elements[0]);
            else
                splitClassNames(elements[2]);
    }

    int changeToNumber(String str)
    {
        Integer value = Integer.valueOf(Integer.parseInt(str.trim()));
        return value.intValue();
    }

    void splitClassNames(String str)
    {
        String relation = "";
        String classNames[] = str.split("<");
        if(classNames.length == 1)
        {
            classNames = str.split("=");
            relation = "=";
        } else
        {
            relation = "<";
        }
        String normalizedClassName = classNames[0];
        String normalizedClassName1 = classNames[1];
        OntClass class1 = (OntClass)ont1Mapper.get(normalizedClassName.trim().toLowerCase());
        OntClass class2 = (OntClass)ont2Mapper.get(normalizedClassName1.trim().toLowerCase());
        if(class1 != null && class2 != null)
            createCell(class1.getURI(), class2.getURI(), relation);
    }

    public void createCell(String o1, String o2, String relation)
    {
        try
        {
            String s1 = o1;
            String s2 = o2;
            URI uri1 = new URI(s1);
            URI uri2 = new URI(s2);
            result.addAlignCell(uri1, uri2, relation, 1.0D);
        }
        catch(Exception owex)
        {
            owex.printStackTrace();
        }
    }

    public String serialize()
    {
        result = new BasicAlignment();
        String filePath = "";
        JENAOntologyFactory factory1 = new JENAOntologyFactory();
        URI ontology1 = null;
        URI ontology2 = null;
        try
        {
            ontology1 = new URI(ont1URI);
            ontology2 = new URI(ont2URI);
        }
        catch(URISyntaxException e)
        {
            e.printStackTrace();
        }
        JENAOntology jenaOntology1 = null;
        JENAOntology jenaOntology2 = null;
        try
        {
            OntologyProcessor op1 = new OntologyProcessor(ont1URI);
            com.hp.hpl.jena.ontology.OntModel ontModel1 = op1.loadRDFOntology();
            jenaOntology1 = new JENAOntology();
            jenaOntology1.setOntology(ontModel1);
            OntologyProcessor op2 = new OntologyProcessor(ont2URI);
            com.hp.hpl.jena.ontology.OntModel ontModel2 = op2.loadRDFOntology();
            jenaOntology2 = new JENAOntology();
            jenaOntology2.setOntology(ontModel2);
        }
        catch(Exception e2)
        {
            e2.printStackTrace();
        }
        try
        {
            jenaOntology1.setURI(ontology1);
            jenaOntology2.setURI(ontology2);
            result.init(jenaOntology1, jenaOntology2);
            result.setOntology1(jenaOntology1);
            result.setOntology2(jenaOntology2);
            result.setFile1(jenaOntology1.getFile());
            result.setFile2(jenaOntology2.getFile());
            serializeInAlignmentAPIFormat();
            try
            {
                File file = new File(outputFile);
                PrintWriter writer = new PrintWriter(file);
                org.semanticweb.owl.align.AlignmentVisitor renderer = new RDFRendererVisitor(writer);
                result.render(renderer);
                writer.flush();
                writer.close();
                filePath = file.getAbsolutePath();
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
        catch(AlignmentException e1)
        {
            e1.printStackTrace();
        }
        return filePath;
    }

    void copy(File src, File dst)
        throws IOException
    {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte buf[] = new byte[1024];
        int len;
        while((len = in.read(buf)) >= 0) 
            out.write(buf, 0, len);
        out.flush();
        in.close();
        out.close();
    }

    String fileName;
    String ont1URI;
    String ont2URI;
    HashMap ont1Mapper;
    HashMap ont2Mapper;
    String outputFile;
    BasicAlignment result;
}
