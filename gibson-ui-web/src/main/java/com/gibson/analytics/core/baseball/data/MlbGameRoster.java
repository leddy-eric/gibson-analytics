package com.gibson.analytics.core.baseball.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import com.gibson.analytics.enums.MlbTeamLookup;

@Entity
public class MlbGameRoster implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column
	private MlbTeamLookup team;
	
	@Column
	private String source;
	
	@Column
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@OrderBy("battingOrder")
	private List<MlbGameActive> lineup;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "probable")
	private MlbGameActive probable;
	

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the team
	 */
	public MlbTeamLookup getTeam() {
		return team;
	}

	/**
	 * @param team the team to set
	 */
	public void setTeam(MlbTeamLookup team) {
		this.team = team;
	}

	/**
	 * @return the lineup
	 */
	public List<MlbGameActive> getLineup() {
		return lineup;
	}

	/**
	 * @param lineup the lineup to set
	 */
	public void setLineup(List<MlbGameActive> lineup) {
		this.lineup = lineup;
	}

	/**
	 * @return the probable
	 */
	public MlbGameActive getProbable() {
		return probable;
	}

	/**
	 * @param probable the probable to set
	 */
	public void setProbable(MlbGameActive probable) {
		this.probable = probable;
	}

}
