package com.scalable.capital.thread;

import java.util.concurrent.Callable;

import com.scalable.capital.logic.WebRequestManager;

public class Worker implements Callable<String> {

	private String webLink;

	private WebRequestManager webRequestManager;

	public Worker(String webLink, WebRequestManager webRequestManager) {
		this.webLink = webLink;
		this.webRequestManager = webRequestManager;
	}

	@Override
	public String call() {
		return callWebLinks();
	}

	private String callWebLinks() {
		System.out.println("Executing; "+ Thread.currentThread().getName());
		return webRequestManager.getDocument(webRequestManager.formURL(webLink));
	}

	@Override
	public String toString() {
		return this.webLink;
	}

}
