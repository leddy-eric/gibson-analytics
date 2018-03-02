package com.gibson.analytics.client.odds;

public class OddsAPIConfiguration {
	
	private String region = "us";
	private String accessLevel = "t";
	private String version = "1";
	private String languageCode = "en";
	private String oddsFormat = "us";
	private String responseFormat = "json";
	private String apiKey;
	
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	
	/**
	 * @return the accessLevel
	 */
	public String getAccessLevel() {
		return accessLevel;
	}
	
	/**
	 * @param accessLevel the accessLevel to set
	 */
	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the languageCode
	 */
	public String getLanguageCode() {
		return languageCode;
	}
	/**
	 * @param languageCode the languageCode to set
	 */
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	/**
	 * @return the oddsFormat
	 */
	public String getOddsFormat() {
		return oddsFormat;
	}
	/**
	 * @param oddsFormat the oddsFormat to set
	 */
	public void setOddsFormat(String oddsFormat) {
		this.oddsFormat = oddsFormat;
	}
	/**
	 * @return the responseFormat
	 */
	public String getResponseFormat() {
		return responseFormat;
	}
	/**
	 * @param responseFormat the responseFormat to set
	 */
	public void setResponseFormat(String responseFormat) {
		this.responseFormat = responseFormat;
	}
	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}
	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

}
