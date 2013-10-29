package com.enonic.autotests.facets;

/**
 * Value object for Range Facet
 * 
 */
public class Range
{

	private int hits;

	private float from;

	private float to;

	public int getHits()
	{
		return hits;
	}

	public void setHits(int hits)
	{
		this.hits = hits;
	}

	public float getFrom()
	{
		return from;
	}

	public void setFrom(float from)
	{
		this.from = from;
	}

	public float getTo()
	{
		return to;
	}

	public void setTo(float to)
	{
		this.to = to;
	}

}
