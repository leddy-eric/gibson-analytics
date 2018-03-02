package com.gibson.analytics.data;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

/**
 * The Class Team.
 */
@Entity
public class Team implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String name;
	
	@OneToMany
	@OrderColumn
	private List<Player> lineup;
	
	
	/**
	 * @return the lineup
	 */
	public List<Player> getLineup() {
		return lineup;
	}

	/**
	 * @param lineup the lineup to set
	 */
	public void setLineup(List<Player> lineup) {
		this.lineup = lineup;
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

}
