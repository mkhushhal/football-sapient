package com.sapient.football.IService;

import java.util.HashMap;

public interface IStandingService {
	
	HashMap<String,String>	 getTeamStanding(String country, String league, String team);

}
