package com.scalable.capital.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.scalable.capital.thread.Worker;

public class JSLibraryService {

	ExecutorService executor;
	private static final String SCRIPT_SRC_PATTERN = "<script(?:[^>]*src=['\"]([^'\"]*)['\"][^>]*>|[^>]*>([^<]*)</script>)";
	private static final Integer NO_OF_WORKER_THREADS = 10;
	private Matcher matcherScriptSrc;
	private Pattern patternScriptSrc;
	private HashMap<String, Integer> libraryMap = new HashMap<>();
	private WebRequestManager webRequestManager;

	/*
	 * Creates a new Executor Service Threadpool
	 */
	public JSLibraryService(WebRequestManager webRequestManager) {
		executor = Executors.newFixedThreadPool(NO_OF_WORKER_THREADS);
		this.webRequestManager = webRequestManager;
		patternScriptSrc = Pattern.compile(SCRIPT_SRC_PATTERN, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	}
	
	/*
	 * Based on the Weblinks identified by google search, this function
	 * will pass the weblinks to the worker thread which would in turn fetch the document
	 * Once the document are fetched, the function would create a libraryMap of all the
	 * js file along with number of times the js files was used across all the weblinks
	 */

	public void loadSearchedDoc(HashSet<String> webLinkSet) {
		List<Future<String>> documentsFuture = new ArrayList<Future<String>>();
		webLinkSet.forEach(webLink -> {
			Callable<String> worker = new Worker(webLink, this.webRequestManager);
			Future<String> future = executor.submit(worker);
			documentsFuture.add(future);
		});
		for (Future<String> documentFuture : documentsFuture) {
			try {
				String scriptDoc = documentFuture.get();
				matcherScriptSrc = patternScriptSrc.matcher(scriptDoc);
				while (matcherScriptSrc.find()) {
					if (matcherScriptSrc.group(1) != null) {
						String library = matcherScriptSrc.group(1).split("\\?")[0];
						if (libraryMap.get(library) != null) {
							Integer count = libraryMap.get(library) + 1;
							libraryMap.put(library, count);
						} else {
							libraryMap.put(library, 1);
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("Finished all threads");
	}

	/*
	 * The function would sort the library map based on count
	 */
	public Map<String, Integer> sortLibraryMapByValue(Map<String, Integer> unsortMap) {

		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	/*
	 * This function will print the top X libraries on console
	 */
	public void getTopXLibraries(Integer topxLibraries) {
		System.out.println("The top " + topxLibraries + " libraries are as below: ");
		Map<String, Integer> sortedMap = sortLibraryMapByValue(libraryMap);
		sortedMap.entrySet().stream().limit(topxLibraries).forEach((library) -> {
			System.out.println("Library Name: " + library.getKey() + " Count: " + library.getValue());
		});
	}

	public HashMap<String, Integer> getLibraryMap() {
		return libraryMap;
	}

	public void setLibraryMap(HashMap<String, Integer> libraryMap) {
		this.libraryMap = libraryMap;
	}

}
