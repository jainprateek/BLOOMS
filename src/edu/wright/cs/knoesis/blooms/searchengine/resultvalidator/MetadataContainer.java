/**
 * 
 */
package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator;

import java.util.List;

/**
 * @author prateek
 *
 */
public class MetadataContainer {
	public Metadata __metadata;
	public List<WebResults> Web;

	/**
	 * @return the __metadata
	 */
	public Metadata get__metadata() {
		return __metadata;
	}

	/**
	 * @param __metadata the __metadata to set
	 */
	public void set__metadata(Metadata __metadata) {
		this.__metadata = __metadata;
	}
}
