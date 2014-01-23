package com.upb.datascience.spanish.analytics.text.analysis.dictionaries;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Negations implements Dictionary {

	private final String defaultValue = "NOT";
	private Set<String> negations;

	public Negations() {
		negations = new HashSet<String>();
	}

	public Negations(Collection<String> words) {
		this();
		negations.addAll(words);
	}

	private void loadDefaultNegationsList() {
		try {
			InputStream fstream = ClassLoader.getSystemResourceAsStream("dictionaries/negations.txt");

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String word;

			// Read File Line By Line (each line a word)
			while ((word = br.readLine()) != null) {
				// Each line, a negation
				negations.add(word);
			}

			br.close();
			in.close();
		} catch (IOException e) {
			System.out
					.println("Error trying to load default negations file negations.txt "
							+ e.getMessage());
		}
	}

	public boolean contains(String word) {
		if (negations.isEmpty()) {
			loadDefaultNegationsList();
		}

		if (negations.contains(word)) {
			return true;
		}

		return false;
	}

	public String getValue(String word) {
		if (negations.isEmpty()) {
			loadDefaultNegationsList();
		}

		if (contains(word)) {
			return defaultValue;
		}
		
		return null;
	}
}
