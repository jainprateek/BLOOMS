package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator;

public class Metadata{
    public String uri;
    public String Query;
    public String ID;
    public String Title;
    public String Description;
    public String DisplayUrl;
    public String Url;
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Metadata [uri=" + uri + ", Query=" + Query + ", ID=" + ID
				+ ", Title=" + Title + ", Description=" + Description
				+ ", DisplayUrl=" + DisplayUrl + ", Url=" + Url + "]";
	}
}
