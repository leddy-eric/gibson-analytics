package com.gibson.analytics.enums;

/**
 * Match up Aarons team ids with the MLB team ids.
 *  
 * @author leddy.eric
 *
 */
public enum MlbTeamLookup {	
	
	ANGELS("Angels","Los Angeles Angels", "108", League.AL),
	ASTROS("Astros","Houston Astros", "117", League.AL),
	ATHLETICS("Athletics","Oakland Athletics", "133", League.AL),
	BLUE_JAYS("Blue Jays","Toronto Blue Jays", "141", League.AL),
	BRAVES("Braves","Atlanta Braves", "144", League.NL),
	BREWERS("Brewers","Milwaukee Brewers", "158", League.NL),
	CARDINALS("Cardinals","St. Louis Cardinals", "138", League.NL),
	CUBS("Cubs","Chicago Cubs", "112", League.NL),
	DIAMONDBACKS("Diamondbacks","Arizona Diamondbacks", "109", League.NL),
	DODGERS("Dodgers","Los Angeles Dodgers", "119", League.NL),
	GIANTS("Giants","San Francisco Giants", "137", League.NL),
	INDIANS("Indians","Cleveland Indians", "114", League.AL),
	MARINERS("Mariners","Seattle Mariners", "136", League.AL),
	MARLINS("Marlins","Miami Marlins", "146", League.NL),
	METS("Mets","New York Mets", "121", League.NL),
	NATIONALS("Nationals","Washington Nationals", "120", League.NL),
	ORIOLES("Orioles","Baltimore Orioles", "110", League.AL),
	PADRES("Padres","San Diego Padres", "135", League.NL),
	PHILLIES("Phillies","Philadelphia Phillies", "143", League.NL),
	PIRATES("Pirates","Pittsburgh Pirates", "134", League.NL),
	RANGERS("Rangers","Texas Rangers", "140", League.AL),
	RAYS("Rays","Tampa Bay Rays", "139", League.AL),
	RED_SOX("Red Sox","Boston Red Sox", "111", League.AL),
	REDS("Reds","Cincinnati Reds", "113", League.NL),
	ROCKIES("Rockies","Colorado Rockies", "115", League.NL),
	ROYALS("Royals","Kansas City Royals", "118", League.AL),
	TIGERS("Tigers","Detroit Tigers", "116", League.AL),
	TWINS("Twins","Minnesota Twins", "142", League.AL),
	WHITE_SOX("White Sox","Chicago White Sox", "145", League.AL),
	YANKEES("Yankees","New York Yankees", "147", League.AL),
	UNKNOWN("Unknown","Unknown API Team", "-1", League.AL);
	
	private String team;
	private String apiTeam;
	private String apiIdentifier;
	private League league;
	
	MlbTeamLookup(String team, String apiTeam, String apiIdentifier, League league){
		this.team = team;
		this.apiTeam = apiTeam;
		this.apiIdentifier = apiIdentifier;
		this.league= league;
	}
	
	public String apiIdentifier() {
		return apiIdentifier;
	}
	
	public String team() {
		return team;
	}
	
	public static String teamFrom(String apiTeam) {
		for (MlbTeamLookup value : values()) {
			if(apiTeam.equals(value.apiTeam)) {
				return value.team;
			}
		}
		
		throw new UnsupportedOperationException("No MLB team name matches " +apiTeam);	
	}
	
	public static MlbTeamLookup lookupFrom(String apiIdentifier) {
		for (MlbTeamLookup value : values()) {
			if(apiIdentifier.equals(value.apiIdentifier)) {
				return value;
			}
		}
		
		throw new UnsupportedOperationException("No MLB team name matches " +apiIdentifier);	
	}
	
	/**
	 * Look up the enum by the team name used in the application db.
	 * 
	 * @param team
	 * @return
	 */
	public static MlbTeamLookup lookupFromTeamName(String team) {
		for (MlbTeamLookup value : values()) {
			if(team.equals(value.team)) {
				return value;
			}
		}
		
		throw new UnsupportedOperationException("No MLB team name matches " +team);
	}

	/**
	 * Checks the league for this team.
	 * 
	 * @return
	 */
	public boolean isAmericanLeague() {
		return League.AL.equals(this.league);
	}

}
