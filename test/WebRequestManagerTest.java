package test;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import com.scalable.capital.logic.WebRequestManager;

public class WebRequestManagerTest {

	WebRequestManager webRequestManager;
	@Before
	public void init() {
		webRequestManager = new WebRequestManager();
	}
	@Test
	public void testFormURL() throws MalformedURLException {
		URL urlTest = new URL("https://www.google.com/search?q=hello&num=5");
		assertEquals(webRequestManager.formURL("https://www.google.com/search?q=hello&num=5").toString(), urlTest.toString());;
	}
	
	@Test
	public void testGetDocument() throws MalformedURLException {
		URL urlTest = new URL("https://www.google.com/search?q=hello&num=5");
		assertTrue(webRequestManager.getDocument(urlTest).length() > 0);
	}
	
	@Test
	public void testPerformGoogleSearch() throws MalformedURLException {
		assertTrue(webRequestManager.performGoogleSearch("hello", 5).length() > 0);
	}

}
