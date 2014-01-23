package com.upb.datascience.spanish.analytics.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import com.upb.datascience.spanish.analytics.text.LuceneUtils;
import com.upb.datascience.spanish.analytics.text.TwitterData;
import com.upb.datascience.spanish.analytics.text.analysis.PolarTweet;
import com.upb.datascience.spanish.analytics.text.analysis.TweetAnalyzer;
import com.upb.datascience.spanish.analytics.text.analysis.TweetVocabulary;

public class PrintLibSVMFile {

	public static void main(String[] args) {
		Directory directory = new RAMDirectory();
		List<PolarTweet> tweets = new ArrayList<PolarTweet>();
		String indexAttribute = "tweet";
		TweetAnalyzer analyzer = new TweetAnalyzer();

		// for (int perc = 2; perc <= 10; perc++) {
		int perc = 100;
		directory = new RAMDirectory();
		tweets = new ArrayList<PolarTweet>();
		TweetVocabulary vocabulary;

		tweets = TwitterData.getPolarTweetsFromFile(
				"datasets/merged-balanced.csv", perc);
		TwitterData.writeIndex(directory, tweets, analyzer, indexAttribute);

		try {
			FileWriter fstream = new FileWriter("out" + perc + "p.txt");
			BufferedWriter out = new BufferedWriter(fstream);

			IndexReader reader = LuceneUtils.getReader(directory);
			vocabulary = new TweetVocabulary(reader, indexAttribute);

			for (int i = 0; i < reader.maxDoc(); i++) {
				if (reader.isDeleted(i)) {
					continue;
				}

				TermFreqVector tfv = reader.getTermFreqVector(i, "tweet");

				if (tfv == null) {
					continue;
				}

				String[] terms = tfv.getTerms();
				int[] termFreqs = tfv.getTermFrequencies();

				String line = tweets.get(i).getPolarity() + " ";

				boolean show = false;

				for (int j = 0; j < terms.length; j++) {
					if (vocabulary.getNumericIndex(terms[j]) != -1) {
						line += vocabulary.getNumericIndex(terms[j]) + ":"
								+ termFreqs[j] + " ";
						show = true;
					}
				}

				line += "\n";

				if (show) {
					out.write(line);
				}
			}

			// Close the output stream
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// }
	}
}
