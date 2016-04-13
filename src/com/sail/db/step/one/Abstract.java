package com.sail.db.step.one;

public class Abstract {

	private String name;
	private String cited;
	private String pdf;
	private int titleId;
	/**
	 * @return the titleId
	 */
	public int getTitleId() {
		return titleId;
	}
	/**
	 * @param titleId the titleId to set
	 */
	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		if(name!=null)
		this.name = name.substring(2,name.length()-2);
	}
	/**
	 * @return the cited
	 */
	public String getCited() {
		return cited;
	}
	/**
	 * @param cited the cited to set
	 */
	public void setCited(String cited) {
		if(cited!=null)
		this.cited = cited.substring(2,cited.length()-2);
	}
	/**
	 * @return the pdf
	 */
	public String getPdf() {
		return pdf;
	}
	/**
	 * @param pdf the pdf to set
	 */
	public void setPdf(String pdf) {
		if(pdf!=null)
		this.pdf = pdf.substring(2,pdf.length()-2);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return titleId+" "+name+" "+pdf+" "+cited;
	}
	
}
