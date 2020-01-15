package com.scalable.capital.main;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

import com.scalable.capital.logic.HTMLSearchService;
import com.scalable.capital.logic.JSLibraryService;
import com.scalable.capital.logic.WebRequestManager;
/*
 * This is the driver class which receives data from console
 * Calls Web Request Manager to google search
 * Calls Http Search Service to Identifies weblinks
 * Calls JS Library Service to print the top X libraries 
 */
public class WebCrawlerMain {
	private static final Integer topX_JSLibraries = 5;
	private static final Integer topX_GoogleSearchResults = 20;

	public static void main(String[] args) {
		
			System.out.print("Enter the String to be Google searched: ");
			String query = new Scanner(System.in).useDelimiter("\n").next();
			WebRequestManager webRequestManager = new WebRequestManager();
			JSLibraryService jsLibraryService = new JSLibraryService(webRequestManager);
			HTMLSearchService htmlSearchService = new HTMLSearchService();			
			try {
				System.out.println("Searching...");
				String document = webRequestManager.performGoogleSearch(query, topX_GoogleSearchResults);
				HashSet<String> webLinkSet = htmlSearchService.findSearchResultDocument(document);
				jsLibraryService.loadSearchedDoc(webLinkSet);
		        System.out.print("");
		        jsLibraryService.getTopXLibraries(topX_JSLibraries);			
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
	}
	
	
}
