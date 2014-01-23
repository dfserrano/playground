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

public class PrintVocabularyFile {

	public static void main(String[] args) {
		Directory directory = new RAMDirectory();
		List<PolarTweet> tweets = new ArrayList<PolarTweet>();
		String indexAttribute = "tweet";
		TweetAnalyzer analyzer = new TweetAnalyzer();

		for (int perc = 5; perc <= 5; perc++) {
			directory = new RAMDirectory();
			tweets = new ArrayList<PolarTweet>();
			TweetVocabulary vocabulary;

			tweets = TwitterData.getPolarTweetsFromFile(
					"datasets/sentiment140-train.csv", perc);
			TwitterData.writeIndex(directory, tweets, analyzer, indexAttribute);

			try {
				FileWriter fstream = new FileWriter("voc" + perc + "p.txt");
				BufferedWriter out = new BufferedWriter(fstream);

				IndexReader reader = LuceneUtils.getReader(directory);
				vocabulary = new TweetVocabulary(reader, indexAttribute);

				out.write(vocabulary.toString());
				
				// Close the output stream
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
