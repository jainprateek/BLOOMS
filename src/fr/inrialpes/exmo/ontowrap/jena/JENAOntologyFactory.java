// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JENAOntologyFactory.java

package fr.inrialpes.exmo.ontowrap.jena;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import fr.inrialpes.exmo.ontowrap.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

// Referenced classes of package fr.inrialpes.exmo.ontowrap.jena:
//            JENAOntology

public class JENAOntologyFactory extends OntologyFactory {

    private static URI formalismUri = null;
    private static String formalismId = "OWL1.0";

    private static OntologyCache<JENAOntology> cache = null;

    public JENAOntologyFactory() {
	cache = new OntologyCache<JENAOntology>();
	try { 
	    formalismUri = new URI("http://www.w3.org/2002/07/owl#");
	} catch (URISyntaxException ex) { ex.printStackTrace(); } // should not happen
    }

    public JENAOntology newOntology( Object ontology ) throws OntowrapException {
	if ( ontology instanceof OntModel ) {
	    JENAOntology onto = new JENAOntology();
	    onto.setFormalism( formalismId );
	    onto.setFormURI( formalismUri );
	    onto.setOntology( (OntModel)ontology );
	    //onto.setFile( uri );// unknown
	    // to be checked : why several ontologies in a model ???
	    // If no URI can be extracted from ontology, then we use the physical URI
	    try {
		try {
		    onto.setURI(new URI(((OntModel)ontology).listOntologies().next().getURI()));
		} catch (NoSuchElementException nse) {
		    // JE: not verysafe
		    onto.setURI(new URI(((OntModel)ontology).getNsPrefixURI("")));
		}
	    } catch ( URISyntaxException usex ){
		// Better put in the OntowrapException of loaded
		throw new OntowrapException( "URI Error ", usex );
	    }
	    cache.recordOntology( onto.getURI(), onto );
	    return onto;
	} else {
	    throw new OntowrapException( "Argument is not an OntModel: "+ontology );
	}
    }

    public JENAOntology loadOntology( URI uri ) throws OntowrapException {
	JENAOntology onto = null;
	onto = cache.getOntologyFromURI( uri );
	if ( onto != null ) return onto;
	onto = cache.getOntology( uri );
	if ( onto != null ) return onto;
	try {
	    OntModel m = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
	    m.read(uri.toString());
	    onto = new JENAOntology();
	    onto.setFile(uri);
	    // to be checked : why several ontologies in a model ???
	    // If no URI can be extracted from ontology, then we use the physical URI
	    try {
		onto.setURI(new URI(m.listOntologies().next().getURI()));
	    } catch (NoSuchElementException nse) {
		onto.setURI(new URI(m.getNsPrefixURI("")));
		//onto.setFile(uri);
	    }
	    //onto.setURI(new URI(m.listOntologies()getOntology(null).getURI()));
	    onto.setOntology(m);
	    cache.recordOntology( uri, onto );
	    return onto;
        } catch (Exception e) {
	    throw new OntowrapException("Cannot load "+uri, e );
	}
    }

    @Override
    public void clearCache() {
	try {
		cache.clear();
	} catch (OntowrapException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    };

}
