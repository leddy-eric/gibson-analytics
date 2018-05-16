package com.gibson.analytics.core.baseball.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Persistent entity to track API status changes.
 * 
 * @author leddy.eric
 *
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
public class MlbGameDetail implements Serializable {
	
	/**
	 * Generated  serialVersionUID
	 */
	private static final long serialVersionUID = -4199871637802577593L;


	@Id
	@GeneratedValue
	private Long id;
	
	@Column
	private String apiId;
	
	@Column
	private LocalDate gameDate;
	
	@Enumerated(EnumType.STRING)
	@Column
	private MlbGameStatus status;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "home")
	private MlbGameRoster home;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "away")
	private MlbGameRoster away;
	
    @Column(nullable = false, updatable = false)
    @CreatedDate
    private long created;
 
    @Column
    @LastModifiedDate
    private long modified;

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
	 * @return the apiId
	 */
	public String getApiId() {
		return apiId;
	}

	/**
	 * @param apiId the apiId to set
	 */
	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	/**
	 * @return the home
	 */
	public MlbGameRoster getHome() {
		return home;
	}

	/**
	 * @param home the home to set
	 */
	public void setHome(MlbGameRoster home) {
		this.home = home;
	}

	/**
	 * @return the away
	 */
	public MlbGameRoster getAway() {
		return away;
	}

	/**
	 * @param away the away to set
	 */
	public void setAway(MlbGameRoster away) {
		this.away = away;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the gameDate
	 */
	public LocalDate getGameDate() {
		return gameDate;
	}

	/**
	 * @param gameDate the gameDate to set
	 */
	public void setGameDate(LocalDate gameDate) {
		this.gameDate = gameDate;
	}

	/**
	 * @return the status
	 */
	public MlbGameStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(MlbGameStatus status) {
		this.status = status;
	}

	/**
	 * @return the created
	 */
	public long getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(long created) {
		this.created = created;
	}

	/**
	 * @return the modified
	 */
	public long getModified() {
		return modified;
	}

	/**
	 * @param modified the modified to set
	 */
	public void setModified(long modified) {
		this.modified = modified;
	}

}
