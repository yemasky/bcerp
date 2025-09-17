package com.base.model.vo;

public class WxMsgDetail {
	private String strategy;//策略类型
	private Integer errcode;//错误码，仅当该值为0时，该项结果有效
	private String suggest;	//建议，有risky、pass、review三种值
	private Integer label;	//命中标签枚举值，100 正常；10001 广告；20001 时政；20002 色情；20003 辱骂；20006 违法犯罪；20008 欺诈；20012 低俗；20013 版权；21000 其他
	private String keyword;	//命中的自定义关键词
	private Integer prob;
	public String getStrategy() {
		return strategy;
	}
	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}
	public Integer getErrcode() {
		return errcode;
	}
	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	public Integer getLabel() {
		return label;
	}
	public void setLabel(Integer label) {
		this.label = label;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getProb() {
		return prob;
	}
	public void setProb(Integer prob) {
		this.prob = prob;
	}
}
