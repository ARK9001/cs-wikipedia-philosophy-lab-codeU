package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
        // some example code to get you started

		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		List<String> visitedURL = new ArrayList<String>();
		
		String nextURL = "";
		int leftPar = 0;
		int rightPar = 0;
		Boolean nItalicized = false;

		while(true){
			visitedURL.add(url);
			Elements paragraphs = wf.fetchWikipedia(url);
			for (Element para: paragraphs){
				Iterable<Node> iter = new WikiNodeIterable(para);
				for (Node n: iter){

					Node childPar = n.parent();
					while (childPar != null && !nItalicized){
						if (((Element) childPar).tagName().equals("i")){
							if (childPar != null){
								nItalicized = true;
							}
							else {
								childPar = childPar.parent();
							}
						}
						else{
							childPar = childPar.parent();
						}
					}

					if (n instanceof TextNode) {
						String read = ((TextNode) n).text();
						for (int i = 0; i < read.length(); i++) {
							if (read.charAt(i) == '(') {
								leftPar++;
							} else if (read.charAt(i) == ')') {
								rightPar++;
							}
						}
					}

					if (n instanceof Element){
						if (((Element) n).tagName().equals("a")){
							if (leftPar <= rightPar){
								if (!nItalicized){
									String addLink = ((Element) n).attr("href");
									if (addLink.length() > 6 && addLink.substring(0,6).equals("/wiki/")){
										nextURL = "https://en.wikipedia.org" + addLink;
										url = nextURL;
									}
								}
							}
						}
					}

				}	
			}


			if (url.equals("https://en.wikipedia.org/wiki/Philosophy")) {
				int clicks = visitedURL.size()+1;
				System.out.println("We have reached the Philosophy page in" + clicks + " clicks!");
				return;
			}
			if (url == null || visitedURL.contains(url)) {
				System.out.println("We cannot reach the Philosophy page");
				return;
			}

		}


	/**	Elements paragraphs = wf.fetchWikipedia(url);

		Element firstPara = paragraphs.get(0);
		
		Iterable<Node> iter = new WikiNodeIterable(firstPara);
		for (Node node: iter) {
			if (node instanceof TextNode) {
				System.out.print(node);
			}
        }**/

        // the following throws an exception so the test fails
        // until you update the code
        //String msg = "Complete this lab by adding your code and removing this statement.";
        //throw new UnsupportedOperationException(msg);
	}
}
