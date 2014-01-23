package com.upb.datascience.spanish.analytics.text.analysis.dictionaries;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import com.upb.datascience.spanish.analytics.text.analysis.dictionaries.Stopwords;

public class StopwordsTest extends TestCase {

	private static Stopwords stopwords = new Stopwords();

	@Test
	public void testContains() {
		// Typical acronym
		Assert.assertTrue(stopwords.contains("of"));

		// Non acronym
		Assert.assertFalse(stopwords.contains("york"));
	}

	@Test
	public void testGetValue() {
		// Typical acronym
		Assert.assertEquals(stopwords.getValue("of"), "of");
		
		// Non acronym
		Assert.assertNull(stopwords.getValue("york"));
	}

}
