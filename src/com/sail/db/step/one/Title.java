package com.sail.db.step.one;

import java.util.Date;

public class Title {
	public int id;
	public String name;
	public String link;
	public String showname;
	public int status;
	public int scope;
	public String year;
	public String cited;
	public String getCited() {
		return cited;
	}
	public void setCited(String cited) {
		this.cited = cited;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getScope() {
		return scope;
	}
	public void setScope(int scope) {
		this.scope = scope;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
		this.name = name;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * @return the shownam
	 */
	public String getShowname() {
		return showname;
	}
	/**
	 * @param shownam the shownam to set
	 */
	public void setShowname(String showname) {
		this.showname = showname;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	
}
