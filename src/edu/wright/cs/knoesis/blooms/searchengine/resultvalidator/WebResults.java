/**
 * 
 */
package edu.wright.cs.knoesis.blooms.searchengine.resultvalidator;

import java.util.ArrayList;

/**
 * @author prateek
 *
 */
public class WebResults {

	    public String Title;
	    public String Description;
	    public String DisplayUrl;
	    public String Url;

	    /* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "WebResults [Title=" + Title + ", Description="
					+ Description + ", DisplayUrl=" + DisplayUrl + ", Url="
					+ Url + "]";
		}

		/**
		 * @return the title
		 */
		public String getTitle() {
			return Title;
		}

		/**
		 * @param title the title to set
		 */
		public void setTitle(String title) {
			Title = title;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return Description;
		}

		/**
		 * @param description the description to set
		 */
		public void setDescription(String description) {
			Description = description;
		}

		/**
		 * @return the displayUrl
		 */
		public String getDisplayUrl() {
			return DisplayUrl;
		}

		/**
		 * @param displayUrl the displayUrl to set
		 */
		public void setDisplayUrl(String displayUrl) {
			DisplayUrl = displayUrl;
		}

		/**
		 * @return the url
		 */
		public String getUrl() {
			return Url;
		}

		/**
		 * @param url the url to set
		 */
		public void setUrl(String url) {
			Url = url;
		}
	
		
}
