package com.sapient.football.datatype;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CountryInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	@JsonProperty("country_id")
	private String id;
	@JsonProperty("country_name")
	private String name;
	@JsonProperty("country_logo")
	private String logo;
}
