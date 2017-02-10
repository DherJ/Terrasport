package com.terrasport.event;

import com.terrasport.model.Niveau;

import java.util.List;

public class AllNiveauEvent {

	private List<Niveau> niveaux;

	public List<Niveau> getNiveaux() {
		return niveaux;
	}

	public void setNiveaux(List<Niveau> niveaux) {
		this.niveaux = niveaux;
	}
}