/**
 * @see Class Request
 * @see Java sample code
 * @see HTTP Request for the API v2
 * @see Requiere "json-simple-1.1.1.jar" to add to the build path
 */

package requester;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Request
{
	
	private String privateKey = ""; // API private key
	private String publicKey = ""; // API public key
	private String salt = ""; // API salt
	private String timestamp;

	
	/**
	 * @see Create hashed key from the UNIX timestamp, the private key and the salt 
	 * @return String encoded key
	 */
	public String createHashedKey()
	{
		
		privateKey = this.getPrivateKey();
		salt = this.getPublicKey();
		timestamp = this.getTimestamp();
		
		String hashedString = new String();
		SecretKeySpec secretKey;
		
		String wholeStr = timestamp + privateKey + salt;
		
		try 
		{
			secretKey = new SecretKeySpec((privateKey).getBytes("UTF-8"), "HmacSHA256");
			
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(secretKey);
			
			StringBuffer hashBuffer = new StringBuffer();
			
			byte[] wholeStrBytes = mac.doFinal(wholeStr.getBytes("ASCII"));
			
			for (int i = 0; i < wholeStrBytes.length; i++) 
			{
				String hexaConverted = Integer.toHexString(0xFF & wholeStrBytes[i]);
				
				if (hexaConverted.length() == 1) 
				{
					hashBuffer.append('0');
				}
				
				hashBuffer.append(hexaConverted);
			}
			
			hashedString = hashBuffer.toString();
			
		} 
		catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException e) 
		{
			// TODO Gerer les exceptions
			e.printStackTrace();
		}
		
		return hashedString;
	}
	
	/**
	 * @see Create authorization key from the hashed key and the public public
 	 * @return String authorization key
	 */
	public String createAuthorizationString()
	{
		String authorizationString = new String();
		
		// see the documentation for the syntax
		authorizationString = " KeyAuth publicKey="+this.getPublicKey()+
							  " hash="+this.createHashedKey()+
							  " ts="+this.getTimestamp();
				 
		return authorizationString;
	}
	
	/**
	 * @see Send HTTP GET request from URL with the authorization key set in the header
 	 * @return String request response
	 */
	public String sendHttpGetRequest(String url)
	{
		StringBuffer apiResponse = new StringBuffer();
		
		this.setTimestamp(Long.toString(System.currentTimeMillis() / 1000L));
		
		try
		{
			URL apiURL = new URL(url);
			HttpURLConnection myURLConnection = (HttpURLConnection) apiURL.openConnection();
			myURLConnection.setRequestProperty ("Authorization", this.createAuthorizationString());
			myURLConnection.setRequestMethod("GET");
			
			InputStreamReader streamReader  = new InputStreamReader(myURLConnection.getInputStream());
			BufferedReader in = new BufferedReader(streamReader);
	
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) 
			{
				apiResponse.append(inputLine);
			}
			
			in.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return (String) null;
		}
		
		return apiResponse.toString();
	}
	
	/**
	 * @see Format a search request according to the parameter included in the SearchQuery object
	 * @return String Job id
	 */
	public String sendSearchRequest(SearchQuery searchQuery)
	{	
		String jsonResult = null;
		String jobId = null;
		
		if (searchQuery.isCorrect())
		{
			jsonResult = sendHttpGetRequest(searchQuery.toString());
		}
		else
		{
			System.out.println("Missing parameters (search_engine, region, search_type, phrases ? /n)");
			return jobId;
		}
		
		try 
		{	
			JSONParser jsonParser = new JSONParser();
			JSONObject responseObj;
			responseObj = (JSONObject) jsonParser.parse(jsonResult);
			jobId = responseObj.get("jid").toString();
		}
		
		catch (ParseException e)
		{
			// TODO Manage exceptions
			e.printStackTrace();
		}
		
		return jobId;
	}
	
	/**
	 * @see Format a get_job request according to the job id
	 * TODO parse the JSON to obtain specific result
	 * @return String request response 
	 */
	public String getJobResult(String jobId)
	{
		String jsonResult = null;
		
		if (jobId != null)
		{
			jsonResult = sendHttpGetRequest("http://v2.api.analyticsseo.com/get_job/"+jobId);
		}
		else
		{
			System.out.println("Job id is null !");
			return jsonResult;
		}
		
		try 
		{
			JSONParser jsonParser = new JSONParser();
			JSONObject responseObj;
			responseObj = (JSONObject) jsonParser.parse(jsonResult);
			// TODO parse the JSON to obtain specific result
			jsonResult = responseObj.toJSONString();
			
			
		}
		catch (ParseException e) 
		{
			e.printStackTrace();
			// TODO Manage exceptions
		}
		
		return jsonResult;
	}
	
	//GETTERS
	
	public String getTimestamp()
	{
		return timestamp;
	}
	
	public String getPublicKey() 
	{
		return publicKey;
	}
	
	public String getPrivateKey() 
	{
		return privateKey;
	}
	

	public String getSalt() {
		return salt;
	}
	
	//SETTERS

	public void setTimestamp(String timestamp) 
	{
		this.timestamp = timestamp;
	}
	
	/**
	 * @see Main loop for testing
	 */
	public static void main (String[] args)
	{
		Request request = new Request();
		SearchQuery SearchQuery = new SearchQuery();
		String jid = null;
		
//		Mandatory parameters
		SearchQuery.setSearchEngine("google");
		SearchQuery.setRegion("global"); 
		SearchQuery.setPhrase("rent boat ajaccio");
		SearchQuery.setSearchType("web");
		
//		Optionnal parameters (see the documentation)
//		SearchQuery.setLanguage("en");
//		SearchQuery.setTown(null);
//		SearchQuery.setMaxResults("10");
//		SearchQuery.setUniversal("1");
		
		System.out.println("Request 1: GET / "+SearchQuery.toString()+"\n");
		jid = request.sendSearchRequest(SearchQuery);
		System.out.println("Job id to retireve (jid): "+jid+"\n");
		
		// wait for the job to finish
		try 
		{
		    Thread.sleep(30000);
		} 
		catch(InterruptedException ex) 
		{
		    Thread.currentThread().interrupt();
		}
		
		System.out.println("Request 2: GET/ http://v2.api.analyticsseo.com/get_job/"+jid+"\n");
		System.out.println("JSON fomatted response: "+request.getJobResult(jid));
	}
	
}
