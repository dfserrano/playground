package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;

public class TweetVocabulary {

	private Map<String, int[]> vocabulary;

	public TweetVocabulary(IndexReader reader, String attribute) {

		vocabulary = new HashMap<String, int[]>();
		try {
			TermEnum termEnum = reader.terms();

			int counter = 0;
			while (termEnum.next()) {
				addTerm(termEnum.term().text(), ++counter, 0);
			}

			for (int i = 0; i < reader.maxDoc(); i++) {
				if (reader.isDeleted(i)) {
					continue;
				}

				TermFreqVector tfv = reader.getTermFreqVector(i, attribute);

				if (tfv == null) {
					continue;
				}

				String[] terms = tfv.getTerms();
				int[] termFreqs = tfv.getTermFrequencies();

				for (int j = 0; j < terms.length; j++) {
					addTerm(terms[j], -1, termFreqs[j]);
				}
			}

			// this.removeLongTail(11);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public TweetVocabulary(String path) throws IOException {
		vocabulary = new HashMap<String, int[]>();
		
		FileInputStream fstream = new FileInputStream(path);

		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		// Read File Line By Line (each line a word)
		while ((strLine = br.readLine()) != null) {
			String[] pieces = strLine.split("\t");

			int[] termInfo = new int[2];
			termInfo[0] = Integer.parseInt(pieces[1]);
			termInfo[1] = Integer.parseInt(pieces[2]);
			
			vocabulary.put(pieces[0], termInfo);
		}
		
		br.close();
	}

	public void addTerm(String term, int index, int totalFreq) {

		// [0] numeric index
		// [1] global frequency
		int[] idxFreq = new int[2];

		if (vocabulary.containsKey(term)) {
			idxFreq = vocabulary.get(term);
		}

		if (index != -1) {
			idxFreq[0] = index;
		}

		idxFreq[1] += totalFreq;

		vocabulary.put(term, idxFreq);
	}

	public int getNumericIndex(String term) {
		int[] termInfo = getTermInfo(term);

		if (termInfo != null) {
			return termInfo[0];
		}

		return -1;
	}

	public void setNumericIndex(String term, int index) {
		int[] termInfo = getTermInfo(term);

		if (termInfo != null) {
			termInfo[0] = index;
			vocabulary.put(term, termInfo);
		}
	}

	public int getTotalFrequency(String term) {
		int[] termInfo = getTermInfo(term);

		if (termInfo != null) {
			return termInfo[1];
		}

		return -1;
	}

	public int[] getTermInfo(String term) {
		if (vocabulary.containsKey(term)) {
			int[] idxFreq = vocabulary.get(term);

			return idxFreq;
		} 
		
		return null;
	}

	public void removeTerm(String term) {
		vocabulary.remove(term);
	}

	public void clear() {
		vocabulary.clear();
	}

	public int size() {
		return vocabulary.size();
	}

	public String toString() {
		String s = "";
		for (String term : vocabulary.keySet()) {
			int[] termInfo = getTermInfo(term);
			s += term + "\t" + termInfo[0] + "\t" + termInfo[1] + "\n";
		}

		return s;
	}

	public void saveToFile(String path) throws IOException {
		FileWriter fstream = new FileWriter(path);
		BufferedWriter out = new BufferedWriter(fstream);

		out.write(this.toString());

		out.close();
	}
}
