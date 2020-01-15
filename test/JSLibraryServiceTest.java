package test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import com.scalable.capital.logic.JSLibraryService;
import com.scalable.capital.logic.WebRequestManager;

public class JSLibraryServiceTest {
	JSLibraryService jsLibraryService;
	WebRequestManager webRequestManager;
	@Before
	public void init() {
		webRequestManager = new WebRequestManager();
		jsLibraryService = new JSLibraryService(webRequestManager);
	}
	@Test
	public void testLibraryJSFileLoad() {
		HashSet<String> searchUrlSet= new HashSet<>();
		searchUrlSet.add("https://www.merriam-webster.com/dictionary/hello");
		jsLibraryService.loadSearchedDoc(searchUrlSet);
		assertEquals(jsLibraryService.getLibraryMap().size(), 6);
	}
	
	@Test
	public void testSortLibraryMapByValue() {
		HashMap<String, Integer> urlCountMap= new HashMap<>();
		urlCountMap.put("a", 1);
		urlCountMap.put("b", 10);
		urlCountMap.put("c", 2);
		urlCountMap.put("d", 5);
		urlCountMap.put("e", 6);
		urlCountMap.put("f", 4);
		jsLibraryService.sortLibraryMapByValue(urlCountMap).entrySet().stream().limit(1).forEach(library -> {
			assertEquals(library.getKey(), "b");
		});
		
	}

}
