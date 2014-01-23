package com.upb.datascience.spanish.analytics.text.analysis.dictionaries;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Stopwords implements Dictionary {

	private Set<String> stopwords;
	private String file = "dictionaries/stopwords.txt";

	public Stopwords() {
		stopwords = new HashSet<String>();
	}

	public Stopwords(Collection<String> words) {
		this();
		stopwords.addAll(words);
	}
	
	public Stopwords(String file) {
		this();
		this.file = file;
	}

	private void loadDefaultStopwordsList() {
		try {
			InputStream fstream = ClassLoader.getSystemResourceAsStream(file);

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String word;

			// Read File Line By Line (each line a word)
			while ((word = br.readLine()) != null) {
				// Each line, a stopword
				stopwords.add(word);
			}

			br.close();
			in.close();
		} catch (IOException e) {
			System.out
					.println("Error trying to load default stopwords file stopwords.txt "
							+ e.getMessage());
		}
	}

	public boolean contains(String word) {
		if (stopwords.isEmpty()) {
			loadDefaultStopwordsList();
		}

		if (stopwords.contains(word)) {
			return true;
		}

		return false;
	}

	public String getValue(String word) {
		if (stopwords.isEmpty()) {
			loadDefaultStopwordsList();
		}

		if (contains(word)) {
			return word;
		}
		
		return null;
	}
	
	public Set<String> getStopwords() {
		if (stopwords.isEmpty()) {
			loadDefaultStopwordsList();
		}
		
		return this.stopwords;
	}

}
