// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JENAOntology.java

package fr.inrialpes.exmo.ontowrap.jena;

import com.hp.hpl.jena.ontology.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.rdf.model.impl.LiteralImpl;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Map1;
import fr.inrialpes.exmo.ontowrap.*;
import fr.inrialpes.exmo.ontowrap.util.EntityFilter;
import java.net.URI;
import java.util.*;

public class JENAOntology extends BasicOntology<OntModel> implements LoadedOntology<OntModel>{

    // JE: this is not very Java 1.5...
    // This is because of the version of Jena we use apparently

    public Object getEntity(URI u) throws OntowrapException {
	return onto.getOntResource(u.toString());
    }
    
    public void getEntityAnnotations( Object o, Set<String> annots, String lang) throws OntowrapException {
	
	StmtIterator stmtIt = onto.listStatements((Resource)o,null,(RDFNode)null);
	while (stmtIt.hasNext()) {
	    Statement st = stmtIt.next();
	    
	    if ( st.getPredicate().canAs(AnnotationProperty.class)) {
		RDFNode obj= st.getObject();
		if (obj.isLiteral()) {
		    Literal l =obj.as(Literal.class);
		    if (lang==null || lang.equals(l.getLanguage())) {
			annots.add(l.getLexicalForm());
		    }
		    else if (obj.isResource()) {
			getEntityAnnotations(obj, annots, lang);
		    }
		}
	    }
	}
	
	// THIS SHOULD BE STATIC NOW!!
	/*Iterator<AnnotationProperty> z = onto.listAnnotationProperties();
	while (z.hasNext()) {
	    AnnotationProperty ap = z.next();
	   // System.out.println("hjhjhjh"+ap);
	    NodeIterator j = ((OntResource)o).listPropertyValues((Property) ap);
	    while (j.hasNext()) {
		RDFNode n = j.nextNode();
		if (n.isResource())
		    getEntityAnnotations(n, annots);
		else if (n.isLiteral()) {
		    annots.add( ((Literal)n.as(Literal.class)).getLexicalForm() );
		}
	    }
	}*/
    }

    public Set<String> getEntityAnnotations(Object o) throws OntowrapException {
	Set<String> annots = new HashSet<String>();
	getEntityAnnotations(o,annots,null);
	return annots;
    }

    public Set<String> getEntityAnnotations( Object o, String lang ) throws OntowrapException {
	Set<String> annots = new HashSet<String>();
	getEntityAnnotations(o,annots,lang);
	return annots;
    }
    
    public Set<String> getEntityComments(Object o, String lang) throws OntowrapException {
	Set<String> comments = new HashSet<String>();
	Iterator<RDFNode> it = ((OntResource)o).listComments(lang);
	while (it.hasNext()) {
	    comments.add( ((LiteralImpl)it.next()).getLexicalForm() );
	}
	return comments;
    }

    public Set<String> getEntityComments(Object o) throws OntowrapException {
	return getEntityComments(o,null);
    }


    public String getEntityName( Object o ) throws OntowrapException {
	try {
	    // Should try to get labels first... (done in the OWLAPI way)
	    return getFragmentAsLabel( new URI( ((OntResource) o).getURI() ) );
	} catch ( Exception oex ) {
	    return null;
	}
    }

    public String getEntityName( Object o, String lang ) throws OntowrapException {
	// Should first get the label in the language
	return getEntityName( o );
    }

    public Set<String> getEntityNames(Object o, String lang) throws OntowrapException {
	Set<String> labels = new HashSet<String>();
	OntResource or = (OntResource) o;
	Iterator<?> i = or.listLabels(lang);
	while (i.hasNext()) {
	    String label = ((LiteralImpl) i.next()).getLexicalForm();
	    labels.add(label);
	}
	return labels;
    }

    public Set<String> getEntityNames(Object o) throws OntowrapException {
	return getEntityNames(o,null);
    }

