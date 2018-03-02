package com.gibson.analytics.data;

public class GameStatistic {
	
	private String name;
	private String value;
	private String vegas;
	
	/**
	 * Statistic
	 * 
	 * @param name
	 * @param value
	 */
	public GameStatistic(String name, String value) {
		this.name = name;
		this.value = value;
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
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the vegas
	 */
	public String getVegas() {
		return vegas;
	}

	/**
	 * @param vegas the vegas to set
	 */
	public void setVegas(String vegas) {
		this.vegas = vegas;
	}

}
