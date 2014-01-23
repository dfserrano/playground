package com.upb.datascience.spanish.analytics.text.analysis.dictionaries;

import org.junit.Assert;
import org.junit.Test;

import com.upb.datascience.spanish.analytics.text.analysis.dictionaries.Emoticons;

public class EmoticonsTest {

	private static Emoticons emoticons = new Emoticons();
	
	@Test
	public void testContains() {
		// Typical acronym
		Assert.assertTrue(emoticons.contains(":-)"));

		// Non acronym
		Assert.assertFalse(emoticons.contains("york"));
	}

	@Test
	public void testGetValue() {
		// Typical acronym
		Assert.assertEquals(emoticons.getValue(":-)"), "happy");
		
		// Non acronym
		Assert.assertNull(emoticons.getValue("york"));
	}
}
