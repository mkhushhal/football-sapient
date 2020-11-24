package com.sapient.football;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sapient.football.controller.StandingsController;

import junit.framework.Assert;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StandingsControllerTests {

	@Autowired
	private TestRestTemplate template;
	
	@Test
	public void testResponseCodeShouldBeOk() throws Exception {
		String uri="/getTeamStanding?country_name=England&league_name=Championship&team_name=Bournemouth";
		
		ResponseEntity<HashMap<String,String>>   response = template.exchange(uri,HttpMethod.GET, null,new ParameterizedTypeReference<HashMap<String,String>>() {});
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	@Test
	public void testResponseData() throws Exception {
		String uri="/getTeamStanding?country_name=England&league_name=Championship&team_name=Bournemouth";
		
		ResponseEntity<HashMap<String,String>>   response = template.exchange(uri,HttpMethod.GET, null,new ParameterizedTypeReference<HashMap<String,String>>() {});
		Assert.assertEquals("41-England", response.getBody().get("Country ID & Name"));
		Assert.assertEquals(true, response.getBody().containsKey("Overall League Position"));
	}

}
