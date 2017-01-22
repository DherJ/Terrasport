package com.terrasport.event;

import com.terrasport.model.Evenement;

import java.util.List;

public class AllEvenementEvent {

	private List<Evenement> evenements;

	public List<Evenement> getEvenements() {
		return evenements;
	}

	public void setEvenements(List<Evenement> evenements) {
		this.evenements = evenements;
	}
}