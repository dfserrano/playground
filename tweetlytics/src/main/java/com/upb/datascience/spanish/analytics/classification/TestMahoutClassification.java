package com.upb.datascience.spanish.analytics.classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.Dictionary;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

import com.upb.datascience.spanish.analytics.text.LuceneUtils;
import com.upb.datascience.spanish.analytics.text.TwitterData;
import com.upb.datascience.spanish.analytics.text.analysis.PolarTweet;
import com.upb.datascience.spanish.analytics.text.analysis.TweetAnalyzer;
import com.upb.datascience.spanish.analytics.text.analysis.TweetVocabulary;

public class TestMahoutClassification {

	public static void main(String[] args) {

		Map<String, Set<Integer>> traceDictionary = new TreeMap<String, Set<Integer>>();
		FeatureVectorEncoder encoder = new StaticWordValueEncoder("body");
		encoder.setProbes(2);
		encoder.setTraceDictionary(traceDictionary);
		
		Directory directory = new RAMDirectory();
		List<PolarTweet> tweets = new ArrayList<PolarTweet>();
		String indexAttribute = "tweet";
		TweetAnalyzer analyzer = new TweetAnalyzer();
		
		tweets = TwitterData.getPolarTweetsFromFile("datasets/elections2008.csv", 100);
		TwitterData.writeIndex(directory, tweets, analyzer, indexAttribute);
		
		Collections.shuffle(tweets);

		try {
			IndexReader reader = LuceneUtils.getReader(directory);

			TweetVocabulary vocabulary = new TweetVocabulary(reader, "tweet");
			int features = vocabulary.size();
			System.out.printf("%d Features\n", features);
			System.out.printf("%d Training files\n", reader.numDocs());

			Dictionary polarities = new Dictionary();

			@SuppressWarnings("resource")
			OnlineLogisticRegression learningAlgorithm = new OnlineLogisticRegression(
					4, features, new L1()).alpha(1).stepOffset(1000)
					.decayExponent(0.9).lambda(3.0e-5).learningRate(20);
			double totalCorrect = 0.0;
			
			for (int i = 0; i < reader.maxDoc(); i++) {
				if (reader.isDeleted(i)) {
					continue;
				}
				
				int actual = polarities.intern(PolarTweet.getPolarityLabel(tweets.get(i).getPolarity()));

				TermFreqVector tfv = reader.getTermFreqVector(i, "tweet");
				
				if (tfv == null) continue;
				
				String[] terms = tfv.getTerms();
				int[] termFreqs = tfv.getTermFrequencies();

				Vector v = new RandomAccessSparseVector(features);

				for (int j = 0; j < terms.length; j++) {
					encoder.addToVector(terms[j], termFreqs[j], v);
				}
				
				if (i < reader.maxDoc()-20) {
					learningAlgorithm.train(actual, v);
				} else {
					System.out.println(tweets.get(i).getPolarity());
					
					Vector p = new DenseVector(20);
					learningAlgorithm.classifyFull(p, v);
					int estimated = p.maxValueIndex();
					System.out.println("Predicted = " + estimated);

					int correct = (estimated == actual? 1 : 0);
					totalCorrect += correct;
				}
			}
			
			System.out.println("Total correct = " + totalCorrect);
			for (String pol : polarities.values()) {
				System.out.println(pol + " : " + polarities.intern(pol));
			}
			
			learningAlgorithm.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
