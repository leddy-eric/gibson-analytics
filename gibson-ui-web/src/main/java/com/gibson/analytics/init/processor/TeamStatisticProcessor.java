package com.gibson.analytics.init.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;

import com.gibson.analytics.data.Player;
import com.gibson.analytics.data.PlayerStatistic;
import com.gibson.analytics.data.Team;
import com.gibson.analytics.data.TeamStatistic;
import com.gibson.analytics.init.CsvPlayerConstants;
import com.gibson.analytics.init.CsvTeamConstants;
import com.gibson.analytics.repository.PlayerRepository;
import com.gibson.analytics.repository.TeamRepository;

public class TeamStatisticProcessor implements ItemProcessor<Map<String, String>, List<TeamStatistic>>, CsvTeamConstants {

	private TeamRepository repository;
	
	private List<String> skipColumns = Arrays.asList("Team");

	public TeamStatisticProcessor(TeamRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<TeamStatistic> process(Map<String, String> row) throws Exception {
		List<TeamStatistic> stats = new ArrayList<>();
		Optional<String> teamName = Optional.ofNullable(row.get(COLUMN_NAME));

		if(teamName.isPresent()) {
			Optional<Team> team = repository.findById(teamName.get());

			// Update stats if they exist
			if(team.isPresent()) {
				
				for (Entry<String, String> entry : row.entrySet()) {
					if(!skipColumns.contains(entry.getKey())) {
						TeamStatistic s = new TeamStatistic();
						s.setName(entry.getKey());
						String value = entry.getValue();

						s.setTeamId(team.get().getName());

						if(!StringUtils.isEmpty(value) && !value.equals("NA")) {
							BigDecimal valueAsDecimal = extractValueAsDecimal(value);
							s.setValue(valueAsDecimal);
							stats.add(s);	
						}	
					}
				}
			}
		}


		return stats;
	}

	/**
	 * Translate String to big decimal.
	 * 
	 * @param value
	 * @return
	 */
	private BigDecimal extractValueAsDecimal(String value) {
		if(StringUtils.hasText(value)) {
			try {
				return new BigDecimal(value);
			} catch (Exception e) {
				System.out.println("Could not parse big decimal (" +value +") "+e.getMessage());
			}			
		}

		return BigDecimal.valueOf(0);
	}

}
