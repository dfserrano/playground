package com.upb.datascience.spanish.analytics.classification;

import java.util.ArrayList;
import java.util.List;

import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import com.upb.datascience.spanish.analytics.classification.algorithms.SVM;
import com.upb.datascience.spanish.analytics.text.LuceneUtils;
import com.upb.datascience.spanish.analytics.text.TwitterData;
import com.upb.datascience.spanish.analytics.text.analysis.PolarTweet;
import com.upb.datascience.spanish.analytics.text.analysis.TweetAnalyzer;
import com.upb.datascience.spanish.analytics.text.analysis.TweetVocabulary;

public class TestSVMClassification {

	public static void main(String[] args) {

		Directory directory = new RAMDirectory();
		List<PolarTweet> tweets = new ArrayList<PolarTweet>();
		String indexAttribute = "tweet";
		TweetAnalyzer analyzer = new TweetAnalyzer();
		
		tweets = TwitterData.getPolarTweetsFromFile("datasets/sentiment.csv", 100);
		TwitterData.writeIndex(directory, tweets, analyzer, indexAttribute);

		try {
			IndexReader reader = LuceneUtils.getReader(directory);
			
			TweetVocabulary vocabulary = new TweetVocabulary(reader,
					indexAttribute);

			int trainingSize = LuceneUtils.getEffectiveNumberOfDocuments(
					reader, indexAttribute);

			// sets svm params
			svm_parameter parameters = SVM
					.getDefaultParametersForClassification();
			parameters.C = 8;
			parameters.gamma = 0.0125;

			// build problem
			svm_problem prob = SVM
					.initializeClassificationProblem(trainingSize);

			int docCount = 0;
			for (int i = 0; i < reader.maxDoc(); i++) {
				if (reader.isDeleted(i)) {
					continue;
				}

				TermFreqVector tfv = reader.getTermFreqVector(i, "tweet");

				if (tfv == null) {
					continue;
				}

				SVM.addTermVectorToProblem(prob, docCount, tfv, tweets.get(i)
						.getPolarity(), vocabulary);

				docCount++;
			}

			// Cross validation
//			 double accuracy = SVM.getAccuracyFromCrossvalidation(prob,
//			 parameters, 10);
//			 System.out.println("Accuracy = " + accuracy);

			// Training
			svm_model model = SVM.train(prob, parameters);

			double countTrue = 0;
			double countFalse = 0;
			
			for (PolarTweet tweet : tweets) {
				svm_node[] vector = SVM.getNodeVectorFromString(tweet.getText(),
						vocabulary, analyzer);

				double d = SVM.predict(model, vector);

				if (d == tweet.getPolarity()) {
					countTrue++;
				} else { 
					countFalse++;
				}
			}
			
			System.out.println("# = " + tweets.size());
			System.out.println("TT = " + (countTrue/(countTrue+countFalse)));
			System.out.println("TF = " + (countFalse/(countTrue+countFalse)));
			System.out.println();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
