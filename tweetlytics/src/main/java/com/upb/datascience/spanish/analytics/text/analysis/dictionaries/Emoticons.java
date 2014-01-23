package com.upb.datascience.spanish.analytics.text.analysis.dictionaries;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Emoticons implements Dictionary {

	private Map<String, String> emoticons;

	public Emoticons() {
		emoticons = new HashMap<String, String>();
	}

	public Emoticons(Map<String, String> emoticons) {
		this();
		this.emoticons = emoticons;
	}

	private void loadDefaultEmoticonsMap() {
		try {
			InputStream fstream = ClassLoader.getSystemResourceAsStream("dictionaries/emoticons.csv");

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			// Read File Line By Line (each line a word)
			while ((strLine = br.readLine()) != null) {
				// Emoticons separated of actual meaning by \t
				String[] strLinePieces = strLine.split("\t");

				if (strLinePieces.length == 2) {
					// Emoticons separated by space
					String[] emoticonsArray = strLinePieces[0].split(" ");

					for (int i = 0; i < emoticonsArray.length; i++) {
						emoticons.put(emoticonsArray[i], strLinePieces[1]);
					}
				}
			}

			br.close();
			in.close();
		} catch (IOException e) {
			System.out
					.println("Error trying to load default emoticons file emoticons.csv "
							+ e.getMessage());
		}
	}

	public boolean contains(String emoticon) {
		if (emoticons.isEmpty()) {
			loadDefaultEmoticonsMap();
		}

		if (emoticons.containsKey(emoticon)) {
			return true;
		}

		return false;
	}

	public String getValue(String word) {
		if (emoticons.isEmpty()) {
			loadDefaultEmoticonsMap();
		}

		return emoticons.get(word);
	}
}
