package com.terrasport.event;

import com.terrasport.model.Participation;

import java.io.Serializable;
import java.util.List;

public class AllParticipationEvent implements Serializable {

	private List<Participation> participations;

	public AllParticipationEvent() {}

	public AllParticipationEvent(List<Participation> participations) {
		this.participations = participations;
	}
	
	public List<Participation> getParticipations() {
		return participations;
	}

	public void setParticipations(List<Participation> participations) {
		this.participations = participations;
	}
}