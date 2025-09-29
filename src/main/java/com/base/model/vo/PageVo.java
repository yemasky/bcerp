package com.base.model.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageVo {
	private int perPage = 2;
	private int allPages = 0;
	private int currentPage = 1;
	private int allNum = 0;
	private List<HashMap<String, Object>> pageList = new ArrayList<>();
	
	public int getPerPage() {
		return perPage;
	}
	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}
	public int getAllPages() {
		return allPages;
	}
	public void setAllPages(int allPages) {
		this.allPages = allPages;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getAllNum() {
		return allNum;
	}
	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}

	public List<HashMap<String, Object>> getPageList() {
		return pageList;
	}
	public void setPageList(List<HashMap<String, Object>> pageList) {
		this.pageList = pageList;
	}
}
