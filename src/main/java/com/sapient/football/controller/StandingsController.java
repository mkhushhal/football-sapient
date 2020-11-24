package com.sapient.football.controller;

import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sapient.football.IService.IStandingService;

@RestController
public class StandingsController {
	
	@Autowired
	IStandingService standingService;
	
	@GetMapping(path = "getTeamStanding")
	public ResponseEntity<HashMap<String,String>> getTeamStanding(@Valid @RequestParam(value="country_name",required=true)String country, @RequestParam(value="league_name",required=true)String league,@RequestParam(value="team_name",required=true) String team) {
		
		HashMap<String,String> standing =  standingService.getTeamStanding(country,league,team);
		
		if (standing != null)
			return new ResponseEntity<>(standing, HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}	