    public URI getEntityURI(Object o) throws OntowrapException {
	try {
	    OntResource or = (OntResource) o;
	    return new URI(or.getURI());
	} catch (Exception e) {
	    throw new OntowrapException(o.toString()+" do not have uri", e );
	}
    }

    /*
     * This strange structure, as well as the corresponding JENAEntityIt
     * is there only because Jena may return unammed entities that have to
     * be filtered out from the sets.
     *
     */
    /*protected <R extends OntResource> Set<R> getEntitySet(final ExtendedIterator<R> i) {
	return new AbstractSet<R>() {
	    private int size=-1;
	    public Iterator<R> iterator() {
		return new JENAEntityIt( getURI(), i );
	    }
	    public int size() {
		if (size==-1) {
		    for (R r : this)
			size++;
		    size++;
		}
		return size;
	    }
	};
    }*/

    public Set<OntClass> getClasses() {
	return new EntityFilter<OntClass>(onto.listNamedClasses().toSet(),this);
    }

    public Set<DatatypeProperty> getDataProperties() {
	return new EntityFilter<DatatypeProperty>(onto.listDatatypeProperties().toSet(),this);
	//return getEntitySet(onto.listDatatypeProperties());
    }

    protected final static Map1<OntProperty,OntResource> mapProperty = new Map1<OntProperty,OntResource> () { public OntResource map1 ( OntProperty o ) { return o; } };
    protected final static Map1<OntClass,OntResource> mapClass = new Map1<OntClass,OntResource> () { public OntResource map1 ( OntClass o ) { return o; } };
    protected final static Map1<Individual,OntResource> mapInd = new Map1<Individual,OntResource> () { public OntResource map1 ( Individual o ) { return o; } };
    
    
    public Set<OntResource> getEntities() {
	return new EntityFilter<OntResource>((onto.listAllOntProperties().mapWith( mapProperty )
		    .andThen(onto.listNamedClasses().mapWith( mapClass ))
		    .andThen(onto.listIndividuals().mapWith( mapInd )).toSet()),
		    this);
    }

    public Set<Individual> getIndividuals() {
	return new EntityFilter<Individual>(onto.listIndividuals().toSet(),this);
    }

    public Set<ObjectProperty> getObjectProperties() {
	return new EntityFilter<ObjectProperty>(onto.listObjectProperties().toSet(),this);
    }

    public Set<OntProperty> getProperties() {
	return new EntityFilter<OntProperty>(onto.listAllOntProperties().toSet(),this);
	/*return getEntitySet( onto.listAllOntProperties().filterDrop( new Filter () {
		public boolean accept( Object o ) { return (o instanceof AnnotationProperty); }
	    }) );*/
	//return getEntitySet(onto.listObjectProperties().andThen(onto.listDatatypeProperties()));
    }

    public boolean isClass(Object o) {
	return o instanceof OntClass;
    }

    public boolean isDataProperty(Object o) {
	return o instanceof DatatypeProperty;
    }

    public boolean isEntity(Object o) {
	return isClass(o)||isProperty(o)||isIndividual(o);
    }

    public boolean isIndividual(Object o) {
	return o instanceof Individual;
    }

    public boolean isObjectProperty(Object o) {
	return o instanceof ObjectProperty;
    }

    public boolean isProperty(Object o) {
	return o instanceof OntProperty;
    }

    public int nbEntities() {
	return this.getEntities().size();
    }

    public int nbClasses() {
	return this.getClasses().size();
    }

    public int nbDataProperties() {
	return this.getDataProperties().size();
    }

    public int nbIndividuals() {
	return this.getIndividuals().size();
    }

    public int nbObjectProperties() {
	return this.getObjectProperties().size();
    }

    public int nbProperties() {
	return this.getProperties().size();
    }

    public void unload() {

    }

	@Override
	public Map<String, String> getEntityAnnotationsL(Object arg0)
			throws OntowrapException {
		// TODO Auto-generated method stub
		return null;
	}


}