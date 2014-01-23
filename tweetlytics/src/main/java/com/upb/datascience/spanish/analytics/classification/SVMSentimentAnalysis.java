package com.upb.datascience.spanish.analytics.classification;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import libsvm.svm;
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

public class SVMSentimentAnalysis {

	svm_problem problem;
	svm_model model;

	Directory directory = new RAMDirectory();
	List<PolarTweet> tweets = new ArrayList<PolarTweet>();
	String indexAttribute = "tweet";
	TweetAnalyzer analyzer = new TweetAnalyzer();
	TweetVocabulary vocabulary;

	public SVMSentimentAnalysis(String path, int samplePercentage) {
		tweets = TwitterData.getPolarTweetsFromFile(path, samplePercentage);
		TwitterData.writeIndex(directory, tweets, analyzer, indexAttribute);
	}

	public SVMSentimentAnalysis(String modelPath, String vocabularyPath)
			throws IOException {
		model = svm.svm_load_model(modelPath);
		vocabulary = new TweetVocabulary(vocabularyPath);
	}

	public void train() {
		// default parameters
		train(8, 0.0125, true);
	}

	public void train(double C, double gamma, boolean forceBuild) {

		// Set svm parameters
		svm_parameter parameters = SVM.getDefaultParametersForClassification();
		parameters.C = C;
		parameters.gamma = gamma;

		// Create problem
		buildProblem(forceBuild);

		// Training
		model = SVM.train(problem, parameters);

	}

	public double getAccuracyFromCrossValidation(int kfold, double C,
			double gamma, boolean forceBuild) {

		// Set svm parameters
		svm_parameter parameters = SVM.getDefaultParametersForClassification();
		parameters.C = C;
		parameters.gamma = gamma;

		// Create problem
		buildProblem(forceBuild);

		double accuracy = SVM.getAccuracyFromCrossvalidation(problem,
				parameters, kfold);
		return accuracy;

	}

	public double predict(String text) {
		if (model == null) {
			return -1;
		}

		svm_node[] vector = SVM.getNodeVectorFromString(text, vocabulary,
				analyzer);

		return SVM.predict(model, vector);
	}

	public int[][] predictFromFile(String path) throws IOException {
		if (model == null) {
			return null;
		}

		FileInputStream fstream = new FileInputStream(path);

		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;

		int[][] confusion = new int[3][3];
		int total = 0;
		int accurate = 0;

		// Read File Line By Line (each line a word)
		while ((strLine = br.readLine()) != null) {
			String[] pieces = strLine.split("\t");
			String text = pieces[0];
			int polarityExpected = -1;

			if (pieces[1].equals("positive") || pieces[1].equals("3")) {
				polarityExpected = 3;
			} else if (pieces[1].equals("neutral") || pieces[1].equals("2")) {
				polarityExpected = 2;
			} else if (pieces[1].equals("negative") || pieces[1].equals("1")) {
				polarityExpected = 1;
			} else {
				continue;
			}

			svm_node[] vector = SVM.getNodeVectorFromString(text, vocabulary,
					analyzer);

			double result = SVM.predict(model, vector);

			if (polarityExpected == result) {
				accurate++;
			}
			total++;
			confusion[polarityExpected - 1][(int) result - 1]++;
		}
		
		br.close();

		System.out.println("Accuracy = " + ((accurate*100)/total) + "%");
		System.out.println("Confusion Matrix");
		for (int i = 0; i < confusion.length; i++) {
			for (int j = 0; j < confusion.length; j++) {
				System.out.print(confusion[i][j] + "\t");
			}
			System.out.println();
		}

		return confusion;
	}

	private void buildProblem(boolean forceBuild) {
		if (!forceBuild && problem != null) {
			return;
		}

		try {
			IndexReader reader = LuceneUtils.getReader(directory);
			vocabulary = new TweetVocabulary(reader, indexAttribute);

			int trainingSize = LuceneUtils.getEffectiveNumberOfDocuments(
					reader, indexAttribute);

			problem = SVM.initializeClassificationProblem(trainingSize);

			int docCount = 0;
			for (int i = 0; i < reader.maxDoc(); i++) {
				if (reader.isDeleted(i)) {
					continue;
				}

				TermFreqVector tfv = reader.getTermFreqVector(i, "tweet");

				if (tfv == null) {
					continue;
				}

				SVM.addTermVectorToProblem(problem, docCount, tfv, tweets
						.get(i).getPolarity(), vocabulary);

				docCount++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save(String modelPath, String vocabularyPath) throws Exception {
		if (model == null) {
			throw new Exception("Model does not exist yet");
		}

		if (vocabulary == null) {
			throw new Exception("Vocabulary does not exist yet");
		}

		svm.svm_save_model(modelPath, model);
		vocabulary.saveToFile(vocabularyPath);
	}
}
