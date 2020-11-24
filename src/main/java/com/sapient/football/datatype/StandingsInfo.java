package com.sapient.football.datatype;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class StandingsInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("league_id")
	private String leagueId;
	@JsonProperty("league_name")
	private String leagueName;
	@JsonProperty("country_name")
	private String countryName;
	@JsonProperty("team_id")
	private String team_Id;
	@JsonProperty("team_name")
	private String teamName;
	@JsonProperty("overall_league_position")
	private String position;

}
