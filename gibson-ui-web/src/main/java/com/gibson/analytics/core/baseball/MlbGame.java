package com.gibson.analytics.core.baseball;

public class MlbGame {

	private MlbLineup home;
	private MlbLineup away;
	
	/**
	 * @return the home
	 */
	public MlbLineup getHome() {
		return home;
	}
	
	/**
	 * @param home the home to set
	 */
	public void setHome(MlbLineup home) {
		this.home = home;
	}

	/**
	 * @return the away
	 */
	public MlbLineup getAway() {
		return away;
	}

	/**
	 * @param away the away to set
	 */
	public void setAway(MlbLineup away) {
		this.away = away;
	}
}
