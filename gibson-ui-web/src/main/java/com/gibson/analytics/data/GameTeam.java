package com.gibson.analytics.data;

/**
 * This needs to be the UI model for teams.
 * 
 * @author leddy.eric
 *
 */
public class GameTeam {
	
	private String name;
	private String code;
	private String record;
	private String city;
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRecord() {
		return record;
	}
	
	/**
	 * 
	 * @param record
	 */
	public void setRecord(String record) {
		this.record = record;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * 
	 * @param city
	 */
	public void setCity(String city) {
		this.city = city;
	}

}
