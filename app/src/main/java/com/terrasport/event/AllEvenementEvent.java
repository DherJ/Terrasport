package com.terrasport.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.terrasport.model.Evenement;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AllEvenementEvent {

	private List<Evenement> evenements;

	public AllEvenementEvent () {}

	public List<Evenement> getEvenements() {
		return evenements;
	}

	public void setEvenements(List<Evenement> evenements) {
		this.evenements = evenements;
	}
}