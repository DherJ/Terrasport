package com.terrasport.event;

import com.terrasport.model.DemandeParticipation;

import java.util.List;

public class AllDemandeParticipationEvent {

	private List<DemandeParticipation> demandeParticipation;

	public List<DemandeParticipation> getDemandeParticipations() {
		return demandeParticipation;
	}

	public void setDemandeParticipations(List<DemandeParticipation> demandeParticipation) {
		this.demandeParticipation = demandeParticipation;
	}
}