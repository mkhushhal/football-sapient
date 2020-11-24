package com.sapient.football.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sapient.football.IService.IStandingService;
import com.sapient.football.datatype.CountryInfo;
import com.sapient.football.datatype.LeagueInfo;
import com.sapient.football.datatype.StandingsInfo;
import com.sapient.football.exceptions.ValidateException;

@Service
public class StandingService implements IStandingService{
	
	@Autowired
	RestTemplate restTemplate;
	
	//This should be in  application.properties, putting it here due to time constraint.
	private static final String apiKey="9bb66184e0c8145384fd2cc0f7b914ada57b4e8fd2e4d6d586adcc27c257a978";
	private static final String URL="https://apiv2.apifootball.com/";

	@Override
	public HashMap<String,String> getTeamStanding(String country, String league, String team) {
		
		HashMap<String,String> standingMap = new HashMap<String,String>();
		String countryId=getCountryId(country);
		String leagueId=getLeagueId(countryId,league);
		standingMap.put("Country ID & Name", countryId+"-"+country);
		standingMap.put("League ID & Name", leagueId+"-"+league);
		fetchTeamIdAndStanding(leagueId,team,standingMap);	
		return standingMap;
	}

	private void fetchTeamIdAndStanding(String leagueId, String team, HashMap<String, String> standingMap) {
		List<StandingsInfo> list=getStandingByLeagueId(leagueId);

		if (!isSafe(list) ) {
			returnError(URL+"get_standings doesn't return any data for league_id="+leagueId+". Please verify league_id and try again.");
		}
		
		StandingsInfo info= list.stream().filter(c->c.getTeamName().equalsIgnoreCase(team)).findAny().orElse(null);
		
		if (info==null) {
			returnError(URL+"get_standings doesn't return any data for given team_name. Please verify team_name and try again.");
		}else {
			standingMap.put("Team ID & Name", info.getTeam_Id()+"-"+info.getTeamName());
			standingMap.put("Overall League Position", info.getPosition());
		}
	}

	private String getLeagueId(String countryId, String league) {
		List<LeagueInfo> list=getLeagueListByCountryId(countryId);
		if (!isSafe(list) ) {
			returnError(URL+"get_leagues doesn't return any data for country_id="+countryId+". Please verify if downstream api is working and try again.");
		}
		
		String id= list.stream().filter(c->c.getLeagueName().equalsIgnoreCase(league)).map(c->c.getLeagueId()).findAny().orElse(null);
		
		if (id==null) {
			returnError(URL+"get_leagues doesn't return any data for given league_name. Please check country_name and try again.");
		}
		return id;
		
	}

	private String getCountryId(String country) {
		List<CountryInfo> list=getCountryList();

		if (!isSafe(list) ) {
			returnError(URL+"get_countries doesn't return any data. Please verify if downstream api is working and try again.");
		}
		
		String id= list.stream().filter(c->c.getName().equalsIgnoreCase(country)).map(c->c.getId()).findAny().orElse(null);
		
		if (id==null) {
			returnError(URL+"get_countries doesn't return any data for given coutry_name. Please check country_name and try again.");
		}
		return id;
	}
	
	private List<CountryInfo> getCountryList() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
				
		String uri = UriComponentsBuilder.fromUriString(URL)
		        .queryParam("action", "get_countries")
		        .queryParam("APIkey", apiKey).toUriString();

		HttpEntity entity = new HttpEntity(headers);

		HttpEntity<List<CountryInfo>> response = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<CountryInfo>>() {});
		return response.getBody();
	}

	private List<StandingsInfo> getStandingByLeagueId(String leagueId) {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		
		String uri = UriComponentsBuilder.fromUriString(URL)
		        // Add query parameter
		        .queryParam("action", "get_standings")
		        .queryParam("league_id", leagueId)
		        .queryParam("APIkey", apiKey).toUriString();


		HttpEntity entity = new HttpEntity(headers);

		HttpEntity<List<StandingsInfo>> response = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<StandingsInfo>>() {});
		return response.getBody();
	
	}
	
	private List<LeagueInfo> getLeagueListByCountryId(String countryId) {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		
		String uri = UriComponentsBuilder.fromUriString(URL)
		        // Add query parameter
		        .queryParam("action", "get_leagues")
		        .queryParam("country_id", countryId)
		        .queryParam("APIkey", apiKey).toUriString();
		
		

		HttpEntity entity = new HttpEntity(headers);

		HttpEntity<List<LeagueInfo>> response = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<List<LeagueInfo>>() {});
		return response.getBody();
	
	}
	
	private <T> boolean isSafe(Collection<T> list) {
		return !(list == null || list.isEmpty());
	}
	
	private void returnError(String message) {
		throw ValidateException.of().message(message).build();
	}


}
