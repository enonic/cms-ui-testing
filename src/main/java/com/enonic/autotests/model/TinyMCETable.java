package com.enonic.autotests.model;

/**
 * Table to insert to TinyMCE HTML-Editor
 *
 */
public class TinyMCETable
{
	private int columnsNumber;
	private int rowsNumber;
	private int cellPadding;
	private int cellSpacing;
	private int width;
	private int height;

	public int getColumnsNumber()
	{
		return columnsNumber;
	}

	public void setColumnsNumber(int columnsNumber)
	{
		this.columnsNumber = columnsNumber;
	}

	public int getRowsNumber()
	{
		return rowsNumber;
	}

	public void setRowsNumber(int rowsNumber)
	{
		this.rowsNumber = rowsNumber;
	}

	public int getCellPadding()
	{
		return cellPadding;
	}

	public void setCellPadding(int cellPadding)
	{
		this.cellPadding = cellPadding;
	}

	public int getCellSpacing()
	{
		return cellSpacing;
	}

	public void setCellSpacing(int cellSpacing)
	{
		this.cellSpacing = cellSpacing;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

}
