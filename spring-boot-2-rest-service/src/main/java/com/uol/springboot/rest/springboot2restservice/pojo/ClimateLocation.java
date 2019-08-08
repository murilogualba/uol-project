package com.uol.springboot.rest.springboot2restservice.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClimateLocation {

	private List<Weather> consolidated_weather;

	public List<Weather> getConsolidated_weather() {
		return consolidated_weather;
	}

	public void setConsolidated_weather(List<Weather> consolidated_weather) {
		this.consolidated_weather = consolidated_weather;
	}

}
