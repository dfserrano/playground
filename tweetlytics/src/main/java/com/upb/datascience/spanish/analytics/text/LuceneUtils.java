package com.upb.datascience.spanish.analytics.text;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class LuceneUtils {

	/**
	 * Some documents in the Lucene index may have been deleted or they may be
	 * empty. This method returns the effective number of documents in the
	 * index, subtracting empty and deleted documents.
	 * 
	 * @param reader
	 *            Index reader
	 * @param attribute
	 *            Name of the attribute in the index (in our case, 'tweets')
	 * @return Number of documents, without counting deleted or empty documents.
	 */
	public static int getEffectiveNumberOfDocuments(IndexReader reader,
			String attribute) {
		int numDocs = 0;

		try {
			for (int i = 0; i < reader.maxDoc(); i++) {
				if (reader.isDeleted(i)) {
					continue;
				}

				TermFreqVector tfv = reader.getTermFreqVector(i, attribute);

				if (tfv == null)
					continue;

				numDocs++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return numDocs;
	}

	/**
	 * Get index reader for the specified directory
	 * 
	 * @param directory
	 *            Lucene directory
	 * @return Index reader
	 * @throws IOException
	 */
	public static IndexReader getReader(Directory directory) throws IOException {
		return IndexReader.open(directory);
	}

	/**
	 * Get writer for directory using a given analyzer
	 * 
	 * @param directory
	 *            Lucene directory
	 * @param analyzer
	 *            Analyzer with filters for text
	 * @return Index writer
	 * @throws IOException
	 */
	public static IndexWriter getWriter(Directory directory, Analyzer analyzer)
			throws IOException {
		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,
				analyzer);
		return new IndexWriter(directory, conf);
	}
}
