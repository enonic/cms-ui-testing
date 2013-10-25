package com.enonic.autotests.facets;

/**
 * Value object for Term Facet.
 *
 */
public class Term
{

	private int hits;
	
	private String value;
	
	public int getHits()
	{
		return hits;
	}
	public void setHits(int hits)
	{
		this.hits = hits;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}

}
