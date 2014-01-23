package com.upb.datascience.spanish.analytics.text;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;

import weka.core.Debug.Random;

import com.upb.datascience.spanish.analytics.text.analysis.PolarTweet;

public class TwitterData {

	private static Log log = LogFactory.getLog(TwitterData.class);
	private static final String separator = "\t";

	/**
	 * Gets a list of polar tweets (with associated sentiment) from the
	 * specified file. The format of the file should be [tweet_text];[polarity]
	 * for each line. Tweets with text containing semicolons are ignored
	 * (advice: strip all semicolons in the text of the tweet). The method can
	 * handle sampling over the file of tweets.
	 * 
	 * @param path
	 *            Path of the file
	 * @param samplePercentage
	 *            Percentage used in the sample. If a sample is not needed, then
	 *            set it to 100.
	 * @return List of polar tweets
	 */
	public static List<PolarTweet> getPolarTweetsFromFile(String path,
			int samplePercentage) {

		log.info("Getting sample (" + samplePercentage
				+ "%) of polar tweets  from " + path);

		List<PolarTweet> tweets = new ArrayList<PolarTweet>();

		try {
			InputStream fstream = ClassLoader.getSystemResourceAsStream(path);

			// Random generator for sampling
			Random r = new Random(System.currentTimeMillis());

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			// Read File Line By Line (each line a word)
			while ((strLine = br.readLine()) != null) {
				if (r.nextInt(100) <= samplePercentage) {
					String[] tweetPieces = strLine.split(separator);
					int polarity;

					if (tweetPieces.length == 2) {
						if (tweetPieces[1].equals("positive")
								|| tweetPieces[1].equals("3")) {
							polarity = 3;
						} else if (tweetPieces[1].equals("neutral")
								|| tweetPieces[1].equals("2")) {
							polarity = 2;
						} else if (tweetPieces[1].equals("negative")
								|| tweetPieces[1].equals("1")) {
							polarity = 1;
						} else {
							continue;
						}

						tweets.add(new PolarTweet(tweetPieces[0], polarity));
					}
				}
			}

			log.info("Number of tweets loaded: " + tweets.size());

			fstream.close();
			br.close();
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return tweets;
	}

	/**
	 * Writes tweets to Lucene index.
	 * 
	 * @param directory
	 *            Lucene directory
	 * @param tweets
	 *            List of tweets
	 * @param analyzer
	 *            Analyzer to filter tweet's text
	 * @param attributeField
	 *            Attribute where tweet text is to be stored
	 */
	public static void writeIndex(Directory directory, List<PolarTweet> tweets,
			Analyzer analyzer, String attributeField) {
		log.info("Write " + tweets.size() + " tweets in index ("
				+ attributeField + ")");

		try {
			IndexWriter writer = LuceneUtils.getWriter(directory, analyzer);

			// Adds all tweets to index
			for (PolarTweet tweet : tweets) {
				Document doc = new Document();
				doc.add(new Field(attributeField, tweet.getText(),
						Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));
				writer.addDocument(doc);
			}

			log.info("Current number of tweets in index : " + writer.numDocs());

			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
