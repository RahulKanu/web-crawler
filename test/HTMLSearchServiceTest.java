package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.scalable.capital.logic.HTMLSearchService;

public class HTMLSearchServiceTest {

	private HashSet<String> testSet;
	HTMLSearchService htmlSearchService;

	@Before
	public void init() {
		htmlSearchService = new HTMLSearchService();

	}

	@Test
	public void testForGoogleSearchLink() throws UnsupportedEncodingException {
		String test = "<a href=\"/url?q=http://helloworld.com\">Take me to StackOverflow</a>";
		testSet = htmlSearchService.findSearchResultDocument(test);
		assertNotNull(testSet);
		assertEquals(testSet.size(), 1);
	}

	@Test
	public void testForGoogleSearchInvalidLink() throws UnsupportedEncodingException {
		String test = "<a href=\"http://helloworld.com\">Take me to StackOverflow</a>";
		testSet = htmlSearchService.findSearchResultDocument(test);
		assertNotNull(testSet);
		assertEquals(testSet.size(), 0);
	}

}