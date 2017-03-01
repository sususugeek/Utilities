package com.pccw.util.search;

import java.io.Serializable;
import java.util.List;

public class SearchResult<E> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1231701829198265954L;
	private int totalCount;
	private List<E> result;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<E> getResult() {
		return result;
	}

	public void setResult(List<E> result) {
		this.result = result;
	}

	public static <E> SearchResult<E> newInstance() {
		return new SearchResult<E>();
	}
}