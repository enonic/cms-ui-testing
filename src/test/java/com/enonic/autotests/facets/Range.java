package com.enonic.autotests.facets;

/**
 * Value object for Range Facet
 * 
 */
public class Range
{

	private int hits;

	private int from;

	private int to;

	public int getHits()
	{
		return hits;
	}

	public void setHits(int hits)
	{
		this.hits = hits;
	}

	public int getFrom()
	{
		return from;
	}

	public void setFrom(int from)
	{
		this.from = from;
	}

	public int getTo()
	{
		return to;
	}

	public void setTo(int to)
	{
		this.to = to;
	}

}
