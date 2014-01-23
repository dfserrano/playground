package com.upb.datascience.spanish.analytics;

import com.upb.datascience.spanish.analytics.classification.SVMSentimentAnalysis;
import com.upb.datascience.spanish.analytics.text.analysis.PolarTweet;
import com.upb.datascience.spanish.analytics.utils.Keyboard;

public class SentimentAnalysisCmd {
	private Keyboard keyboard = new Keyboard();
	private SVMSentimentAnalysis analysis;

	public static void main(String[] args) {

		SentimentAnalysisCmd cmd = new SentimentAnalysisCmd();

		int menuOption = 9;

		do {
			menuOption = cmd.menu();

			switch (menuOption) {
			case 1:
				cmd.train();
				break;
			case 2:
				cmd.predict();
				break;
			case 3:
				cmd.predictFromFile();
				break;
			case 4:
				cmd.crossValidation();
				break;
			case 5:
				cmd.save();
				break;
			case 6:
				cmd.load();
				break;
			case 9:
				System.exit(0);
				break;
			default:
				System.out.println("\nType option: ");
				break;
			}
		} while (menuOption != 9);
	}

	private int menu() {
		System.out.println("--------------------------------------------");
		System.out.println("       SENTIMENT ANALYSIS COMMAND LINE      ");
		System.out.println("--------------------------------------------\n");

		System.out.println("1 - Train model");
		System.out.println("2 - Predict value for text");
		System.out.println("3 - Predict from file");
		System.out.println("4 - Cross validation");
		System.out.println("5 - Save");
		System.out.println("6 - Load");
		System.out.println("9 - Exit");

		System.out.print("\nType option: ");

		int option = keyboard.readInt();

		return option;
	}

	private void train() {
		System.out.println("TRAIN...\n");

		System.out.print("Path for tweets file: ");
		String path = keyboard.readString();

		System.out.print("Sample percentage (1-100): ");
		int samplePercentage = keyboard.readInt();

		System.out.print("C= ");
		double c = keyboard.readDouble();

		System.out.print("gamma= ");
		double gamma = keyboard.readDouble();

		analysis = new SVMSentimentAnalysis(path, samplePercentage);
		analysis.train(c, gamma, true);

		System.out.println("END OF TRAIN...\n");
	}

	private void predict() {
		System.out.println("PREDICT...\n");

		System.out.print("Text: ");
		String text = keyboard.readString();

		long timeStart = System.currentTimeMillis();
		double prediction = analysis.predict(text);
		long timeEnd = System.currentTimeMillis();
		System.out.println(prediction + " "
				+ PolarTweet.getPolarityLabel((int) prediction));
		System.out.println("Time: " + (timeEnd - timeStart) + " ms");

		System.out.println("END OF PREDICT...\n");
	}

	private void predictFromFile() {
		System.out.println("PREDICT FROM FILE...\n");

		System.out.print("Path: ");
		String path = keyboard.readString();

		long timeStart = System.currentTimeMillis();
		try {
			analysis.predictFromFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long timeEnd = System.currentTimeMillis();
		System.out.println("Time: " + (timeEnd - timeStart) + " ms");

		System.out.println("END OF PREDICT FROM FILE...\n");
	}

	private void crossValidation() {
		System.out.println("CROSS VALIDATION...\n");

		System.out.print("Path for tweets file: ");
		String path = keyboard.readString();

		System.out.print("Sample percentage (1-100): ");
		int samplePercentage = keyboard.readInt();

		System.out.print("C= ");
		double c = keyboard.readDouble();

		System.out.print("gamma= ");
		double gamma = keyboard.readDouble();

		System.out.println("k-fold= ");
		int k = keyboard.readInt();

		analysis = new SVMSentimentAnalysis(path, samplePercentage);
		double accuracy = analysis.getAccuracyFromCrossValidation(k, c, gamma,
				true);

		System.out.println("Accuracy = " + accuracy + "%");

		System.out.println("END OF CROSS VALIDATION...\n");
	}

	private void save() {
		System.out.println("SAVE...\n");

		System.out.print("Path model: ");
		String modelPath = keyboard.readString();

		System.out.print("Path vocabulary: ");
		String vocabularyPath = keyboard.readString();

		try {
			analysis.save(modelPath, vocabularyPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("END OF SAVE...\n");
	}

	private void load() {
		System.out.println("LOAD...\n");

		System.out.print("Path model: ");
		String modelPath = keyboard.readString();

		System.out.print("Path vocabulary: ");
		String vocabularyPath = keyboard.readString();

		try {
			analysis = new SVMSentimentAnalysis(modelPath, vocabularyPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("END OF LOAD...\n");
	}
}
