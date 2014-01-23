package com.upb.datascience.spanish.analytics.text.analysis.dictionaries;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Acronyms implements Dictionary {

	private Map<String, String> acronyms;

	public Acronyms() {
		acronyms = new HashMap<String, String>();
	}

	public Acronyms(Map<String, String> acronyms) {
		this();
		this.acronyms = acronyms;
	}

	public void loadDefaultAcronymsMap() {
		try {
			InputStream fstream = ClassLoader.getSystemResourceAsStream("dictionaries/acronyms.csv");

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			// Read File Line By Line (each line a word)
			while ((strLine = br.readLine()) != null) {
				// Acronym separated of actual meaning by \t
				String[] strLinePieces = strLine.split("\t");

				if (strLinePieces.length == 2) {
					acronyms.put(strLinePieces[0],
							strLinePieces[1].toLowerCase());
				}
			}

			br.close();
			in.close();

		} catch (IOException e) {
			System.out
					.println("Error trying to load default acronyms file acronyms.csv "
							+ e.getMessage());
		}

	}

	public boolean contains(String acronym) {

		if (acronyms.isEmpty()) {
			loadDefaultAcronymsMap();
		}
		
		if (acronyms.containsKey(acronym)) {
			return true;
		}
		return false;
	}

	public String getValue(String acronym) {

		if (acronyms.isEmpty()) {
			loadDefaultAcronymsMap();
		}

		return acronyms.get(acronym);
	}
}
