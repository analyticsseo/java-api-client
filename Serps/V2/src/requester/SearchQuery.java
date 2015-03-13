/**
 * Class SearchRequest
 * Java sample code
 * Java class to format a APIv2 search query
 */

package requester;

public class SearchQuery
{
	// search parameters
	private String searchEngine; 
	private String region;
	private String town;
	private String searchType;
	private String language;
	private String maxResults;
	private String phrase;
	private String universal;
	
	/**
	 * @see class constructor, set all parameters to null
	 */
	public SearchQuery()
	{
		this.setSearchEngine(null);
		this.setRegion(null);
		this.setTown(null);
		this.setSearchType(null);
		this.setLanguage(null);
		this.setMaxResults(null);
		this.setPhrase(null);
		this.setUniversal(null);
	}
	
	/**
	 * @see check if mandatory parameters are not missing
	 * @return boolean query is correct or not
	 */
	public boolean isCorrect()
	{
		boolean isCorrect = false;
				
			if( this.getSearchEngine() != null && this.getRegion() != null && this.getPhrase() != null )
				isCorrect = true;
			
		return isCorrect;
	}
	
	/**
	 * @see stringify the query object
	 * @return String the formatted query
	 */
	public String toString()
	{
		String query = null;
	
		if (this.getSearchEngine() != null)
			query = "http://v2.api.analyticsseo.com/search_results?search_engine="+this.getSearchEngine();
		
		if (this.getRegion() != null)
			query += "&region="+this.getRegion();
		
		if (this.getTown() != null)
			query += "&town="+this.getTown();
		
		if (this.getSearchType() != null)
			query += "&search_type="+this.getSearchType();
		
		if (this.getLanguage() != null)
			query += "&language="+this.getLanguage();
		
		if (this.getMaxResults() != null)
			query += "&max_results="+this.getMaxResults();
		
		if (this.getPhrase() != null)
			query += "&phrase="+this.getPhrase().replace(" ", "+"); //Manual URL encode
		
		if (this.getUniversal() != null)
			query += "&universal="+this.getUniversal();
			
		return query;
	}
	
	//GETTERS and SETTERS for the search parameters

	public String getSearchEngine()
	{
		return searchEngine;
	}

	public void setSearchEngine(String searchEngine)
	{
		this.searchEngine = searchEngine;
	}

	public String getRegion()
	{
		return region;
	}

	public void setRegion(String region) 
	{
		this.region = region;
	}

	public String getTown()
	{
		return town;
	}

	public void setTown(String town)
	{
		this.town = town;
	}

	public String getSearchType() 
	{
		return searchType;
	}

	public void setSearchType(String searchType)
	{
		this.searchType = searchType;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public String getMaxResults()
	{
		return maxResults;
	}

	public void setMaxResults(String maxResults) 
	{
		this.maxResults = maxResults;
	}

	public String getPhrase()
	{
		return phrase;
	}

	public void setPhrase(String phrase)
	{
		this.phrase = phrase;
	}

	public String getUniversal() 
	{
		return universal;
	}

	public void setUniversal(String universal)
	{
		this.universal = universal;
	}
}
