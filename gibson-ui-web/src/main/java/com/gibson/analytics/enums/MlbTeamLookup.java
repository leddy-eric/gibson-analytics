package com.gibson.analytics.enums;

/**
 * Match up Aarons team ids with the MLB team ids.
 *  
 * @author leddy.eric
 *
 */
public enum MlbTeamLookup {	
	
	ANGELS("Angels","Los Angeles Angels", "108"),
	ASTROS("Astros","Houston Astros", "117"),
	ATHLETICS("Athletics","Oakland Athletics", "133"),
	BLUE_JAYS("Blue Jays","Toronto Blue Jays", "141"),
	BRAVES("Braves","Atlanta Braves", "144"),
	BREWERS("Brewers","Milwaukee Brewers", "158"),
	CARDINALS("Cardinals","St. Louis Cardinals", "138"),
	CUBS("Cubs","Chicago Cubs", "112"),
	DIAMONDBACKS("Diamondbacks","Arizona Diamondbacks", "109"),
	DODGERS("Dodgers","Los Angeles Dodgers", "119"),
	GIANTS("Giants","San Francisco Giants", "137"),
	INDIANS("Indians","Cleveland Indians", "114"),
	MARINERS("Mariners","Seattle Mariners", "136"),
	MARLINS("Marlins","Miami Marlins", "146"),
	METS("Mets","New York Mets", "121"),
	NATIONALS("Nationals","Washington Nationals", "120"),
	ORIOLES("Orioles","Baltimore Orioles", "110"),
	PADRES("Padres","San Diego Padres", "135"),
	PHILLIES("Phillies","Philadelphia Phillies", "143"),
	PIRATES("Pirates","Pittsburgh Pirates", "134"),
	RANGERS("Rangers","Texas Rangers", "140"),
	RAYS("Rays","Tampa Bay Rays", "139"),
	RED_SOX("Red Sox","Boston Red Sox", "111"),
	REDS("Reds","Cincinnati Reds", "113"),
	ROCKIES("Rockies","Colorado Rockies", "115"),
	ROYALS("Royals","Kansas City Royals", "118"),
	TIGERS("Tigers","Detroit Tigers", "116"),
	TWINS("Twins","Minnesota Twins", "142"),
	WHITE_SOX("White Sox","Chicago White Sox", "145"),
	YANKEES("Yankees","New York Yankees", "147");
	
	private String team;
	private String apiTeam;
	private String apiIdentifier;
	
	MlbTeamLookup(String team, String apiTeam, String apiIdentifier){
		this.team = team;
		this.apiTeam = apiTeam;
		this.apiIdentifier = apiIdentifier;
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
	
	public static MlbTeamLookup lookupFromTeamName(String team) {
		for (MlbTeamLookup value : values()) {
			if(team.equals(value.team)) {
				return value;
			}
		}
		
		throw new UnsupportedOperationException("No MLB team name matches " +team);
	}


}
