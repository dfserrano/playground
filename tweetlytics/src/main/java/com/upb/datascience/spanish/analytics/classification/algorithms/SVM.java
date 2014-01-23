package com.upb.datascience.spanish.analytics.classification.algorithms;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.TermFreqVector;

import com.upb.datascience.spanish.analytics.text.analysis.TweetVocabulary;

public class SVM {

	public static Log log = LogFactory.getLog(SVM.class);

	/**
	 * Get default parameters for Cost-Support Vector Classification, using
	 * Radial Basis Functions (RBF). Gamma = 0.5 and Cost = 1.
	 * 
	 * @return Default parameters for a C-SVC with RBF problem.
	 */
	public static svm_parameter getDefaultParametersForClassification() {
		svm_parameter param = new svm_parameter();

		// default values
		param.svm_type = svm_parameter.C_SVC;
		param.kernel_type = svm_parameter.RBF;
		param.degree = 3;
		// param.gamma = 0;
		param.coef0 = 0;
		param.nu = 0.5;
		param.cache_size = 40;
		param.C = 1;
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 1;
		param.probability = 0;
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];

		param.gamma = 0.5;

		return param;
	}

	/**
	 * Initialize classification problem using LibSVM, with an specified number
	 * of instances
	 * 
	 * @param numInstances
	 *            Number of instances of the training set
	 * @return Problem representation of LibSVM
	 */
	public static svm_problem initializeClassificationProblem(int numInstances) {
		log.info("Initialize classification problem with " + numInstances
				+ " instances");

		svm_problem problem = new svm_problem();
		problem.l = numInstances;
		problem.y = new double[numInstances];
		problem.x = new svm_node[numInstances][];

		return problem;
	}

	/**
	 * Add a Lucene term vector frequency to the problem in LibSVM.
	 * 
	 * @param problem
	 *            LibSVM problem
	 * @param curProblemIndex
	 *            Index in the array to save the instance
	 * @param vector
	 *            Lucene's term vector frequency
	 * @param klass
	 *            Actual class (for classification)
	 * @param vocabulary
	 *            Vocabulary of the problem
	 */
	public static void addTermVectorToProblem(svm_problem problem,
			int curProblemIndex, TermFreqVector vector, double klass,
			TweetVocabulary vocabulary) {
		// log.info("Add term vector " + curProblemIndex + " (" + klass
		// + ") to problem");

		String[] terms = vector.getTerms();
		int[] termFreqs = vector.getTermFrequencies();

		problem.y[curProblemIndex] = klass;
		problem.x[curProblemIndex] = new svm_node[terms.length];

		for (int j = 0; j < terms.length; j++) {
			if (vocabulary.getNumericIndex(terms[j]) != -1) {
				problem.x[curProblemIndex][j] = new svm_node();
				problem.x[curProblemIndex][j].index = vocabulary
						.getNumericIndex(terms[j]);
				problem.x[curProblemIndex][j].value = termFreqs[j];
			}
		}
	}

	/**
	 * Get accuracy from n-fold cross-validation. Accuracy = Positives / Total
	 * 
	 * @param problem
	 *            LibSVM problem
	 * @param parameters
	 *            Problem parameters
	 * @param kfold
	 *            Number of divisions in the data set.
	 * @return
	 */
	public static double getAccuracyFromCrossvalidation(svm_problem problem,
			svm_parameter parameters, int kfold) {
		log.info("Get accuracy for " + kfold + "-fold cross-validation");

		int[][] confusionMatrix = new int[3][3];

		double[] target = new double[problem.l];
		svm.svm_cross_validation(problem, parameters, kfold, target);

		double accuracy = 0;
		for (int i = 0; i < target.length; i++) {
			confusionMatrix[(int) problem.y[i] - 1][(int) target[i] - 1]++;
			if (target[i] == problem.y[i]) {
				accuracy++;
			}
		}

		System.out.println("Confusion Matrix");
		for (int i = 0; i < confusionMatrix.length; i++) {
			for (int j = 0; j < confusionMatrix.length; j++) {
				System.out.print(confusionMatrix[i][j] + "\t");
			}
			System.out.println();
		}

		return (accuracy * 100 / problem.l);
	}

	public static svm_model train(svm_problem problem, svm_parameter parameters) {
		log.info("Training and generating model");
		return svm.svm_train(problem, parameters);
	}

	public static double predict(svm_model model, svm_node[] instance) {
		log.info("Predicting class for instance");
		
		if (instance == null || instance.length == 0) {
			// no identifiable terms, then return neutral
			System.out.println("Should return neutral");
			return 2;
		}
		
		return svm.svm_predict(model, instance);
	}

	/**
	 * Get a representation for LibSVM given a text to be analyzed.
	 * 
	 * @param text
	 *            Text to be analyzed
	 * @param vocabulary
	 *            Vocabulary of the problem
	 * @param analyzer
	 *            Lucene analyzer for filtering
	 * @return Array of svm_node ready to be used in LibSVM
	 */
	public static svm_node[] getNodeVectorFromString(String text,
			TweetVocabulary vocabulary, Analyzer analyzer) {
		log.info("Get node vector for : " + text);

		HashMap<String, Integer> words = new HashMap<String, Integer>();

		Reader textReader = new StringReader(text);
		TokenStream ts = analyzer.tokenStream(null, textReader);
		CharTermAttribute charTermAttribute = ts
				.addAttribute(CharTermAttribute.class);

		try {
			while (ts.incrementToken()) {
				String term = charTermAttribute.toString();

				if (words.containsKey(term)) {
					int count = ((Integer) words.get(term)).intValue();
					count++;
					words.put(term, new Integer(count));
				} else {
					words.put(term, new Integer(1));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		//svm_node[] vector = new svm_node[words.size()];
		List<svm_node> vector = new ArrayList<svm_node>();

		for (String term : words.keySet()) {
			if (vocabulary.getNumericIndex(term) != -1) {
				svm_node vectorTerm = new svm_node();
				vectorTerm.index = vocabulary.getNumericIndex(term);
				vectorTerm.value = words.get(term).intValue();
				vector.add(vectorTerm);
				
				System.out.println(term);
			} else {
				System.out.println(term+"XXX");
			}
		}

		svm_node[] realVector = new svm_node[vector.size()];
		for (int i=0; i<vector.size(); i++) {
			realVector[i] = vector.get(i);
		}
		return realVector;
	}
}
