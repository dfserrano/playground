package com.upb.datascience.spanish.analytics.text.analysis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import weka.core.Debug.Random;

public class TestIndexing {

	private Directory directory;
	private List<PolarTweet> tweets;

	public void createIndex() {

		loadTweets("datasets/sentiment-short.csv", 100);

		directory = new RAMDirectory();

		try {
			IndexWriter writer = getWriter();
			for (int i = 0; i < tweets.size(); i++) {
				Document doc = new Document();
				doc.add(new Field("tweet", tweets.get(i).getText(),
						Field.Store.YES, Field.Index.ANALYZED, TermVector.YES));
				writer.addDocument(doc);
			}

			System.out.println("Docs: " + writer.numDocs());
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private IndexReader getReader() throws IOException {
		return IndexReader.open(directory);
	}

	private IndexWriter getWriter() throws IOException {

		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,
				new TweetAnalyzer());
		return new IndexWriter(directory, conf);
	}

	private void loadTweets(String path, int samplePercentage) {
		try {
			tweets = new ArrayList<PolarTweet>();

			InputStream fstream = ClassLoader.getSystemResourceAsStream(path);
			Random r = new Random(1);

			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			// Read File Line By Line (each line a word)
			while ((strLine = br.readLine()) != null) {
				if (r.nextInt(100) <= samplePercentage) {
					String[] tweetPieces = strLine.split(";");
					int polarity;

					if (tweetPieces.length == 2) {
						if (tweetPieces[1].equals("positive")) {
							polarity = 3;
						} else if (tweetPieces[1].equals("neutral")) {
							polarity = 2;
						} else if (tweetPieces[1].equals("negative")) {
							polarity = 1;
						} else {
							continue;
						}

						tweets.add(new PolarTweet(tweetPieces[0], polarity));
					}
				}
			}

			System.out.println("Num tweets: " + tweets.size());

			br.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestIndexing t = new TestIndexing();
		t.createIndex();

		try {
			IndexReader reader = t.getReader();

			TweetVocabulary vocabulary = new TweetVocabulary(reader, "tweet");
			System.out.println(vocabulary);//

			for (int i = 0; i < reader.maxDoc(); i++) {
				if (reader.isDeleted(i)) {
					continue;
				}
				
				TermFreqVector tfv = reader.getTermFreqVector(i, "tweet");
				System.out.println(tfv);//
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
