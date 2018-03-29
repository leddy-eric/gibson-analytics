package com.gibson.analytics.client.baseball;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import com.gibson.analytics.data.Player;
import com.gibson.analytics.enums.MlbTeamLookup;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class BaseballRosterResponseExtractor implements ResponseExtractor<List<Player>> {

	@Override
	public List<Player> extractData(ClientHttpResponse response) throws IOException {
		List<Player> results = new ArrayList<>();
		
		if(response.getStatusCode().is2xxSuccessful()){
			Configuration configuration = Configuration.defaultConfiguration().addOptions(Option.ALWAYS_RETURN_LIST, Option.SUPPRESS_EXCEPTIONS);
			List<Map<String, Object>> players = JsonPath.parse(response.getBody(), configuration).read("$.roster_40.queryResults.row[*]");
			
			for (Map<String, Object> map : players) {
				Player p = new Player();
				
				p.setName(map.get("name_display_first_last").toString());
				String apiTeamName = map.get("team_name").toString();
				p.setTeam(MlbTeamLookup.teamFrom(apiTeamName));
				
				results.add(p);
			}
		}
		
		return results;
	}

}
