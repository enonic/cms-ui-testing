package com.enonic.autotests.facets;

public class Histogram
{
	private float value;

	private int hits;
	
	private float min;
	
	private float max;

	private float sum;

	public int getHits()
	{
		return hits;
	}

	public void setHits(int hits)
	{
		this.hits = hits;
	}

	public float getMin()
	{
		return min;
	}

	public void setMin(float min)
	{
		this.min = min;
	}

	public float getMax()
	{
		return max;
	}

	public void setMax(float max)
	{
		this.max = max;
	}

	public float getSum()
	{
		return sum;
	}

	public void setSum(float sum)
	{
		this.sum = sum;
	}

	public float getValue()
	{
		return value;
	}

	public void setValue(float value)
	{
		this.value = value;
	}
}
