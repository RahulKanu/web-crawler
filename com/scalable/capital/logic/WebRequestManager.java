package com.scalable.capital.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class WebRequestManager {

	/*
	 * This function would fetch document by making an httpConnection and would return this document as string
	 */
	public String getDocument(URL url) {
		String document = "";
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.addRequestProperty("User-Agent",
					"Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");

			BufferedReader br = new BufferedReader(
					new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

			while (br.readLine() != null) {
				document = document + br.readLine();
			}
			br.close();
		} catch (IOException ioe) {
			System.out.println("IOException");
		} catch(Exception e) {
			System.out.print("Exception");
		}
		return document;
	}
	
	/*
	 * The function is used to create an URL object
	 */
	public URL formURL(String urlString) {
		try {
			return new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * This function would replace all the spaces entered on console with %20 and create a google search url 
	 */
	public String performGoogleSearch(String query, Integer topX_Libraries) {
		 query = query.replaceAll(" ", "%20");
		 return getDocument(formURL("https://www.google.com/search?q=" + query + "&num="+topX_Libraries));
	}
}
