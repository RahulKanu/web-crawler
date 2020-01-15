package com.scalable.capital.logic;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLSearchService {

	private static final String HTML_TAG_PATTERN = "(?i)<a([^>]+)>(.+?)</a>";
	private static final String HTML_HREF_TAG_PATTERN = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))";
	private static final String DOMAIN_NAME_PATTERN = "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}";
	private Matcher matcherAnchor;
	private Matcher matcherHref;
	private Matcher matcherDomain;
	private Pattern patternAnchor;
	private Pattern patternHref;
	private Pattern patternDomain;

	public HTMLSearchService() {
		patternAnchor = Pattern.compile(HTML_TAG_PATTERN);
		patternHref = Pattern.compile(HTML_HREF_TAG_PATTERN);
		patternDomain = Pattern.compile(DOMAIN_NAME_PATTERN);
	}

	/*
	 * Based on the google html result page, the function identifies the weblinks and pushes it to a Set
	 * If the below commented lines are uncommented, we should be able to see the weblinks pushed in the set
	 */
	public HashSet<String> findSearchResultDocument(final String sourceHtml) throws UnsupportedEncodingException {
		HashSet<String> result = new HashSet<String>();
		matcherAnchor = patternAnchor.matcher(sourceHtml);

		while (matcherAnchor.find()) {
			String href = matcherAnchor.group(1);
			String domainName = "";
			matcherHref = patternHref.matcher(href);
			while (matcherHref.find()) {
				String link = matcherHref.group(1);
				if (link.startsWith("\"/url?q=")) {
					link = URLDecoder.decode(link, "UTF-8");
					matcherDomain = patternDomain.matcher(link);
					if (matcherDomain.find()) {
						domainName = matcherDomain.group(0).toLowerCase().trim();
					}
					link = link.split("&")[0];
					link = link.replaceAll("/url\\?q=", "").replaceAll("\"", "");
					result.add(link);
				}
			}

		}
		// System.out.println(result.size());
		// result.forEach(x -> System.out.println(x));
		return result;

	}
}
